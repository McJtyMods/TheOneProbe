package mcjty.theoneprobe.apiimpl.elements;

import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IProgressStyle;
import mcjty.theoneprobe.api.NumberFormat;
import mcjty.theoneprobe.api.TankReference;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.client.ElementProgressRender;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

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
	
	public ElementTank(RegistryFriendlyByteBuf buffer) {
		tank = new TankReference(buffer);
        style = new ProgressStyle()
                .width(buffer.readInt())
                .height(buffer.readInt())
				.prefix(ComponentSerialization.STREAM_CODEC.decode(buffer))
				.suffix(ComponentSerialization.STREAM_CODEC.decode(buffer))
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
	public void render(GuiGraphics graphics, int x, int y) {
		ElementProgressRender.renderTank(graphics, x, y, getWidth(), getHeight(), style, tank);
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
	public void toBytes(RegistryFriendlyByteBuf buf) {
		tank.toBytes(buf);
        buf.writeInt(style.getWidth());
        buf.writeInt(style.getHeight());
		ComponentSerialization.STREAM_CODEC.encode(buf, style.getPrefixComp());
		ComponentSerialization.STREAM_CODEC.encode(buf, style.getSuffixComp());
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
	public ResourceLocation getID() {
		return TheOneProbeImp.ELEMENT_TANK;
	}
}