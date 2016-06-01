package mcjty.theoneprobe.apiimpl;

import mcjty.theoneprobe.Config;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.IOverlayRenderer;
import mcjty.theoneprobe.api.IOverlayStyle;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.apiimpl.styles.DefaultOverlayStyle;
import mcjty.theoneprobe.rendering.OverlayRenderer;

public class DefaultOverlayRenderer implements IOverlayRenderer {

    @Override
    public IOverlayStyle createDefaultStyle() {
        return ((DefaultOverlayStyle)Config.getDefaultOverlayStyle()).copy();
    }

    @Override
    public IProbeInfo createProbeInfo() {
        return TheOneProbe.theOneProbeImp.create();
    }

    @Override
    public void render(IOverlayStyle style, IProbeInfo probeInfo) {
        OverlayRenderer.renderOverlay(style, probeInfo);
    }
}
