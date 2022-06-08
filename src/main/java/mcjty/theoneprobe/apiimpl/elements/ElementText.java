package mcjty.theoneprobe.apiimpl.elements;

import com.mojang.blaze3d.vertex.PoseStack;

import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.ITextStyle;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.client.ElementTextRender;
import mcjty.theoneprobe.apiimpl.styles.TextStyle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ElementText implements IElement {

    private final Component text;
    private final ITextStyle style;
    //Compound Text support
    private boolean legacy = false;

    public ElementText(String text) {
        this(Component.translatable(text), new TextStyle());
    }

    public ElementText(String text, ITextStyle style) {
        this(Component.translatable(text), style);
    }

    public ElementText(Component text) {
        this(text, new TextStyle());
    }

    public ElementText(Component text, ITextStyle style) {
        this.text = text;
        this.style = style;
    }

    public ElementText(FriendlyByteBuf buf) {
        text = buf.readComponent();
        style = new TextStyle().alignment(buf.readEnum(ElementAlignment.class)).topPadding(buf.readInt()).bottomPadding(buf.readInt()).leftPadding(buf.readInt()).rightPadding(buf.readInt());
        if (buf.readBoolean()) {
            style.width(buf.readInt());
        }
        if (buf.readBoolean()) {
            style.height(buf.readInt());
        }
        legacy = buf.readBoolean();

    }

    //For Allowing to edit styles afterwards if a method decides on the style
    public ITextStyle getStyle() {
        return style;
    }

    public ElementText setLegacy() {
        legacy = true;
        return this;
    }

    @Override
    public void render(PoseStack matrixStack, int x, int y) {
        int width = getTextWidth();
        switch (style.getAlignment()) {
            case ALIGN_BOTTOMRIGHT -> ElementTextRender.render(text, matrixStack, (x + getInternalWidth() - width) + style.getLeftPadding(), y + style.getTopPadding(), legacy);
            case ALIGN_CENTER -> ElementTextRender.render(text, matrixStack, ((x + (getInternalWidth() / 2)) - (width / 2)) + style.getLeftPadding(), y + style.getTopPadding(), legacy);
            case ALIGN_TOPLEFT -> ElementTextRender.render(text, matrixStack, x + style.getLeftPadding(), y + style.getTopPadding(), legacy);
        }
    }

    protected int getTextWidth() {
        return legacy ? ElementTextRender.getLegacyWidth(text) : ElementTextRender.getWidth(text);
    }

    protected int getInternalWidth() {
        return (style.getWidth() != null ? style.getWidth() : getTextWidth());
    }

    @Override
    public int getWidth() {
        return style.getLeftPadding() + (style.getWidth() != null ? style.getWidth() : getTextWidth()) + style.getRightPadding();
    }

    @Override
    public int getHeight() {
        return style.getTopPadding() + (style.getHeight() != null ? style.getHeight() : ElementTextRender.getHeight()) + style.getBottomPadding();
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeComponent(text);
        buffer.writeEnum(style.getAlignment()).writeInt(style.getTopPadding()).writeInt(style.getBottomPadding()).writeInt(style.getLeftPadding()).writeInt(style.getRightPadding());
        buffer.writeBoolean(style.getWidth() != null);
        if (style.getWidth() != null) {
            buffer.writeInt(style.getWidth());
        }
        buffer.writeBoolean(style.getHeight() != null);
        if (style.getHeight() != null) {
            buffer.writeInt(style.getHeight());
        }
        buffer.writeBoolean(legacy);
    }

    @Override
    public ResourceLocation getID() {
        return TheOneProbeImp.ELEMENT_TEXT;
    }
}
