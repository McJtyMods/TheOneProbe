package mcjty.theoneprobe.apiimpl.elements;

import mcjty.theoneprobe.api.Color;

import com.mojang.blaze3d.matrix.MatrixStack;

import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ElementPadding implements IElement
{	
	final int width;
	final int height;
	int color = -1;
	
	public ElementPadding(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public ElementPadding(PacketBuffer buf) {
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
	@OnlyIn(Dist.CLIENT) //Eh i don't know where to put this? Could you decide where this impl goes? Temp until you found a solution
	public void render(MatrixStack stack, int x, int y) {
		if(color != -1) {
			Screen.fill(stack, x, y, x + getWidth(), y + getHeight(), color);
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
	public void toBytes(PacketBuffer buf) {
		buf.writeInt(width).writeInt(height).writeInt(color);
	}
	
	@Override
	public int getID() {
		return TheOneProbeImp.ELEMENT_PADDING;
	}
}