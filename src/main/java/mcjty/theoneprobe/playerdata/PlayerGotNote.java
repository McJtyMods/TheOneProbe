package mcjty.theoneprobe.playerdata;


import net.minecraft.nbt.CompoundTag;

public class PlayerGotNote {

    private boolean playerGotNote = false;

    public PlayerGotNote() {
    }

    public boolean isPlayerGotNote() {
        return playerGotNote;
    }

    public void setPlayerGotNote(boolean playerGotNote) {
        this.playerGotNote = playerGotNote;
    }

    public void copyFrom(PlayerGotNote source) {
        playerGotNote = source.playerGotNote;
    }


    public void saveNBTData(CompoundTag compound) {
        compound.putBoolean("gotNote", playerGotNote);
    }

    public void loadNBTData(CompoundTag compound) {
        playerGotNote = compound.getBoolean("gotNote");
    }
}
