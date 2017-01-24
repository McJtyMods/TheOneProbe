package mcjty.theoneprobe.apiimpl.client;

import mcjty.lib.tools.MinecraftTools;
import mcjty.theoneprobe.api.IEntityStyle;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;

public class ElementEntityRender {

    public static void renderPlayer(String entityName, Integer playerID, IEntityStyle style, int x, int y) {
        Entity entity = MinecraftTools.getWorld(Minecraft.getMinecraft()).getEntityByID(playerID);
        if (entity != null) {
            renderEntity(style, x, y, entity);
        }
    }

    public static void render(String entityName, NBTTagCompound entityNBT, IEntityStyle style, int x, int y) {
        if (entityName != null && !entityName.isEmpty()) {
            Entity entity = null;
            if (entityNBT != null) {
                entity = EntityList.createEntityFromNBT(entityNBT, MinecraftTools.getWorld(Minecraft.getMinecraft()));
            } else {
//                int id = EntityList.getIDFromString(entityName);
//                Class<? extends Entity> clazz = EntityList.getClassFromID(id);
//                try {
//                    entity = clazz.getConstructor(World.class).newInstance(Minecraft.getMinecraft().theWorld);
//                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//                }
            }
            if (entity != null) {
                renderEntity(style, x, y, entity);
            }
        }
    }

    private static void renderEntity(IEntityStyle style, int x, int y, Entity entity) {
        float height = entity.height;
        height = (float) ((height - 1) * .7 + 1);
        float s = style.getScale() * ((style.getHeight() * 14.0f / 25) / height);

        RenderHelper.renderEntity(entity, x, y, s);
    }

}
