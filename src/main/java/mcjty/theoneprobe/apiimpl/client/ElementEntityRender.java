package mcjty.theoneprobe.apiimpl.client;

import com.google.common.collect.Maps;
import com.mojang.datafixers.DataFixUtils;
import mcjty.theoneprobe.api.IEntityStyle;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public class ElementEntityRender {

    public static void renderPlayer(String entityName, Integer playerID, IEntityStyle style, int x, int y) {
        Entity entity = Minecraft.getInstance().world.getEntityByID(playerID);
        if (entity != null) {
            renderEntity(style, x, y, entity);
        }
    }

    public static void render(String entityName, CompoundNBT entityNBT, IEntityStyle style, int x, int y) {
        if (entityName != null && !entityName.isEmpty()) {
            Entity entity = null;
            if (entityNBT != null) {
                String fixed = fixEntityId(entityName);
                EntityType<?> value = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(fixed));
                if (value != null) {
                    try {
                        entity = value.create(Minecraft.getInstance().world, entityNBT, null, null, new BlockPos(0, 0, 0), SpawnReason.COMMAND, false, false);
                        entity.read(entityNBT);
                    } catch (Exception ignore) {
                        // This can crash due to a vanilla bug with foxes. Workaround here
                    }
                }
            } else {
                String fixed = fixEntityId(entityName);
                EntityType<?> value = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(fixed));
                if (value != null) {
                    try {
                        entity = value.create(Minecraft.getInstance().world);
                    } catch (Exception ignore) {
                        // This can crash due to a vanilla bug with foxes. Workaround here
                    }
                }
            }
            if (entity != null) {
                renderEntity(style, x, y, entity);
            }
        }
    }

    // @todo 1.13
