package mcjty.theoneprobe.apiimpl.elements;

import com.mojang.blaze3d.matrix.MatrixStack;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.client.ElementTextRender;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;

public class ElementText implements IElement {

    private final ITextComponent text;

    public ElementText(ITextComponent text) {
        this.text = text;
    }

    public ElementText(PacketBuffer buf) {
        text = buf.readTextComponent();
    }

    @Override
    public void render(MatrixStack matrixStack, int x, int y) {
        ElementTextRender.render(text, matrixStack, x, y);
    }

    @Override
    public int getWidth() {
        return ElementTextRender.getWidth(text);
    }

    @Override
    public int getHeight() {
        return 10;
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeTextComponent(text);
    }

    @Override
    public int getID() {
        return TheOneProbeImp.ELEMENT_TEXT;
    }
}
