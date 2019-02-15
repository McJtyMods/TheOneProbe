package mcjty.theoneprobe.apiimpl.client;

import mcjty.theoneprobe.api.IEntityStyle;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.fixes.EntityId;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ForgeRegistries;

public class ElementEntityRender {

    public static void renderPlayer(String entityName, Integer playerID, IEntityStyle style, int x, int y) {
        Entity entity = Minecraft.getInstance().world.getEntityByID(playerID);
        if (entity != null) {
            renderEntity(style, x, y, entity);
        }
    }

    public static void render(String entityName, NBTTagCompound entityNBT, IEntityStyle style, int x, int y) {
        if (entityName != null && !entityName.isEmpty()) {
            Entity entity = null;
            if (entityNBT != null) {
                String fixed = fixEntityId(entityName);
                EntityType<?> value = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(fixed));
                if (value != null) {
                    entity = value.makeEntity(Minecraft.getInstance().world, entityNBT, null, null, new BlockPos(0, 0, 0), false, false);
                }
//                entity = EntityList.createEntityFromNBT(entityNBT, Minecraft.getInstance().world);
            } else {
                String fixed = fixEntityId(entityName);
                EntityType<?> value = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(fixed));
                if (value != null) {
                    entity = value.create(Minecraft.getInstance().world);
                }
            }
            if (entity != null) {
                renderEntity(style, x, y, entity);
            }
        }
    }

    // @todo 1.13
//    private static final EntityId FIXER = new EntityId();

    /**
     * This method attempts to fix an old-style (1.10.2) entity Id and convert it to the
     * string representation of the new ResourceLocation. The 1.10 version of this function will just return
     * the given id
     * This does not work for modded entities.
     * @param id an old-style entity id as used in 1.10
     * @return
     */
    public static String fixEntityId(String id) {
        // @todo 1.13
//        NBTTagCompound nbt = new NBTTagCompound();
//        nbt.setString("id", id);
//        nbt = FIXER.fixTagCompound(nbt);
//        return nbt.getString("id");
        return id;
    }



    private static void renderEntity(IEntityStyle style, int x, int y, Entity entity) {
        float height = entity.height;
        height = (float) ((height - 1) * .7 + 1);
        float s = style.getScale() * ((style.getHeight() * 14.0f / 25) / height);

        RenderHelper.renderEntity(entity, x, y, s);
    }

}