//    private static final EntityId FIXER = new EntityId();
    // copied from vanilla until this is fixed in forge
    private static final Map OLD_TO_NEW_ID_MAP = (Map) DataFixUtils.make(Maps.newHashMap(), (p_209312_0_) -> {
        p_209312_0_.put("AreaEffectCloud", "minecraft:area_effect_cloud");
        p_209312_0_.put("ArmorStand", "minecraft:armor_stand");
        p_209312_0_.put("Arrow", "minecraft:arrow");
        p_209312_0_.put("Bat", "minecraft:bat");
        p_209312_0_.put("Blaze", "minecraft:blaze");
        p_209312_0_.put("Boat", "minecraft:boat");
        p_209312_0_.put("CaveSpider", "minecraft:cave_spider");
        p_209312_0_.put("Chicken", "minecraft:chicken");
        p_209312_0_.put("Cow", "minecraft:cow");
        p_209312_0_.put("Creeper", "minecraft:creeper");
        p_209312_0_.put("Donkey", "minecraft:donkey");
        p_209312_0_.put("DragonFireball", "minecraft:dragon_fireball");
        p_209312_0_.put("ElderGuardian", "minecraft:elder_guardian");
        p_209312_0_.put("EnderCrystal", "minecraft:ender_crystal");
        p_209312_0_.put("EnderDragon", "minecraft:ender_dragon");
        p_209312_0_.put("Enderman", "minecraft:enderman");
        p_209312_0_.put("Endermite", "minecraft:endermite");
        p_209312_0_.put("EyeOfEnderSignal", "minecraft:eye_of_ender_signal");
        p_209312_0_.put("FallingSand", "minecraft:falling_block");
        p_209312_0_.put("Fireball", "minecraft:fireball");
        p_209312_0_.put("FireworksRocketEntity", "minecraft:fireworks_rocket");
        p_209312_0_.put("Ghast", "minecraft:ghast");
        p_209312_0_.put("Giant", "minecraft:giant");
        p_209312_0_.put("Guardian", "minecraft:guardian");
        p_209312_0_.put("Horse", "minecraft:horse");
        p_209312_0_.put("Husk", "minecraft:husk");
        p_209312_0_.put("Item", "minecraft:item");
        p_209312_0_.put("ItemFrame", "minecraft:item_frame");
        p_209312_0_.put("LavaSlime", "minecraft:magma_cube");
        p_209312_0_.put("LeashKnot", "minecraft:leash_knot");
        p_209312_0_.put("MinecartChest", "minecraft:chest_minecart");
        p_209312_0_.put("MinecartCommandBlock", "minecraft:commandblock_minecart");
        p_209312_0_.put("MinecartFurnace", "minecraft:furnace_minecart");
        p_209312_0_.put("MinecartHopper", "minecraft:hopper_minecart");
        p_209312_0_.put("MinecartRideable", "minecraft:minecart");
        p_209312_0_.put("MinecartSpawner", "minecraft:spawner_minecart");
        p_209312_0_.put("MinecartTNT", "minecraft:tnt_minecart");
        p_209312_0_.put("Mule", "minecraft:mule");
        p_209312_0_.put("MushroomCow", "minecraft:mooshroom");
        p_209312_0_.put("Ozelot", "minecraft:ocelot");
        p_209312_0_.put("Painting", "minecraft:painting");
        p_209312_0_.put("Pig", "minecraft:pig");
        p_209312_0_.put("PigZombie", "minecraft:zombie_pigman");
        p_209312_0_.put("PolarBear", "minecraft:polar_bear");
        p_209312_0_.put("PrimedTnt", "minecraft:tnt");
        p_209312_0_.put("Rabbit", "minecraft:rabbit");
        p_209312_0_.put("Sheep", "minecraft:sheep");
        p_209312_0_.put("Shulker", "minecraft:shulker");
        p_209312_0_.put("ShulkerBullet", "minecraft:shulker_bullet");
        p_209312_0_.put("Silverfish", "minecraft:silverfish");
        p_209312_0_.put("Skeleton", "minecraft:skeleton");
        p_209312_0_.put("SkeletonHorse", "minecraft:skeleton_horse");
        p_209312_0_.put("Slime", "minecraft:slime");
        p_209312_0_.put("SmallFireball", "minecraft:small_fireball");
        p_209312_0_.put("SnowMan", "minecraft:snowman");
        p_209312_0_.put("Snowball", "minecraft:snowball");
        p_209312_0_.put("SpectralArrow", "minecraft:spectral_arrow");
        p_209312_0_.put("Spider", "minecraft:spider");
        p_209312_0_.put("Squid", "minecraft:squid");
        p_209312_0_.put("Stray", "minecraft:stray");
        p_209312_0_.put("ThrownEgg", "minecraft:egg");
        p_209312_0_.put("ThrownEnderpearl", "minecraft:ender_pearl");
        p_209312_0_.put("ThrownExpBottle", "minecraft:xp_bottle");
        p_209312_0_.put("ThrownPotion", "minecraft:potion");
        p_209312_0_.put("Villager", "minecraft:villager");
        p_209312_0_.put("VillagerGolem", "minecraft:villager_golem");
        p_209312_0_.put("Witch", "minecraft:witch");
        p_209312_0_.put("WitherBoss", "minecraft:wither");
        p_209312_0_.put("WitherSkeleton", "minecraft:wither_skeleton");
        p_209312_0_.put("WitherSkull", "minecraft:wither_skull");
        p_209312_0_.put("Wolf", "minecraft:wolf");
        p_209312_0_.put("XPOrb", "minecraft:xp_orb");
        p_209312_0_.put("Zombie", "minecraft:zombie");
        p_209312_0_.put("ZombieHorse", "minecraft:zombie_horse");
        p_209312_0_.put("ZombieVillager", "minecraft:zombie_villager");
    });



    /**
     * This method attempts to fix an old-style (1.10.2) entity Id and convert it to the
     * string representation of the new ResourceLocation.
     * This does not work for modded entities.
     * @param id an old-style entity id as used in 1.10
     * @return
     */
    public static String fixEntityId(String id) {
//        CompoundNBT nbt = new CompoundNBT();
//        nbt.setString("id", id);
//        nbt = FIXER.fixTagCompound(nbt);
//        return nbt.getString("id");
        if (OLD_TO_NEW_ID_MAP.containsKey(id)) {
            return (String) OLD_TO_NEW_ID_MAP.get(id);
        }
        return id;
    }

    private static void renderEntity(IEntityStyle style, int x, int y, Entity entity) {
        float height = entity.getHeight();
        height = (float) ((height - 1) * .7 + 1);
        float s = style.getScale() * ((style.getHeight() * 14.0f / 25) / height);

        RenderHelper.renderEntity(entity, x, y, s);
    }

}
