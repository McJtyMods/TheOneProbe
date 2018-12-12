package mcjty.theoneprobe.apiimpl.client;

import mcjty.theoneprobe.api.IEntityStyle;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;

public class ElementEntityRender {

    public static void renderPlayer(String entityName, Integer playerID, IEntityStyle style, int x, int y) {
        Entity entity = MinecraftClient.getInstance().world.getEntityById(playerID);
        if (entity != null) {
            renderEntity(style, x, y, entity);
        }
    }

    public static void render(String entityName, CompoundTag entityNBT, IEntityStyle style, int x, int y) {
        if (entityName != null && !entityName.isEmpty()) {
            Entity entity = null;
            // @todo fabric
//            if (entityNBT != null) {
//                entity = EntityList.createEntityFromNBT(entityNBT, MinecraftClient.getInstance().world);
//            } else {
//                String fixed = fixEntityId(entityName);
//                EntityEntry value = ForgeRegistries.ENTITIES.getValue(new Identifier(fixed));
//                if (value != null) {
//                    entity = value.newInstance(MinecraftClient.getInstance().world);
//                }
//            }
            if (entity != null) {
                renderEntity(style, x, y, entity);
            }
        }
    }

    // @todo fabric
//    private static final EntityId FIXER = new EntityId();

    /**
     * This method attempts to fix an old-style (1.10.2) entity Id and convert it to the
     * string representation of the new Identifier. The 1.10 version of this function will just return
     * the given id
     * This does not work for modded entities.
     * @param id an old-style entity id as used in 1.10
     * @return
     */
    public static String fixEntityId(String id) {
        // @todo fabric
//        CompoundTag nbt = new CompoundTag();
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
