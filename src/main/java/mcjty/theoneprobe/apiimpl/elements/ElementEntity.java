package mcjty.theoneprobe.apiimpl.elements;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IEntityStyle;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.client.ElementEntityRender;
import mcjty.theoneprobe.apiimpl.styles.EntityStyle;
import mcjty.theoneprobe.network.NetworkTools;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class ElementEntity implements IElement {

    private final String entityName;
    private final Integer playerID;
    private final NBTTagCompound entityNBT;
    private final IEntityStyle style;

    public ElementEntity(String entityName, IEntityStyle style) {
        this.entityName = entityName;
        this.entityNBT = null;
        this.style = style;
        this.playerID = null;
    }

    public ElementEntity(Entity entity, IEntityStyle style) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            entityNBT = null;
            playerID = player.getEntityId();
        } else {
            entityNBT = entity.serializeNBT();
            playerID = null;
        }
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
        if (buf.readBoolean()) {
            playerID = buf.readInt();
        } else {
            playerID = null;
        }
    }

    @Override
    public void render(int x, int y) {
        if (playerID != null) {
            ElementEntityRender.renderPlayer(entityName, playerID, style, x, y);
        } else {
            ElementEntityRender.render(entityName, entityNBT, style, x, y);
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
        if (playerID != null) {
            buf.writeBoolean(true);
            buf.writeInt(playerID);
        } else {
            buf.writeBoolean(false);
        }
    }

    @Override
    public int getID() {
        return TheOneProbeImp.ELEMENT_ENTITY;
    }
}
