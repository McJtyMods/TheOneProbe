package mcjty.theoneprobe.apiimpl.client;

import mcjty.theoneprobe.api.IEntityStyle;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.lang.reflect.InvocationTargetException;

public class ElementEntityRender {

    public static void render(String entityName, NBTTagCompound entityNBT, IEntityStyle style, int x, int y) {
        if (entityName != null && !entityName.isEmpty()) {
            Entity entity = null;
            if (entityNBT != null) {
                entity = EntityList.createEntityFromNBT(entityNBT, Minecraft.getMinecraft().theWorld);
            } else {
                int id = EntityList.getIDFromString(entityName);
                Class<? extends Entity> clazz = EntityList.getClassFromID(id);
                try {
                    entity = clazz.getConstructor(World.class).newInstance(Minecraft.getMinecraft().theWorld);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                }
            }
            if (entity != null) {
                float height = entity.height;
                height = (float) ((height - 1) * .7 + 1);
                float s = style.getScale() * ((style.getHeight() * 14.0f / 25) / height);

                RenderHelper.renderEntity(entity, x, y, s);
            }
        }
    }

}
