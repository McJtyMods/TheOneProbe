package mcjty.theoneprobe.mixin.client;

import com.google.common.collect.Maps;
import mcjty.theoneprobe.TheOneProbe;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorLayerMixin {
    @Unique
    private static final ResourceLocation theoneprobe$helment = TheOneProbe.asResource("textures/models/armor/gold_helmet_probe.png");
    @Inject(method = "getArmorLocation", at = @At("HEAD"), cancellable = true)
    private void theoneprobe$getArmorLocation(ArmorItem armorItem, boolean legs, String overlay, CallbackInfoReturnable<ResourceLocation> cir) {
        if (armorItem.getMaterial().getName().contains("theoneprobe")) {
            String name = armorItem.getMaterial().getName();
            String path = "textures/models/armor/" + name.subSequence(12, name.length()) + "_layer_" + (legs ? 2 : 1) + (overlay == null ? "" : "_" + overlay) + ".png";
            cir.setReturnValue(ARMOR_LOCATION_CACHE.computeIfAbsent(path, ResourceLocation::new));
        }
    }

    @Shadow @Final private static Map<String, ResourceLocation> ARMOR_LOCATION_CACHE;
}
