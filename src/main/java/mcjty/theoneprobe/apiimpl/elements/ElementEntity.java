package mcjty.theoneprobe.apiimpl.elements;

import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IEntityStyle;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.client.ElementEntityRender;
import mcjty.theoneprobe.apiimpl.styles.EntityStyle;
import mcjty.theoneprobe.network.NetworkTools;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;

public class ElementEntity implements IElement {

    private final String entityName;
    private final Integer playerID;
    private final CompoundTag entityNBT;
    private final IEntityStyle style;

    public ElementEntity(String entityName, IEntityStyle style) {
        this.entityName = entityName;
        this.entityNBT = null;
        this.style = style;
        this.playerID = null;
    }

    public ElementEntity(Entity entity, IEntityStyle style) {
        if (entity instanceof Player player) {
            entityNBT = null;
            playerID = player.getId();
        } else {
            entityNBT = new CompoundTag();
            entity.saveWithoutId(entityNBT);
//            entityNBT = entity.serializeNBT();
            playerID = null;
        }
        ResourceLocation registryName = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        if (registryName == null) {
            registryName = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        }
        this.entityName = registryName == null ? "<null>" : registryName.toString();
//        this.entityName = EntityList.getEntityString(entity);
        this.style = style;
    }

    public ElementEntity(FriendlyByteBuf buf) {
        entityName = buf.readUtf();
        style = new EntityStyle()
                .width(buf.readInt())
                .height(buf.readInt())
                .scale(buf.readFloat());
        entityNBT = buf.readNbt();
        playerID = buf.readNullable(FriendlyByteBuf::readInt);
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y) {
        if (playerID != null) {
            ElementEntityRender.renderPlayer(entityName, playerID, style, graphics, x, y);
        } else {
            ElementEntityRender.render(entityName, entityNBT, style, graphics, x, y);
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
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(entityName);
        buf.writeInt(style.getWidth());
        buf.writeInt(style.getHeight());
        buf.writeFloat(style.getScale());
        buf.writeNbt(entityNBT);
        buf.writeNullable(playerID, FriendlyByteBuf::writeInt);
    }

    @Override
    public ResourceLocation getID() {
        return TheOneProbeImp.ELEMENT_ENTITY;
    }
}
