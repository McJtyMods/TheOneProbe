package mcjty.theoneprobe.apiimpl.elements;

import com.mojang.blaze3d.vertex.PoseStack;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IEntityStyle;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.client.ElementEntityRender;
import mcjty.theoneprobe.apiimpl.styles.EntityStyle;
import mcjty.theoneprobe.network.NetworkTools;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.registries.ForgeRegistries;

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
        ResourceLocation registryName = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
        if (registryName == null) {
            registryName = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
        }
        this.entityName = registryName == null ? "<null>" : registryName.toString();
//        this.entityName = EntityList.getEntityString(entity);
        this.style = style;
    }

    public ElementEntity(FriendlyByteBuf buf) {
        entityName = NetworkTools.readString(buf);
        style = new EntityStyle()
                .width(buf.readInt())
                .height(buf.readInt())
                .scale(buf.readFloat());
        entityNBT = buf.readNbt();
        if (buf.readBoolean()) {
            playerID = buf.readInt();
        } else {
            playerID = null;
        }
    }

    @Override
    public void render(PoseStack matrixStack, int x, int y) {
        if (playerID != null) {
            ElementEntityRender.renderPlayer(entityName, playerID, style, matrixStack, x, y);
        } else {
            ElementEntityRender.render(entityName, entityNBT, style, matrixStack, x, y);
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
        NetworkTools.writeString(buf, entityName);
        buf.writeInt(style.getWidth());
        buf.writeInt(style.getHeight());
        buf.writeFloat(style.getScale());
        buf.writeNbt(entityNBT);
        if (playerID != null) {
            buf.writeBoolean(true);
            buf.writeInt(playerID);
        } else {
            buf.writeBoolean(false);
        }
    }

    @Override
    public ResourceLocation getID() {
        return TheOneProbeImp.ELEMENT_ENTITY;
    }
}
