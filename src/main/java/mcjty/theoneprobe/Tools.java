package mcjty.theoneprobe;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.config.Config;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.ModList;
import org.apache.commons.lang3.StringUtils;

import static mcjty.theoneprobe.api.IProbeConfig.ConfigMode.EXTENDED;
import static mcjty.theoneprobe.api.IProbeConfig.ConfigMode.NORMAL;

public class Tools {

    public static <B extends FriendlyByteBuf, V extends Enum<V>> StreamCodec<B, V> enumCodecNullable(final Class<V> enumClass) {
        return new StreamCodec<B, V>() {
            public V decode(B buf) {
                int idx = buf.readVarInt();
                if (idx == -1) {
                    return null;
                } else {
                    return enumClass.getEnumConstants()[idx];
                }
            }

            public void encode(B buf, V value) {
                if (value == null) {
                    buf.writeVarInt(-1);
                } else {
                    buf.writeVarInt(value.ordinal());
                }
            }
        };
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemStack> SAFE_ITEMSTACK_STREAM_CODEC = new StreamCodec<>() {
        private static final StreamCodec<RegistryFriendlyByteBuf, Holder<Item>> ITEM_STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.ITEM);

        public ItemStack decode(RegistryFriendlyByteBuf buf) {
            return ItemStack.OPTIONAL_STREAM_CODEC.decode(buf);
        }

        public void encode(RegistryFriendlyByteBuf buf, ItemStack stack) {
            if (stack.isEmpty()) {
                buf.writeVarInt(0);
            } else {
                buf.writeVarInt(stack.getCount());
                ITEM_STREAM_CODEC.encode(buf, stack.getItemHolder());
                RegistryFriendlyByteBuf buffer = new RegistryFriendlyByteBuf(Unpooled.buffer(), buf.registryAccess());
                DataComponentPatch.STREAM_CODEC.encode(buffer, stack.getComponentsPatch());
                if (buffer.writerIndex() <= Config.maxPacketToServer.get()) {
                    DataComponentPatch.STREAM_CODEC.encode(buf, stack.getComponentsPatch());
                } else {
                    ItemStack copy = new ItemStack(stack.getItem(), stack.getCount());
                    DataComponentPatch.STREAM_CODEC.encode(buf, copy.getComponentsPatch());
                }
            }
        }
    };

    public static final StreamCodec<FriendlyByteBuf, Vec3> VEC3_CODEC = new StreamCodec<>() {
        @Override
        public Vec3 decode(FriendlyByteBuf buf) {
            if (buf.readBoolean()) {
                return new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
            } else {
                return null;
            }
        }

        @Override
        public void encode(FriendlyByteBuf buf, Vec3 vec3) {
            if (vec3 == null) {
                buf.writeBoolean(false);
            } else {
                buf.writeBoolean(true);
                buf.writeDouble(vec3.x);
                buf.writeDouble(vec3.y);
                buf.writeDouble(vec3.z);
            }
        }
    };

    public static String getModName(EntityType<?> entry) {
        ResourceLocation registryName = BuiltInRegistries.ENTITY_TYPE.getKey(entry);
        String modId = registryName == null ? "minecraft" : registryName.getNamespace();
        return ModList.get().getModContainerById(modId)
                .map(mod -> mod.getModInfo().getDisplayName())
                .orElse(StringUtils.capitalize(modId));
    }

    public static String getModName(Block entry) {
        ResourceLocation registryName = BuiltInRegistries.BLOCK.getKey(entry);
        String modId = registryName == null ? "minecraft" : registryName.getNamespace();
        return ModList.get().getModContainerById(modId)
                .map(mod -> mod.getModInfo().getDisplayName())
                .orElse(StringUtils.capitalize(modId));
    }

    public static boolean show(ProbeMode mode, IProbeConfig.ConfigMode cfg) {
        return cfg == NORMAL || (cfg == EXTENDED && mode == ProbeMode.EXTENDED);
    }
}
