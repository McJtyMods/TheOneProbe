package baubles.api.cap;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

public class BaublesContainerProvider implements INBTSerializable<CompoundTag>, ICapabilityProvider {

	private final BaublesContainer container;

	public BaublesContainerProvider(BaublesContainer container) {
		this.container = container;
	}

	@Override
	public boolean hasCapability (Capability<?> capability, EnumFacing facing) {
		return capability == BaublesCapabilities.CAPABILITY_BAUBLES;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability (Capability<T> capability, EnumFacing facing) {
		if (capability == BaublesCapabilities.CAPABILITY_BAUBLES) return (T) this.container;
		return null;
	}

	@Override
	public CompoundTag serializeNBT () {
		return this.container.serializeNBT();
	}

	@Override
	public void deserializeNBT (CompoundTag nbt) {
		this.container.deserializeNBT(nbt);
	}
}
