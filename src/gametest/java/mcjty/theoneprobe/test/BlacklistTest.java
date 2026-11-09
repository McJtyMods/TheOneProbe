package mcjty.theoneprobe.test;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.IProbeConfigProvider;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.items.ModItems;
import mcjty.theoneprobe.network.PacketGetEntityInfo;
import mcjty.theoneprobe.network.PacketGetInfo;
import mcjty.theoneprobe.rendering.OverlayRenderer;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/** Checks conditional suppression before cached rendering and server provider calls. */
public class BlacklistTest implements FabricClientGameTest {

    @Override
    public void runTest(ClientGameTestContext context) {
        try (var world = context.worldBuilder().create()) {
            context.waitFor(client -> client.player != null && client.level != null);
            BlockPos target = new BlockPos(0, -59, 0);
            var uuid = context.computeOnClient(client -> client.player.getUUID());
            AtomicBoolean hidden = new AtomicBoolean();
            AtomicInteger providerCalls = new AtomicInteger();
            var probe = TheOneProbe.theOneProbeImp;
            IProbeConfigProvider provider = new IProbeConfigProvider() {

                @Override
                public void getProbeConfig(IProbeConfig config, Player player, Level level, Entity entity, IProbeHitEntityData data) {
                    providerCalls.incrementAndGet();
                }

                @Override
                public void getProbeConfig(IProbeConfig config, Player player, Level level, BlockState state, IProbeHitData data) {
                    providerCalls.incrementAndGet();
                }
            };
            probe.registerProbeConfigProvider(provider);
            // A preceding non-match must allow later predicates to hide the target.
            probe.registerBlockBlacklist((player, level, pos, state) -> false);
            probe.registerEntityBlacklist((player, entity) -> false);
            probe.registerBlockBlacklist((player, level, pos, state) -> {
                if (!hidden.get() || !pos.equals(target)) {
                    return false;
                }
                check(level == player.level() && state == level.getBlockState(pos), "Predicate must receive the current world and state");
                return true;
            });
            probe.registerEntityBlacklist((player, entity) -> hidden.get() && entity.getUUID().equals(uuid));
            try {
                world.getServer().runOnServer(server -> {
                    var player = server.getPlayerList().getPlayers().getFirst();
                    player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModItems.PROBE));
                    server.overworld().setBlockAndUpdate(target, Blocks.STONE.defaultBlockState());
                    checkServerInfo(player, target, true);
                    check(providerCalls.get() == 2, "Visible targets must reach config providers");
                });
                context.waitFor(client -> client.level.getBlockState(target).is(Blocks.STONE));
                hidden.set(true);
                world.getServer().runOnServer(server -> {
                    providerCalls.set(0);
                    checkServerInfo(server.getPlayerList().getPlayers().getFirst(), target, false);
                    check(providerCalls.get() == 0, "Hidden targets must skip config providers");
                });
                context.runOnClient(client -> {
                    // Populate both caches. Rendering must return before touching the null graphics.
                    ProbeInfo cached = new ProbeInfo();
                    cached.text("Cached information must stay hidden");
                    OverlayRenderer.registerProbeInfo(client.level.dimension(), target, cached);
                    OverlayRenderer.registerProbeInfo(uuid, cached);
                    BlockHitResult blockHit = new BlockHitResult(Vec3.atCenterOf(target), Direction.UP, target, false);
                    invoke(OverlayRenderer.class, "renderHUDBlock", null, ProbeMode.NORMAL, blockHit, 320.0, 240.0);
                    invoke(OverlayRenderer.class, "renderHUDEntity", null, ProbeMode.NORMAL, new EntityHitResult(client.player), 320.0, 240.0);
                });
                hidden.set(false);
                world.getServer().runOnServer(server -> {
                    providerCalls.set(0);
                    checkServerInfo(server.getPlayerList().getPlayers().getFirst(), target, true);
                    check(providerCalls.get() == 2, "Targets must become visible again when conditions change");
                });
            } finally {
                hidden.set(false);
                probe.getConfigProviders().remove(provider);
            }
        }
    }

    private static void checkServerInfo(Player player, BlockPos pos, boolean visible) {
        Object blockInfo = invoke(PacketGetInfo.class, "getProbeInfo", player, ProbeMode.NORMAL,
                player.level(), pos, Direction.UP, Vec3.atCenterOf(pos), new ItemStack(Blocks.STONE));
        Object entityInfo = invoke(PacketGetEntityInfo.class, "getProbeInfo", player, ProbeMode.NORMAL,
                player.level(), player, player.position());
        check((blockInfo != null) == visible, "Block response must reflect the current blacklist condition");
        check((entityInfo != null) == visible, "Entity response must reflect the current blacklist condition");
    }

    private static Object invoke(Class<?> owner, String name, Object... arguments) {
        Method method = Arrays.stream(owner.getDeclaredMethods())
                .filter(candidate -> candidate.getName().equals(name))
                .findFirst().orElseThrow();
        method.setAccessible(true);
        try {
            return method.invoke(null, arguments);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError("Failed to call " + owner.getSimpleName() + "." + name, e);
        }
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
