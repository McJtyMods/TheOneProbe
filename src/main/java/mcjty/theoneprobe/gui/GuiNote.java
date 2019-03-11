package mcjty.theoneprobe.gui;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.config.ConfigSetup;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.io.IOException;

import static mcjty.theoneprobe.config.ConfigSetup.*;
import static net.minecraft.util.text.TextFormatting.*;

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
        int x = guiLeft+5;
        int y = guiTop+8;
        RenderHelper.renderText(Minecraft.getMinecraft(), x, y, "Things you should know about" + TextFormatting.GOLD + " The One Probe"); y += 10;
        y += 10;

        RenderHelper.renderText(Minecraft.getMinecraft(), x, y, BOLD + "This mod can show a tooltip on screen"); y += 10;
        RenderHelper.renderText(Minecraft.getMinecraft(), x, y, BOLD + "when you look at a block or an entity"); y += 10;

        y += 10;
        switch (ConfigSetup.needsProbe) {
            case PROBE_NEEDED:
                RenderHelper.renderText(Minecraft.getMinecraft(), x, y, "In this pack the probe is configured to be"); y += 10;
                RenderHelper.renderText(Minecraft.getMinecraft(), x, y, "required in order to see the tooltip"); y += 10;
                y += 16;
                y = setInConfig(x, y);
                break;
            case PROBE_NOTNEEDED:
                RenderHelper.renderText(Minecraft.getMinecraft(), x, y, "In this pack the probe is configured to be not"); y += 10;
                RenderHelper.renderText(Minecraft.getMinecraft(), x, y, "required in order to see the tooltip"); y += 10;
                y += 16;
                y = setInConfig(x, y);
                break;
            case PROBE_NEEDEDFOREXTENDED:
                RenderHelper.renderText(Minecraft.getMinecraft(), x, y, "In this pack the probe is configured to be"); y += 10;
                RenderHelper.renderText(Minecraft.getMinecraft(), x, y, "required to see extended information (when"); y += 10;
                RenderHelper.renderText(Minecraft.getMinecraft(), x, y, "sneaking) but not for basic information"); y += 10;
                y += 6;
                y = setInConfig(x, y);
                break;
            case PROBE_NEEDEDHARD:
                RenderHelper.renderText(Minecraft.getMinecraft(), x, y, "In this pack the probe is configured to be"); y += 10;
                RenderHelper.renderText(Minecraft.getMinecraft(), x, y, "required in order to see the tooltip"); y += 10;
                RenderHelper.renderText(Minecraft.getMinecraft(), x, y, "This is set server side"); y += 10;
                break;
        }

        y += 10;

        RenderHelper.renderText(Minecraft.getMinecraft(), x, y, "Check out the 'Mod Options... for many client'"); y += 10;
        RenderHelper.renderText(Minecraft.getMinecraft(), x, y, "side configuration settings or sneak-right click"); y += 10;
        RenderHelper.renderText(Minecraft.getMinecraft(), x, y, "this note for more user-friendly setup"); y += 10;
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
                ConfigSetup.setProbeNeeded(PROBE_NEEDED);
            } else if (mouseX >= hitX+BUTTON_MARGIN && mouseX < hitX + BUTTON_WIDTH+BUTTON_MARGIN) {
                ConfigSetup.setProbeNeeded(PROBE_NOTNEEDED);
            } else if (mouseX >= hitX+BUTTON_MARGIN*2 && mouseX < hitX + BUTTON_WIDTH+BUTTON_MARGIN*2) {
                ConfigSetup.setProbeNeeded(PROBE_NEEDEDFOREXTENDED);
            }
        }
    }

    private int setInConfig(int x, int y) {
        RenderHelper.renderText(Minecraft.getMinecraft(), x, y, BOLD + "" + GREEN + "You can change this here:");
        y += 10;

        hitY = y + guiTop;
        hitX = x + guiLeft;
        drawRect(x, y, x + BUTTON_WIDTH, y + BUTTON_HEIGHT, 0xff000000);
        RenderHelper.renderText(Minecraft.getMinecraft(), x + 3, y + 4, "Needed"); x += BUTTON_MARGIN;

        drawRect(x, y, x + BUTTON_WIDTH, y + BUTTON_HEIGHT, 0xff000000);
        RenderHelper.renderText(Minecraft.getMinecraft(), x + 3, y + 4, "Not needed"); x += BUTTON_MARGIN;

        drawRect(x, y, x + BUTTON_WIDTH, y + BUTTON_HEIGHT, 0xff000000);
        RenderHelper.renderText(Minecraft.getMinecraft(), x + 3, y + 4, "Extended"); x += BUTTON_MARGIN;

        y += BUTTON_HEIGHT - 4;
        return y;
    }

}
