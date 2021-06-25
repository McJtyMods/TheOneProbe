package mcjty.theoneprobe.apiimpl.elements;

import com.mojang.blaze3d.matrix.MatrixStack;

import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IProgressStyle;
import mcjty.theoneprobe.api.NumberFormat;
import mcjty.theoneprobe.api.TankReference;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.client.ElementProgressRender;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;
import net.minecraft.network.PacketBuffer;

public class ElementTank implements IElement {
	private final TankReference tank;
	private final IProgressStyle style;
	
	public ElementTank(TankReference tank) {
		this(tank, new ProgressStyle());
	}
	
	public ElementTank(TankReference tank, IProgressStyle style) {
		this.tank = tank;
		this.style = style;
	}
	
	public ElementTank(PacketBuffer buffer) {
		tank = new TankReference(buffer);
        style = new ProgressStyle()
                .width(buffer.readInt())
                .height(buffer.readInt())
                .prefix(buffer.readComponent())
                .suffix(buffer.readComponent())
                .borderColor(buffer.readInt())
                .filledColor(buffer.readInt())
                .alternateFilledColor(buffer.readInt())
                .backgroundColor(buffer.readInt())
                .showText(buffer.readBoolean())
                .numberFormat(NumberFormat.values()[buffer.readByte()])
                .lifeBar(buffer.readBoolean())
                .armorBar(buffer.readBoolean())
                .alignment(buffer.readEnum(ElementAlignment.class));
	}
	
	public IProgressStyle getStyle() {
		return style;
	}
	
	@Override
	public void render(MatrixStack matrixStack, int x, int y) {
		ElementProgressRender.renderTank(matrixStack, x, y, getWidth(), getHeight(), style, tank);
	}
	
	@Override
	public int getWidth() {
		return style.getWidth();
	}
	
	@Override
	public int getHeight() {
		return style.getHeight();
	}
	
	@Override
	public void toBytes(PacketBuffer buf) {
		tank.toBytes(buf);
        buf.writeInt(style.getWidth());
        buf.writeInt(style.getHeight());
        buf.writeComponent(style.getPrefixComp());
        buf.writeComponent(style.getSuffixComp());
        buf.writeInt(style.getBorderColor());
        buf.writeInt(style.getFilledColor());
        buf.writeInt(style.getAlternatefilledColor());
        buf.writeInt(style.getBackgroundColor());
        buf.writeBoolean(style.isShowText());
        buf.writeByte(style.getNumberFormat().ordinal());
        buf.writeBoolean(style.isLifeBar());
        buf.writeBoolean(style.isArmorBar());
        buf.writeEnum(style.getAlignment());
	}
	
	@Override
	public int getID() {
		return TheOneProbeImp.ELEMENT_TANK;
	}
}