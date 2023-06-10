package mcjty.theoneprobe.apiimpl.elements;

import mcjty.theoneprobe.api.Color;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.client.ElementPaddingRender;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class ElementPadding implements IElement {
	private final int width;
	private final int height;
	private int color = -1;
	
	public ElementPadding(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public ElementPadding(FriendlyByteBuf buf) {
		width = buf.readInt();
		height = buf.readInt();
		color = buf.readInt();
	}
	
	public ElementPadding setDebugColor(Color color) {
		this.color = color.getRGB();
		return this;
	}
	
	/// Helper method that allows you to visualize the Padding Element
	public ElementPadding setDebugColor(int color) {
		this.color = color;
		return this;
	}
	
	@Override
	public void render(GuiGraphics stack, int x, int y) {
		if (color != -1) {
			ElementPaddingRender.renderPadding(stack, x, y, getWidth(), getHeight(), color);
		}
	}

	@Override
	public int getWidth() {
		return width;
	}
	
	@Override
	public int getHeight() {
		return height;
	}
	
	@Override
	public void toBytes(FriendlyByteBuf buf) {
		buf.writeInt(width).writeInt(height).writeInt(color);
	}
	
	@Override
	public ResourceLocation getID() {
		return TheOneProbeImp.ELEMENT_PADDING;
	}
}