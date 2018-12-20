package mcjty.theoneprobe.apiimpl.client;

import mcjty.theoneprobe.api.IEntityStyle;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ElementEntityRender {

    public static void renderPlayer(Integer playerID, IEntityStyle style, int x, int y) {
        Entity entity = MinecraftClient.getInstance().world.getEntityById(playerID);
        if (entity != null) {
            renderEntity(style, x, y, entity);
        }
    }

    public static void render(Identifier entityId, CompoundTag entityNBT, IEntityStyle style, int x, int y) {
        if (entityId != null) {
            EntityType<?> entityType = Registry.ENTITY_TYPE.get(entityId);
            if (entityType != null) {
                Entity entity = entityType.create(MinecraftClient.getInstance().world);
                if (entity != null) {
                    EntityType.loadFromEntityTag(MinecraftClient.getInstance().world, null, entity, entityNBT);
                    renderEntity(style, x, y, entity);
                }
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
