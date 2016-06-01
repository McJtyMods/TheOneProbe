package mcjty.theoneprobe.apiimpl.elements;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IEntityStyle;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.styles.EntityStyle;
import mcjty.theoneprobe.network.NetworkTools;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.lang.reflect.InvocationTargetException;

public class ElementEntity implements IElement {

    private final String entityName;
    private final NBTTagCompound entityNBT;
    private final IEntityStyle style;

    public ElementEntity(String entityName, IEntityStyle style) {
        this.entityName = entityName;
        this.entityNBT = null;
        this.style = style;
    }

    public ElementEntity(Entity entity, IEntityStyle style) {
        entityNBT = entity.serializeNBT();

        this.entityName = EntityList.getEntityString(entity);
        this.style = style;
    }

    public ElementEntity(ByteBuf buf) {
        entityName = NetworkTools.readString(buf);
        style = new EntityStyle()
                .width(buf.readInt())
                .height(buf.readInt())
                .scale(buf.readFloat());
        if (buf.readBoolean()) {
            entityNBT = NetworkTools.readNBT(buf);
        } else {
            entityNBT = null;
        }
    }

    @Override
    public void render(int x, int y) {
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

    @Override
    public int getWidth() {
        return style.getWidth();
    }

    @Override
    public int getHeight() {
        return style.getHeight();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NetworkTools.writeString(buf, entityName);
        buf.writeInt(style.getWidth());
        buf.writeInt(style.getHeight());
        buf.writeFloat(style.getScale());
        if (entityNBT != null) {
            buf.writeBoolean(true);
            NetworkTools.writeNBT(buf, entityNBT);
        } else {
            buf.writeBoolean(false);
        }
    }

    @Override
    public int getID() {
        return TheOneProbeImp.ELEMENT_ENTITY;
    }
}
