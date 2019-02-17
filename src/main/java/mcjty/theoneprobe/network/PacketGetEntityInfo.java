package mcjty.theoneprobe.network;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.ProbeHitEntityData;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.items.ModItems;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import java.util.List;
import java.util.UUID;

import static mcjty.theoneprobe.api.TextStyleClass.ERROR;
import static mcjty.theoneprobe.api.TextStyleClass.LABEL;
import static mcjty.theoneprobe.config.Config.PROBE_NEEDEDFOREXTENDED;
import static mcjty.theoneprobe.config.Config.PROBE_NEEDEDHARD;

public class PacketGetEntityInfo implements IPacket {

    public static final Identifier GET_ENTITY_INFO = new Identifier(TheOneProbe.MODID, "get_entity_info");

    private int dim;
    private UUID uuid;
    private ProbeMode mode;
    private Vec3d hitVec;

    @Override
    public Identifier getId() {
        return GET_ENTITY_INFO;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        dim = buf.readInt();
        uuid = new UUID(buf.readLong(), buf.readLong());
        mode = ProbeMode.values()[buf.readByte()];
        if (buf.readBoolean()) {
            hitVec = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(dim);
        buf.writeLong(uuid.getMostSignificantBits());
        buf.writeLong(uuid.getLeastSignificantBits());
        buf.writeByte(mode.ordinal());
        if (hitVec == null) {
            buf.writeBoolean(false);
        } else {
            buf.writeBoolean(true);
            buf.writeDouble(hitVec.x);
            buf.writeDouble(hitVec.y);
            buf.writeDouble(hitVec.z);
        }
    }

    public PacketGetEntityInfo() {
    }

    public PacketGetEntityInfo(int dim, ProbeMode mode, HitResult mouseOver, Entity entity) {
        this.dim = dim;
        this.uuid = entity.getUuid();
        this.mode = mode;
        this.hitVec = mouseOver.getPos();
    }

    public static class Handler extends MessageHandler<PacketGetEntityInfo> {

        @Override
        protected PacketGetEntityInfo createPacket() {
            return new PacketGetEntityInfo();
        }

        @Override
        void handle(PacketContext context, PacketGetEntityInfo message) {
            ServerWorld world = context.getPlayer().getEntityWorld().getServer().getWorld(DimensionType.byRawId(message.dim));
            if (world != null) {
                Entity entity = world.getEntity(message.uuid);
                if (entity != null) {
                    ProbeInfo probeInfo = getProbeInfo(context.getPlayer(), message.mode, world, entity, message.hitVec);
                    NetworkInit.sendToClient(new PacketReturnEntityInfo(message.uuid, probeInfo), (ServerPlayerEntity) context.getPlayer());
                }
            }
        }
    }

    private static ProbeInfo getProbeInfo(PlayerEntity player, ProbeMode mode, World world, Entity entity, Vec3d hitVec) {
        if (Config.needsProbe == PROBE_NEEDEDFOREXTENDED) {
            // We need a probe only for extended information
            if (!ModItems.hasAProbeSomewhere(player)) {
                // No probe anywhere, switch EXTENDED to NORMAL
                if (mode == ProbeMode.EXTENDED) {
                    mode = ProbeMode.NORMAL;
                }
            }
        } else if (Config.needsProbe == PROBE_NEEDEDHARD && !ModItems.hasAProbeSomewhere(player)) {
            // The server says we need a probe but we don't have one in our hands or on our head
            return null;
        }

        ProbeInfo probeInfo = TheOneProbe.theOneProbeImp.create();
        IProbeHitEntityData data = new ProbeHitEntityData(hitVec);

        IProbeConfig probeConfig = TheOneProbe.theOneProbeImp.createProbeConfig();
        List<IProbeConfigProvider> configProviders = TheOneProbe.theOneProbeImp.getConfigProviders();
        for (IProbeConfigProvider configProvider : configProviders) {
            configProvider.getProbeConfig(probeConfig, player, world, entity, data);
        }
        Config.setRealConfig(probeConfig);

        List<IProbeInfoEntityProvider> entityProviders = TheOneProbe.theOneProbeImp.getEntityProviders();
        for (IProbeInfoEntityProvider provider : entityProviders) {
            try {
                provider.addProbeEntityInfo(mode, probeInfo, player, world, entity, data);
            } catch (Throwable e) {
                ThrowableIdentity.registerThrowable(e);
                probeInfo.text(LABEL + "Error: " + ERROR + provider.getID());
            }
        }
        return probeInfo;
    }

}
