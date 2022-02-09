package mcjty.theoneprobe.mixin.common;

import mcjty.theoneprobe.lib.PlayerGotNoteExtension;
import mcjty.theoneprobe.playerdata.PlayerGotNote;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nonnull;

@Mixin(Player.class)
public class PlayerMixin implements PlayerGotNoteExtension {
    @Unique
    private PlayerGotNote playerGotNote = null;

    @Unique
    @Nonnull
    private PlayerGotNote createPlayerGotNote() {
        if (playerGotNote == null) {
            playerGotNote = new PlayerGotNote();
        }
        return playerGotNote;
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void theoneprobe$saveData(CompoundTag compound, CallbackInfo ci) {
        createPlayerGotNote().saveNBTData(compound);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void theoneprobe$readData(CompoundTag compound, CallbackInfo ci) {
        createPlayerGotNote().loadNBTData(compound);
    }

    @Unique
    @Override
    public PlayerGotNote getPlayerGotNote() {
        return createPlayerGotNote();
    }
}
