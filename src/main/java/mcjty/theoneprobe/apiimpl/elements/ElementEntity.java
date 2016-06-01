package mcjty.theoneprobe.apiimpl.elements;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.network.NetworkTools;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.world.World;

import java.lang.reflect.InvocationTargetException;

public class ElementEntity implements IElement {

    private final String entityName;

    public ElementEntity(String entityName) {
        this.entityName = entityName;
    }

    public ElementEntity(ByteBuf buf) {
        entityName = NetworkTools.readString(buf);
    }

    @Override
    public void render(int x, int y) {
        if (entityName != null && !entityName.isEmpty()) {
            int id = EntityList.getIDFromString(entityName);
            Class<? extends Entity> clazz = EntityList.getClassFromID(id);
            Entity entity = null;
            try {
                entity = clazz.getConstructor(World.class).newInstance(Minecraft.getMinecraft().theWorld);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            }
            if (entity != null) {
                float height = entity.height;
                if (height > 3) {
                    height *= .7f;
                }
                RenderHelper.renderEntity(entity, x, y, 13 / height);
            }
        }
    }

    @Override
    public int getWidth() {
        return 25;
    }

    @Override
    public int getHeight() {
        return 25;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NetworkTools.writeString(buf, entityName);
    }

    @Override
    public int getID() {
        return TheOneProbeImp.ELEMENT_ENTITY;
    }
}
