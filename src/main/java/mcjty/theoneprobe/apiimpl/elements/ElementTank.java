package mcjty.theoneprobe.apiimpl.elements;

import com.mojang.blaze3d.matrix.MatrixStack;

import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IProgressStyle;
import mcjty.theoneprobe.api.TankReference;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;
import net.minecraft.network.PacketBuffer;

public class ElementTank implements IElement {
	public static int ELEMENT_ID;
	TankReference reference;
	IProgressStyle style;
	
	public ElementTank(TankReference reference) {
		this(reference, new ProgressStyle());
	}
	
	public ElementTank(TankReference reference, IProgressStyle style) {
		this.reference = reference;
		this.style = style;
	}
	
	@Override
	public void render(MatrixStack matrixStack, int x, int y) {
		
	}
	
	@Override
	public int getWidth() {
		return 0;
	}
	
	@Override
	public int getHeight() {
		return 0;
	}
	
	@Override
	public void toBytes(PacketBuffer buf) {
		
	}
	
	@Override
	public int getID() {
		return 0;
	}
}