package mcjty.theoneprobe.api;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;

public final class TankReference {
	int maxCapacity;
	int stored;
	FluidStack[] fluids;
	
	public TankReference(int maxCapacity, int stored, FluidStack... fluids) {
		this.maxCapacity = maxCapacity;
		this.stored = stored;
		this.fluids = fluids;
	}
	
	///Simple Self Simulated Tank or just a fluid display
	public static TankReference createSimple(int maxCapacity, FluidStack fluid) {
		return new TankReference(maxCapacity, fluid.getAmount(), fluid);
	}
	
	///Simple Tank like FluidTank
	public static TankReference createTank(IFluidTank tank) {
		return new TankReference(tank.getCapacity(), tank.getFluidAmount(), tank.getFluid());
	}
	
	///Any Fluid Handler, but Squashes all the fluids into 1 Progress Bar
	public static TankReference createHandler(IFluidHandler handler) {
		int maxCapacity = 0;
		int stored = 0;
		FluidStack[] fluids = new FluidStack[handler.getTanks()];
		for(int i = 0;i < fluids.length;i++) {
			maxCapacity += handler.getTankCapacity(i);
			FluidStack fluid = handler.getFluidInTank(i);
			fluids[i] = fluid;
			stored += fluid.getAmount();
		}
		return new TankReference(maxCapacity, stored, fluids);
	}
	
	///Any Fluid Handler but splits each internal Tank into its own Progress Bar
	public static TankReference[] createSplitHandler(IFluidHandler handler) {
		TankReference[] references = new TankReference[handler.getTanks()];
		for(int i = 0;i < references.length;i++) {
			FluidStack fluid = handler.getFluidInTank(i);
			references[i] = new TankReference(handler.getTankCapacity(i), fluid.getAmount(), fluid);
		}
		return references;
	}
}
