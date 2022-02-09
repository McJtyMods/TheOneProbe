//package mcjty.theoneprobe.playerdata;
//
//import mcjty.theoneprobe.lib.LazyOptional;
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraftforge.common.capabilities.Capability;
//import net.minecraftforge.common.capabilities.ICapabilityProvider;
//import net.minecraftforge.common.util.INBTSerializable;
//import net.minecraftforge.common.util.LazyOptional;
//
//import javax.annotation.Nonnull;
//import javax.annotation.Nullable;
//
//public class PropertiesDispatcher {
//
//    private PlayerGotNote playerGotNote = null;
//    private LazyOptional<PlayerGotNote> opt = LazyOptional.of(this::createPlayerGotNote);
//
//    @Nonnull
//    private PlayerGotNote createPlayerGotNote() {
//        if (playerGotNote == null) {
//            playerGotNote = new PlayerGotNote();
//        }
//        return playerGotNote;
//    }
//
//    @Nonnull
//    @Override
//    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
//        if (cap == PlayerProperties.PLAYER_GOT_NOTE) {
//            return opt.cast();
//        }
//        return LazyOptional.empty();
//    }
//
//    @Nonnull
//    @Override
//    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
//        return getCapability(cap);
//    }
//
//    public CompoundTag serializeNBT() {
//        CompoundTag nbt = new CompoundTag();
//        createPlayerGotNote().saveNBTData(nbt);
//        return nbt;
//    }
//
//    public void deserializeNBT(CompoundTag nbt) {
//        createPlayerGotNote().loadNBTData(nbt);
//    }
//}
