package mcjty.theoneprobe.gui;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

import static mcjty.theoneprobe.config.Config.*;

public class GuiNote extends GuiScreen {
    private static final int WIDTH = 256;
    private static final int HEIGHT = 160;

    private static final int BUTTON_WIDTH = 70;
    private static final int BUTTON_MARGIN = 80;
    public static final int BUTTON_HEIGHT = 16;

    private int guiLeft;
    private int guiTop;

    private static final ResourceLocation background = new ResourceLocation(TheOneProbe.MODID, "textures/gui/note.png");

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        super.initGui();
        guiLeft = (this.width - WIDTH) / 2;
        guiTop = (this.height - HEIGHT) / 2;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, WIDTH, HEIGHT);
        int x = guiLeft + 5;
        int y = guiTop + 8;
        RenderHelper.renderText(Minecraft.getMinecraft(), x, y, I18n.format("gui.theoneprobe.gui_note.title"));
        y += 10;
        y += 10;

        RenderHelper.renderText(Minecraft.getMinecraft(), x, y, I18n.format("gui.theoneprobe.gui_note.body.1"));
        y += 10;
        RenderHelper.renderText(Minecraft.getMinecraft(), x, y, I18n.format("gui.theoneprobe.gui_note.body.2"));
        y += 10;

        y += 10;
        switch (Config.needsProbe) {
            case PROBE_NEEDED:
                RenderHelper.renderText(Minecraft.getMinecraft(), x, y, I18n.format("gui.theoneprobe.gui_note.needed.1"));
                y += 10;
                RenderHelper.renderText(Minecraft.getMinecraft(), x, y, I18n.format("gui.theoneprobe.gui_note.needed.2"));
                y += 10;
                y += 16;
                y = setInConfig(x, y);
                break;
            case PROBE_NOTNEEDED:
                RenderHelper.renderText(Minecraft.getMinecraft(), x, y, I18n.format("gui.theoneprobe.gui_note.not_needed.1"));
                y += 10;
                RenderHelper.renderText(Minecraft.getMinecraft(), x, y, I18n.format("gui.theoneprobe.gui_note.not_needed.2"));
                y += 10;
                y += 16;
                y = setInConfig(x, y);
                break;
            case PROBE_NEEDEDFOREXTENDED:
                RenderHelper.renderText(Minecraft.getMinecraft(), x, y, I18n.format("gui.theoneprobe.gui_note.needed_for_extended.1"));
                y += 10;
                RenderHelper.renderText(Minecraft.getMinecraft(), x, y, I18n.format("gui.theoneprobe.gui_note.needed_for_extended.2"));
                y += 10;
                RenderHelper.renderText(Minecraft.getMinecraft(), x, y, I18n.format("gui.theoneprobe.gui_note.needed_for_extended.3"));
                y += 10;
                y += 6;
                y = setInConfig(x, y);
                break;
            case PROBE_NEEDEDHARD:
                RenderHelper.renderText(Minecraft.getMinecraft(), x, y, I18n.format("gui.theoneprobe.gui_note.needed_hard.1"));
                y += 10;
                RenderHelper.renderText(Minecraft.getMinecraft(), x, y, I18n.format("gui.theoneprobe.gui_note.needed_hard.2"));
                y += 10;
                RenderHelper.renderText(Minecraft.getMinecraft(), x, y, I18n.format("gui.theoneprobe.gui_note.needed_hard.3"));
                y += 10;
                break;
        }

        y += 10;

        RenderHelper.renderText(Minecraft.getMinecraft(), x, y, I18n.format("gui.theoneprobe.gui_note.tail.1"));
        y += 10;
        RenderHelper.renderText(Minecraft.getMinecraft(), x, y, I18n.format("gui.theoneprobe.gui_note.tail.2"));
        y += 10;
        RenderHelper.renderText(Minecraft.getMinecraft(), x, y, I18n.format("gui.theoneprobe.gui_note.tail.3"));
        y += 10;
    }

    private int hitX;
    private int hitY;

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        mouseX += guiLeft;
        mouseY += guiTop;
        if (mouseY >= hitY && mouseY < hitY + BUTTON_HEIGHT) {
            if (mouseX >= hitX && mouseX < hitX + BUTTON_WIDTH) {
                Config.setProbeNeeded(PROBE_NEEDED);
            } else if (mouseX >= hitX + BUTTON_MARGIN && mouseX < hitX + BUTTON_WIDTH + BUTTON_MARGIN) {
                Config.setProbeNeeded(PROBE_NOTNEEDED);
            } else if (mouseX >= hitX + BUTTON_MARGIN * 2 && mouseX < hitX + BUTTON_WIDTH + BUTTON_MARGIN * 2) {
                Config.setProbeNeeded(PROBE_NEEDEDFOREXTENDED);
            }
        }
    }

    private int setInConfig(int x, int y) {
        RenderHelper.renderText(Minecraft.getMinecraft(), x, y, I18n.format("gui.theoneprobe.gui_note.button.title"));
        y += 10;

        hitY = y + guiTop;
        hitX = x + guiLeft;
        drawRect(x, y, x + BUTTON_WIDTH, y + BUTTON_HEIGHT, 0xff000000);
        RenderHelper.renderText(Minecraft.getMinecraft(), x + 3, y + 4, I18n.format("gui.theoneprobe.gui_note.button.needed"));
        x += BUTTON_MARGIN;

        drawRect(x, y, x + BUTTON_WIDTH, y + BUTTON_HEIGHT, 0xff000000);
        RenderHelper.renderText(Minecraft.getMinecraft(), x + 3, y + 4, I18n.format("gui.theoneprobe.gui_note.button.not_needed"));
        x += BUTTON_MARGIN;

        drawRect(x, y, x + BUTTON_WIDTH, y + BUTTON_HEIGHT, 0xff000000);
        RenderHelper.renderText(Minecraft.getMinecraft(), x + 3, y + 4, I18n.format("gui.theoneprobe.gui_note.button.extended"));
        x += BUTTON_MARGIN;

        y += BUTTON_HEIGHT - 4;
        return y;
    }

}
