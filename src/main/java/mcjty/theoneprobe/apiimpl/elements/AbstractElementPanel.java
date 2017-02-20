package mcjty.theoneprobe.apiimpl.elements;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.apiimpl.styles.*;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractElementPanel implements IElement, IProbeInfo {

    protected List<IElement> children = new ArrayList<>();
    protected Integer borderColor;
    protected int spacing;
    protected ElementAlignment alignment;
    protected IProbeConfig overriddenConfig;

    @Override
    public void render(int x, int y) {
        if (borderColor != null) {
            int w = getWidth();
            int h = getHeight();
            RenderHelper.drawHorizontalLine(x, y, x + w - 1, borderColor);
            RenderHelper.drawHorizontalLine(x, y + h - 1, x + w - 1, borderColor);
            RenderHelper.drawVerticalLine(x, y, y + h - 1, borderColor);
            RenderHelper.drawVerticalLine(x + w - 1, y, y + h, borderColor);
        }
    }

    public AbstractElementPanel(Integer borderColor, int spacing, ElementAlignment alignment) {
        this.borderColor = borderColor;
        this.spacing = spacing;
        this.alignment = alignment;
    }

    public AbstractElementPanel(ByteBuf buf) {
        children = ProbeInfo.createElements(buf);
        if (buf.readBoolean()) {
            borderColor = buf.readInt();
        }
        spacing = buf.readShort();
        alignment = ElementAlignment.values()[buf.readShort()];
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ProbeInfo.writeElements(children, buf);
        if (borderColor != null) {
            buf.writeBoolean(true);
            buf.writeInt(borderColor);
        } else {
            buf.writeBoolean(false);
        }
        buf.writeShort(spacing);
        buf.writeShort(alignment.ordinal());
    }

    @Override
    public IProbeInfo icon(ResourceLocation icon, int u, int v, int w, int h) {
        return icon(icon, u, v, w, h, new IconStyle());
    }

    @Override
    public IProbeInfo icon(ResourceLocation icon, int u, int v, int w, int h, IIconStyle style) {
        children.add(new ElementIcon(icon, u, v, w, h, style));
        return this;
    }

    @Override
    public IProbeInfo text(String text) {
        children.add(new ElementText(text));
        return this;
    }

    @Override
    public IProbeInfo text(String text, ITextStyle style) {
        children.add(new ElementText(text));
        return this;
    }

    @Override
    public IProbeInfo itemLabel(ItemStack stack, ITextStyle style) {
        children.add(new ElementItemLabel(stack));
        return this;
    }

    @Override
    public IProbeInfo itemLabel(ItemStack stack) {
        children.add(new ElementItemLabel(stack));
        return this;
    }

    @Override
    public IProbeInfo entity(String entityName, IEntityStyle style) {
        children.add(new ElementEntity(entityName, style));
        return this;
    }

    @Override
    public IProbeInfo entity(String entityName) {
        return entity(entityName, new EntityStyle());
    }

    @Override
    public IProbeInfo entity(Entity entity, IEntityStyle style) {
        children.add(new ElementEntity(entity, style));
        return this;
    }

    @Override
    public IProbeInfo entity(Entity entity) {
        return entity(entity, new EntityStyle());
    }

    @Override
    public IProbeInfo item(ItemStack stack, IItemStyle style) {
        children.add(new ElementItemStack(stack, style));
        return this;
    }

    @Override
    public IProbeInfo item(ItemStack stack) {
        return item(stack, new ItemStyle());
    }

    @Override
    public IProbeInfo progress(int current, int max) {
        return progress(current, max, new ProgressStyle());
    }

    @Override
    public IProbeInfo progress(int current, int max, IProgressStyle style) {
        children.add(new ElementProgress(current, max, style));
        return this;
    }

    @Override
    public IProbeInfo progress(long current, long max) {
        return progress(current, max, new ProgressStyle());
    }

    @Override
    public IProbeInfo progress(long current, long max, IProgressStyle style) {
        children.add(new ElementProgress(current, max, style));
        return this;
    }

    @Override
    public IProbeInfo horizontal(ILayoutStyle style) {
        ElementHorizontal e = new ElementHorizontal(style.getBorderColor(), style.getSpacing(), style.getAlignment());
        children.add(e);
        return e;
    }

    @Override
    public IProbeInfo horizontal() {
        ElementHorizontal e = new ElementHorizontal(null, spacing, ElementAlignment.ALIGN_TOPLEFT);
        children.add(e);
        return e;
    }

    @Override
    public IProbeInfo vertical(ILayoutStyle style) {
        ElementVertical e = new ElementVertical(style.getBorderColor(), style.getSpacing(), style.getAlignment());
        children.add(e);
        return e;
    }

    @Override
    public IProbeInfo vertical() {
        ElementVertical e = new ElementVertical(null, ElementVertical.SPACING, ElementAlignment.ALIGN_TOPLEFT);
        children.add(e);
        return e;
    }

    @Override
    public IProbeInfo element(IElement element) {
        children.add(element);
        return this;
    }

    @Override
    public ILayoutStyle defaultLayoutStyle() {
        return new LayoutStyle();
    }

    @Override
    public IProgressStyle defaultProgressStyle() {
        return new ProgressStyle();
    }

    @Override
    public ITextStyle defaultTextStyle() {
        return new TextStyle();
    }

    @Override
    public IItemStyle defaultItemStyle() {
        return new ItemStyle();
    }

    @Override
    public IEntityStyle defaultEntityStyle() {
        return new EntityStyle();
    }

    @Override
    public IIconStyle defaultIconStyle() {
        return new IconStyle();
    }
}
