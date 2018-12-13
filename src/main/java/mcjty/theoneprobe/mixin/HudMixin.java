package mcjty.theoneprobe.mixin;

import mcjty.theoneprobe.rendering.HudRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class HudMixin {

    private HudRenderer renderer = new HudRenderer();

    @Inject(at = @At("RETURN"), method = "draw(F)V")
    public void draw(float elapsedTicks, CallbackInfo info) {
        renderer.draw();
    }
}
