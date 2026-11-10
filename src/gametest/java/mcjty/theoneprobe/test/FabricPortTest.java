package mcjty.theoneprobe.test;

import io.netty.buffer.Unpooled;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.FluidStack;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.TankReference;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.gui.GuiConfig;
import mcjty.theoneprobe.gui.GuiNote;
import mcjty.theoneprobe.items.ModItems;
import mcjty.theoneprobe.network.PacketGetEntityInfo;
import mcjty.theoneprobe.network.PacketGetInfo;
import mcjty.theoneprobe.rendering.OverlayRenderer;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;

/** Exercises the port in an actual client and integrated server. Not included in the release jar. */
public class FabricPortTest implements FabricClientGameTest {
    @Override
    public void runTest(ClientGameTestContext context) {
        try (var world = context.worldBuilder().create()) {
            context.waitFor(client -> client.player != null && client.level != null);
            world.getServer().runOnServer(server -> {
                var player = server.getPlayerList().getPlayers().getFirst();
                check(player.getAttachedOrCreate(TheOneProbe.PLAYER_GOT_NOTE), "Join should record receipt of the note");
                check(player.getInventory().contains(new ItemStack(ModItems.PROBE_NOTE)), "Join should give a probe note");
                player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, new ItemStack(ModItems.PROBE));
                server.overworld().setBlockAndUpdate(new BlockPos(0, -59, 0), net.minecraft.world.level.block.Blocks.CHEST.defaultBlockState());
                var chest = (net.minecraft.world.level.block.entity.ChestBlockEntity) server.overworld().getBlockEntity(new BlockPos(0, -59, 0));
                chest.setItem(0, new ItemStack(Items.DIAMOND, 7));
            });
            context.runOnClient(client -> testFluidCodec(client.level.registryAccess()));
            context.runOnClient(client -> ClientPlayNetworking.send(new PacketGetInfo(
                    Level.OVERWORLD, new BlockPos(0, -59, 0), ProbeMode.EXTENDED, Direction.UP,
                    new Vec3(0.5, -58, 0.5), new ItemStack(Items.CHEST))));
            context.waitFor(client -> cached("cachedInfo", Pair.of(Level.OVERWORLD, new BlockPos(0, -59, 0))) != null);
            context.runOnClient(client -> {
                ProbeInfo info = cached("cachedInfo", Pair.of(Level.OVERWORLD, new BlockPos(0, -59, 0)));
                check(!info.getElements().isEmpty(), "Block packet should decode a populated overlay");
                check(containsDiamond(info), "Fabric item storage lookup should return chest contents");
            });
            UUID uuid = context.computeOnClient(client -> client.player.getUUID());
            context.runOnClient(client -> ClientPlayNetworking.send(new PacketGetEntityInfo(
                    Level.OVERWORLD, uuid, ProbeMode.EXTENDED, client.player.position())));
            context.waitFor(client -> cached("cachedEntityInfo", uuid) != null);
            world.getServer().runCommand("execute as @p run top config");
            context.waitForScreen(GuiConfig.class);
            context.waitTicks(2);
            context.setScreen(() -> null);
            world.getServer().runCommand("execute as @p run top need");
            context.waitForScreen(GuiNote.class);
            context.waitTicks(2);
            context.setScreen(() -> null);
        }
    }

    private static boolean containsDiamond(mcjty.theoneprobe.apiimpl.elements.AbstractElementPanel panel) {
        for (var element : panel.getElements()) {
            if (element instanceof mcjty.theoneprobe.apiimpl.elements.ElementItemStack item
                    && itemStack(item).is(Items.DIAMOND) && itemStack(item).getCount() == 7) return true;
            if (element instanceof mcjty.theoneprobe.apiimpl.elements.AbstractElementPanel child && containsDiamond(child)) return true;
        }
        return false;
    }

    private static ItemStack itemStack(mcjty.theoneprobe.apiimpl.elements.ElementItemStack item) {
        try {
            Field field = item.getClass().getDeclaredField("itemStack");
            field.setAccessible(true);
            return (ItemStack) field.get(item);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    private static void testFluidCodec(net.minecraft.core.RegistryAccess registries) {
        var variant = FluidVariant.of(Fluids.WATER, DataComponentPatch.builder()
                .set(DataComponents.CUSTOM_NAME, Component.literal("Test water")).build());
        var stack = new FluidStack(variant, 1000);
        var buffer = new RegistryFriendlyByteBuf(Unpooled.buffer(), registries);
        try {
            FluidStack.STREAM_CODEC.encode(buffer, stack);
            check(stack.equals(FluidStack.STREAM_CODEC.decode(buffer)), "Fluid packets should preserve amount and components");
            SingleVariantStorage<FluidVariant> storage = new SingleVariantStorage<>() {
                protected FluidVariant getBlankVariant() { return FluidVariant.blank(); }
                protected long getCapacity(FluidVariant resource) { return 162000; }
            };
            storage.variant = variant;
            storage.amount = 81000;
            var tank = TankReference.createHandler(storage);
            check(tank.getStored() == 1000 && tank.getCapacity() == 2000, "Fabric droplets should display as millibuckets");
            tank.toBytes(buffer);
            var decoded = new TankReference(buffer);
            check(decoded.getFluids()[0].equals(stack), "Tank packets should preserve fluid components");
            check(TankReference.toMillibuckets(Long.MAX_VALUE) == Integer.MAX_VALUE, "Large tank capacities should not overflow");
        } finally {
            buffer.release();
        }
    }

    @SuppressWarnings("unchecked")
    private static ProbeInfo cached(String fieldName, Object key) {
        try {
            Field field = OverlayRenderer.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            var entry = ((Map<Object, Pair<Long, ProbeInfo>>) field.get(null)).get(key);
            return entry == null ? null : entry.getRight();
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
