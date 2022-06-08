package mcjty.theoneprobe.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nonnull;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.ChatFormatting;

import static mcjty.theoneprobe.config.Config.*;
import static mcjty.theoneprobe.rendering.RenderHelper.drawTexturedModalRect;
import static net.minecraft.ChatFormatting.BOLD;
import static net.minecraft.ChatFormatting.GREEN;

public class GuiNote extends Screen {
    private static final int WIDTH = 256;
    private static final int HEIGHT = 160;

    private static final int BUTTON_WIDTH = 70;
    private static final int BUTTON_MARGIN = 80;
    public static final int BUTTON_HEIGHT = 16;

    private int guiLeft;
    private int guiTop;

    private static final ResourceLocation background = new ResourceLocation(TheOneProbe.MODID, "textures/gui/note.png");

    public GuiNote() {
        super(Component.literal("note"));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void init() {
        super.init();
        guiLeft = (this.width - WIDTH) / 2;
        guiTop = (this.height - HEIGHT) / 2;
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        RenderSystem.setShaderTexture(0, background);
        drawTexturedModalRect(matrixStack.last().pose(), guiLeft, guiTop, 0, 0, WIDTH, HEIGHT);
        int x = guiLeft+5;
        int y = guiTop+8;
        RenderHelper.renderText(Minecraft.getInstance(), matrixStack, x, y, "Things you should know about" + ChatFormatting.GOLD + " The One Probe"); y += 10;
        y += 10;

        RenderHelper.renderText(Minecraft.getInstance(), matrixStack, x, y, BOLD + "This mod can show a tooltip on screen"); y += 10;
        RenderHelper.renderText(Minecraft.getInstance(), matrixStack, x, y, BOLD + "when you look at a block or an entity"); y += 10;

        y += 10;
        switch (Config.needsProbe.get()) {
            case PROBE_NEEDED -> {
                RenderHelper.renderText(Minecraft.getInstance(), matrixStack, x, y, "In this pack the probe is configured to be");
                y += 10;
                RenderHelper.renderText(Minecraft.getInstance(), matrixStack, x, y, "required in order to see the tooltip");
                y += 10;
                y += 16;
                y = setInConfig(matrixStack, x, y);
            }
            case PROBE_NOTNEEDED -> {
                RenderHelper.renderText(Minecraft.getInstance(), matrixStack, x, y, "In this pack the probe is configured to be not");
                y += 10;
                RenderHelper.renderText(Minecraft.getInstance(), matrixStack, x, y, "required in order to see the tooltip");
                y += 10;
                y += 16;
                y = setInConfig(matrixStack, x, y);
            }
            case PROBE_NEEDEDFOREXTENDED -> {
                RenderHelper.renderText(Minecraft.getInstance(), matrixStack, x, y, "In this pack the probe is configured to be");
                y += 10;
                RenderHelper.renderText(Minecraft.getInstance(), matrixStack, x, y, "required to see extended information (when");
                y += 10;
                RenderHelper.renderText(Minecraft.getInstance(), matrixStack, x, y, "sneaking) but not for basic information");
                y += 10;
                y += 6;
                y = setInConfig(matrixStack, x, y);
            }
            case PROBE_NEEDEDHARD -> {
                RenderHelper.renderText(Minecraft.getInstance(), matrixStack, x, y, "In this pack the probe is configured to be");
                y += 10;
                RenderHelper.renderText(Minecraft.getInstance(), matrixStack, x, y, "required in order to see the tooltip");
                y += 10;
                RenderHelper.renderText(Minecraft.getInstance(), matrixStack, x, y, "This is set server side");
                y += 10;
            }
        }

        y += 10;

        RenderHelper.renderText(Minecraft.getInstance(), matrixStack, x, y, "Check out the 'Mod Options... for many client'"); y += 10;
        RenderHelper.renderText(Minecraft.getInstance(), matrixStack, x, y, "side configuration settings or sneak-right click"); y += 10;
        RenderHelper.renderText(Minecraft.getInstance(), matrixStack, x, y, "this note for more user-friendly setup"); y += 10;
    }

    private int hitX;
    private int hitY;

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        boolean rc = super.mouseClicked(mouseX, mouseY, mouseButton);
        if (rc) {
            return true;
        }
        mouseX += guiLeft;
        mouseY += guiTop;
        if (mouseY >= hitY && mouseY < hitY + BUTTON_HEIGHT) {
            if (mouseX >= hitX && mouseX < hitX + BUTTON_WIDTH) {
                Config.setProbeNeeded(PROBE_NEEDED);
            } else if (mouseX >= hitX+BUTTON_MARGIN && mouseX < hitX + BUTTON_WIDTH+BUTTON_MARGIN) {
                Config.setProbeNeeded(PROBE_NOTNEEDED);
            } else if (mouseX >= hitX+BUTTON_MARGIN*2 && mouseX < hitX + BUTTON_WIDTH+BUTTON_MARGIN*2) {
                Config.setProbeNeeded(PROBE_NEEDEDFOREXTENDED);
            }
            return true;
        }
        return false;
    }

    private int setInConfig(PoseStack matrixStack, int x, int y) {
        RenderHelper.renderText(Minecraft.getInstance(), matrixStack, x, y, BOLD + "" + GREEN + "You can change this here:");
        y += 10;

        hitY = y + guiTop;
        hitX = x + guiLeft;
        fill(matrixStack, x, y, x + BUTTON_WIDTH, y + BUTTON_HEIGHT, 0xff000000);
        RenderHelper.renderText(Minecraft.getInstance(), matrixStack, x + 3, y + 4, "Needed"); x += BUTTON_MARGIN;

        fill(matrixStack, x, y, x + BUTTON_WIDTH, y + BUTTON_HEIGHT, 0xff000000);
        RenderHelper.renderText(Minecraft.getInstance(), matrixStack, x + 3, y + 4, "Not needed"); x += BUTTON_MARGIN;

        fill(matrixStack, x, y, x + BUTTON_WIDTH, y + BUTTON_HEIGHT, 0xff000000);
        RenderHelper.renderText(Minecraft.getInstance(), matrixStack, x + 3, y + 4, "Extended"); x += BUTTON_MARGIN;

        y += BUTTON_HEIGHT - 4;
        return y;
    }

    public static void open() {
        Minecraft.getInstance().setScreen(new GuiNote());
    }
}
