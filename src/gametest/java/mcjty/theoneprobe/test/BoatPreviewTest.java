package mcjty.theoneprobe.test;

import mcjty.theoneprobe.rendering.RenderHelper;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Objects;

/** Exercises deferred GUI rendering, where an incompatible boat water mask crashes the client. */
public class BoatPreviewTest implements FabricClientGameTest {

    @Override
    public void runTest(ClientGameTestContext context) {
        try (var world = context.worldBuilder().create()) {
            context.waitFor(client -> client.level != null && client.player != null);
            boolean originalTransparency = context.computeOnClient(client -> client.options.improvedTransparency().get());
            try {
                var preview = context.computeOnClient(client -> new BoatPreviewScreen(client.level));
                context.setScreen(() -> preview);
                for (boolean improvedTransparency : new boolean[]{true, false}) {
                    context.runOnClient(client -> client.options.improvedTransparency().set(improvedTransparency));
                    context.waitFor(client -> client.gameRenderer.useImprovedTransparency() == improvedTransparency);
                    context.runOnClient(client -> preview.extractedFrames = 0);
                    context.waitFor(client -> preview.extractedFrames >= 3);
                    context.waitTicks(2);
                }
            } finally {
                context.setScreen(() -> null);
                context.runOnClient(client -> client.options.improvedTransparency().set(originalTransparency));
            }
        }
    }

    private static class BoatPreviewScreen extends Screen {
        private final List<Entity> entities;
        private int extractedFrames;

        private BoatPreviewScreen(Level level) {
            super(Component.literal("Boat preview regression test"));
            entities = List.of(
                    Objects.requireNonNull(EntityTypes.OAK_BOAT.create(level, EntitySpawnReason.LOAD)),
                    Objects.requireNonNull(EntityTypes.OAK_CHEST_BOAT.create(level, EntitySpawnReason.LOAD)),
                    Objects.requireNonNull(EntityTypes.BAMBOO_RAFT.create(level, EntitySpawnReason.LOAD)),
                    Objects.requireNonNull(EntityTypes.COW.create(level, EntitySpawnReason.LOAD)));
            for (int i = 0; i < entities.size(); i++) {
                entities.get(i).setId(-1 - i);
            }
        }

        @Override
        public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
            for (int i = 0; i < entities.size(); i++) {
                RenderHelper.renderEntity(entities.get(i), graphics, 10 + i * 60, 10, 50, 50, 20.0f);
            }
            extractedFrames++;
        }
    }
}
