package mcjty.theoneprobe.api;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.material.Fluid;

/** Fluid variant and amount in millibuckets (one bucket = 1000 mB). */
public record FluidStack(FluidVariant variant, int amount) {
    public static final StreamCodec<RegistryFriendlyByteBuf, FluidStack> STREAM_CODEC = StreamCodec.of(
            (buf, stack) -> {
                buf.writeIdentifier(BuiltInRegistries.FLUID.getKey(stack.getFluid()));
                DataComponentPatch.STREAM_CODEC.encode(buf, stack.variant.getComponentsPatch());
                buf.writeVarInt(stack.amount);
            }, buf -> new FluidStack(FluidVariant.of(BuiltInRegistries.FLUID.getValue(buf.readIdentifier()),
                    DataComponentPatch.STREAM_CODEC.decode(buf)), buf.readVarInt()));
    public static final StreamCodec<RegistryFriendlyByteBuf, FluidStack> OPTIONAL_STREAM_CODEC = STREAM_CODEC;

    public FluidStack(Fluid fluid, int amount) { this(FluidVariant.of(fluid), amount); }
    public Fluid getFluid() { return variant.getFluid(); }
    public int getAmount() { return amount; }
    public boolean isEmpty() { return variant.isBlank() || amount <= 0; }
    public Component getHoverName() { return FluidVariantAttributes.getName(variant); }
}
