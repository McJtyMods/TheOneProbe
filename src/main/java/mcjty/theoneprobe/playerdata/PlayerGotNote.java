package mcjty.theoneprobe.playerdata;


import net.minecraft.nbt.CompoundNBT;

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


    public void saveNBTData(CompoundNBT compound) {
        compound.putBoolean("gotNote", playerGotNote);
    }

    public void loadNBTData(CompoundNBT compound) {
        playerGotNote = compound.getBoolean("gotNote");
    }
}
