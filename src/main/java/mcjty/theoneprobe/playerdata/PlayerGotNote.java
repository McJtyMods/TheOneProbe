package mcjty.theoneprobe.playerdata;

import net.minecraft.nbt.NBTTagCompound;

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


    public void saveNBTData(NBTTagCompound compound) {
        compound.setBoolean("gotNote", playerGotNote);
    }

    public void loadNBTData(NBTTagCompound compound) {
        playerGotNote = compound.getBoolean("gotNote");
    }
}
