package mcjty.theoneprobe.apiimpl;

import mcjty.theoneprobe.api.IProbeConfig;

public class LazyProbeConfig implements IProbeConfig {

    private IProbeConfig original;
    private boolean dirty = false;

    public LazyProbeConfig(IProbeConfig original) {
        this.original = original;
    }

    private IProbeConfig realCopy() {
        if (!dirty) {
            dirty = true;
            original = new ProbeConfig()
                    .setRFMode(original.getRFMode())
                    .setTankMode(original.getTankMode())
                    .showModName(original.getShowModName())
                    .showChestContents(original.getShowChestContents())
                    .showChestContentsDetailed(original.getShowChestContentsDetailed())
                    .showCropPercentage(original.getShowCropPercentage())
                    .showHarvestLevel(original.getShowHarvestLevel())
                    .showCanBeHarvested(original.getShowCanBeHarvested())
                    .showMobHealth(original.getShowMobHealth())
                    .showMobGrowth(original.getShowMobGrowth())
                    .showMobPotionEffects(original.getShowMobPotionEffects())
                    .showRedstone(original.getShowRedstone())
                    .showLeverSetting(original.getShowLeverSetting())
                    .showTankSetting(original.getShowTankSetting())
                    .showBrewStandSetting(original.getShowBrewStandSetting())
                    .showMobSpawnerSetting(original.getShowMobSpawnerSetting())
                    .showAnimalOwnerSetting(original.getAnimalOwnerSetting())
                    .showHorseStatSetting(original.getHorseStatSetting())
					.showSilverfish(original.getShowSilverfish());
        }
        return original;
    }

    @Override
    public IProbeConfig setTankMode(int mode) {
        realCopy().setTankMode(mode);
        return this;
    }

    @Override
    public int getTankMode() {
        return original.getTankMode();
    }

    @Override
    public IProbeConfig showHorseStatSetting(ConfigMode mode) {
        realCopy().showHorseStatSetting(mode);
        return this;
    }

    @Override
    public ConfigMode getHorseStatSetting() {
        return original.getHorseStatSetting();
    }

    @Override
    public IProbeConfig showAnimalOwnerSetting(ConfigMode mode) {
        realCopy().showAnimalOwnerSetting(mode);
        return this;
    }

    @Override
    public ConfigMode getAnimalOwnerSetting() {
        return original.getAnimalOwnerSetting();
    }

    @Override
    public IProbeConfig showMobSpawnerSetting(ConfigMode mode) {
        realCopy().showMobSpawnerSetting(mode);
        return this;
    }

    @Override
    public ConfigMode getShowMobSpawnerSetting() {
        return original.getShowMobSpawnerSetting();
    }

    @Override
    public IProbeConfig showBrewStandSetting(ConfigMode mode) {
        realCopy().showBrewStandSetting(mode);
        return this;
    }

    @Override
    public ConfigMode getShowBrewStandSetting() {
        return original.getShowBrewStandSetting();
    }

    @Override
    public IProbeConfig showTankSetting(ConfigMode mode) {
        realCopy().showTankSetting(mode);
        return this;
    }

    @Override
    public ConfigMode getShowTankSetting() {
        return original.getShowTankSetting();
    }

    @Override
    public IProbeConfig setRFMode(int showRF) {
        realCopy().setRFMode(showRF);
        return this;
    }

    @Override
    public int getRFMode() {
        return original.getRFMode();
    }

    @Override
    public IProbeConfig showModName(ConfigMode mode) {
        realCopy().showModName(mode);
        return this;
    }

    @Override
    public ConfigMode getShowModName() {
        return original.getShowModName();
    }

    @Override
    public IProbeConfig showHarvestLevel(ConfigMode mode) {
        realCopy().showHarvestLevel(mode);
        return this;
    }

    @Override
    public ConfigMode getShowHarvestLevel() {
        return original.getShowHarvestLevel();
    }

    @Override
    public IProbeConfig showCanBeHarvested(ConfigMode mode) {
        realCopy().showCanBeHarvested(mode);
        return this;
    }

    @Override
    public ConfigMode getShowCanBeHarvested() {
        return original.getShowCanBeHarvested();
    }

    @Override
    public IProbeConfig showCropPercentage(ConfigMode mode) {
        realCopy().showCropPercentage(mode);
        return this;
    }

    @Override
    public ConfigMode getShowCropPercentage() {
        return original.getShowCropPercentage();
    }

    @Override
    public IProbeConfig showChestContents(ConfigMode mode) {
        realCopy().showChestContents(mode);
        return this;
    }

    @Override
    public ConfigMode getShowChestContents() {
        return original.getShowChestContents();
    }

    @Override
    public IProbeConfig showChestContentsDetailed(ConfigMode mode) {
        realCopy().showChestContentsDetailed(mode);
        return this;
    }

    @Override
    public ConfigMode getShowChestContentsDetailed() {
        return original.getShowChestContentsDetailed();
    }

    @Override
    public IProbeConfig showRedstone(ConfigMode mode) {
        realCopy().showRedstone(mode);
        return this;
    }

    @Override
    public ConfigMode getShowRedstone() {
        return original.getShowRedstone();
    }

    @Override
    public IProbeConfig showMobHealth(ConfigMode mode) {
        realCopy().showMobHealth(mode);
        return this;
    }

    @Override
    public ConfigMode getShowMobHealth() {
        return original.getShowMobHealth();
    }

    @Override
    public IProbeConfig showMobGrowth(ConfigMode mode) {
        realCopy().showMobGrowth(mode);
        return this;
    }

    @Override
    public ConfigMode getShowMobGrowth() {
        return original.getShowMobGrowth();
    }

    @Override
    public IProbeConfig showMobPotionEffects(ConfigMode mode) {
        realCopy().showMobPotionEffects(mode);
        return this;
    }

    @Override
    public ConfigMode getShowMobPotionEffects() {
        return original.getShowMobPotionEffects();
    }

    @Override
    public IProbeConfig showLeverSetting(ConfigMode mode) {
        realCopy().showLeverSetting(mode);
        return this;
    }

    @Override
    public ConfigMode getShowLeverSetting() {
        return original.getShowLeverSetting();
    }
	
	@Override
	public ConfigMode getShowSilverfish() {
		return original.getShowSilverfish();
	}
	
	@Override
	public IProbeConfig showSilverfish(ConfigMode mode){
		realCopy().showSilverfish(mode);
		return this;
	}
}
