package mcjty.theoneprobe.api;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import mcjty.theoneprobe.api.FluidStack;


public final class TankReference {
    private final int capacity;
    private final int stored;
    private final FluidStack[] fluids;

    public TankReference(int capacity, int stored, FluidStack... fluids) {
        this.capacity = capacity;
        this.stored = stored;
        this.fluids = fluids;
    }

    public TankReference(RegistryFriendlyByteBuf buffer) {
        capacity = buffer.readInt();
        stored = buffer.readInt();
        fluids = new FluidStack[buffer.readInt()];
        for (int i = 0; i < fluids.length; i++) {
            fluids[i] = FluidStack.OPTIONAL_STREAM_CODEC.decode(buffer);
        }
    }

    public int getCapacity() {
        return capacity;
    }

    public int getStored() {
        return stored;
    }

    public FluidStack[] getFluids() {
        return fluids;
    }

    /// Simple Self Simulated Tank or just a fluid display
    public static TankReference createSimple(int capacity, FluidStack fluid) {
        return new TankReference(capacity, fluid.getAmount(), fluid);
    }

    /** Combine all storage views into one bar. Fabric amounts are converted to mB. */
    public static TankReference createHandler(Storage<FluidVariant> handler) {
        int capacity = 0;
        int stored = 0;
        List<FluidStack> fluids = new ArrayList<>();
        for (var view : handler) {
            int amount = toMillibuckets(view.getAmount());
            capacity = (int) Math.min(Integer.MAX_VALUE, (long) capacity + toMillibuckets(view.getCapacity()));
            stored = (int) Math.min(Integer.MAX_VALUE, (long) stored + amount);
            fluids.add(new FluidStack(view.getResource(), amount));
        }
        return new TankReference(capacity, stored, fluids.toArray(FluidStack[]::new));
    }

    public static TankReference[] createSplitHandler(Storage<FluidVariant> handler) {
        List<TankReference> tanks = new ArrayList<>();
        for (var view : handler) {
            int amount = toMillibuckets(view.getAmount());
            tanks.add(new TankReference(toMillibuckets(view.getCapacity()), amount, new FluidStack(view.getResource(), amount)));
        }
        return tanks.toArray(TankReference[]::new);
    }

    public static int toMillibuckets(long droplets) {
        return (int) Math.min(Integer.MAX_VALUE, droplets / 81);
    }

    public void toBytes(RegistryFriendlyByteBuf buffer) {
        buffer.writeInt(capacity);
        buffer.writeInt(stored);
        buffer.writeInt(fluids.length);
		for (FluidStack fluid : fluids) {
            FluidStack.OPTIONAL_STREAM_CODEC.encode(buffer, fluid);
		}
    }
}
