package mcjty.theoneprobe.rendering;

import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.rendering.OverlayRenderer;

public class HudRenderer {

    public void draw() {
        OverlayRenderer.renderHUD(ProbeMode.EXTENDED, 0.0f);
    }
}
