package mcjty.theoneprobe.apiimpl.elements;

import com.mojang.blaze3d.matrix.MatrixStack;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IEntityStyle;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import mcjty.theoneprobe.apiimpl.client.ElementEntityRender;
import mcjty.theoneprobe.apiimpl.styles.EntityStyle;
import mcjty.theoneprobe.network.NetworkTools;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class ElementEntity implements IElement {

    private final String entityName;
    private final Integer playerID;
    private final CompoundNBT entityNBT;
    private final IEntityStyle style;

    public ElementEntity(String entityName, IEntityStyle style) {
        this.entityName = entityName;
        this.entityNBT = null;
        this.style = style;
        this.playerID = null;
    }

    public ElementEntity(Entity entity, IEntityStyle style) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            entityNBT = null;
            playerID = player.getId();
        } else {
            entityNBT = new CompoundNBT();
            entity.saveWithoutId(entityNBT);
//            entityNBT = entity.serializeNBT();
            playerID = null;
        }
        ResourceLocation registryName = entity.getType().getRegistryName();
        if (registryName == null) {
            registryName = ForgeRegistries.ENTITIES.getKey(entity.getType());
        }
        this.entityName = registryName.toString();
//        this.entityName = EntityList.getEntityString(entity);
        this.style = style;
    }

    public ElementEntity(PacketBuffer buf) {
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
    public void render(MatrixStack matrixStack, int x, int y) {
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
    public void toBytes(PacketBuffer buf) {
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
    public int getID() {
        return TheOneProbeImp.ELEMENT_ENTITY;
    }
}
