package mcjty.theoneprobe.apiimpl.elements;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IEntityStyle;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.client.ElementEntityRender;
import mcjty.theoneprobe.apiimpl.styles.EntityStyle;
import mcjty.theoneprobe.network.NetworkTools;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ElementEntity implements IElement {

    private final Identifier entityIdentifier;
    private final Integer playerID;
    private final CompoundTag entityNBT;
    private final IEntityStyle style;

    public ElementEntity(Identifier entityIdentifier, IEntityStyle style) {
        this.entityIdentifier = entityIdentifier;
        this.entityNBT = null;
        this.style = style;
        this.playerID = null;
    }

    public ElementEntity(Entity entity, IEntityStyle style) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            entityNBT = null;
            playerID = player.getEntityId();
            entityIdentifier = null;
        } else {
            entityNBT = new CompoundTag();
            entity.saveSelfToTag(entityNBT);
            playerID = null;
            this.entityIdentifier = Registry.ENTITY_TYPE.getId(entity.getType());
        }
        this.style = style;
    }

    public ElementEntity(ByteBuf buf) {
        String id = NetworkTools.readString(buf);
        if (id == null) {
            entityIdentifier = null;
        } else {
            entityIdentifier = new Identifier(id);
        }

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
            ElementEntityRender.renderPlayer(playerID, style, x, y);
        } else {
            ElementEntityRender.render(entityIdentifier, entityNBT, style, x, y);
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
        NetworkTools.writeString(buf, entityIdentifier == null ? null : entityIdentifier.toString());
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
