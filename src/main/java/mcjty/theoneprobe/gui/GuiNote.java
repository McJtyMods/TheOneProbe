package mcjty.theoneprobe.gui;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import static mcjty.theoneprobe.config.Config.*;
import static net.minecraft.util.text.TextFormatting.*;

public class GuiNote extends GuiScreen {
    private static final int WIDTH = 256;
    private static final int HEIGHT = 160;

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
        switch (Config.needsProbe) {
            case PROBE_NEEDED:
                RenderHelper.renderText(Minecraft.getMinecraft(), x, y, "In this pack the probe is configured to be"); y += 10;
                RenderHelper.renderText(Minecraft.getMinecraft(), x, y, "required in order to see the tooltip"); y += 10;
                y = setInConfig(x, y);
                break;
            case PROBE_NOTNEEDED:
                RenderHelper.renderText(Minecraft.getMinecraft(), x, y, "In this pack the probe is configured to be not"); y += 10;
                RenderHelper.renderText(Minecraft.getMinecraft(), x, y, "required in order to see the tooltip"); y += 10;
                y = setInConfig(x, y);
                break;
            case PROBE_NEEDEDFOREXTENDED:
                RenderHelper.renderText(Minecraft.getMinecraft(), x, y, "In this pack the probe is configured to be"); y += 10;
                RenderHelper.renderText(Minecraft.getMinecraft(), x, y, "required to see extended information"); y += 10;
                RenderHelper.renderText(Minecraft.getMinecraft(), x, y, "This is info you can see when sneaking but you"); y += 10;
                RenderHelper.renderText(Minecraft.getMinecraft(), x, y, "can see basic info without a probe"); y += 10;
                y = setInConfig(x, y);
                break;
            case PROBE_NEEDEDHARD:
                RenderHelper.renderText(Minecraft.getMinecraft(), x, y, "In this pack the probe is configured to be"); y += 10;
                RenderHelper.renderText(Minecraft.getMinecraft(), x, y, "required in order to see the tooltip"); y += 10;
                RenderHelper.renderText(Minecraft.getMinecraft(), x, y, "This is set server side"); y += 10;
                break;
        }

        y += 10;

        RenderHelper.renderText(Minecraft.getMinecraft(), x, y, "Be sure to check out the 'Mod Options...'"); y += 10;
        RenderHelper.renderText(Minecraft.getMinecraft(), x, y, "for many client-side configuration settings"); y += 10;
    }

    private int setInConfig(int x, int y) {
        RenderHelper.renderText(Minecraft.getMinecraft(), x, y, BOLD + "You can change this in the config:");
        y += 10;
        RenderHelper.renderText(Minecraft.getMinecraft(), x, y, RESET + "Change " + ITALIC + "needsProbe" + RESET + " in " + ITALIC + "theoneprobe.cfg");
        y += 10;
        return y;
    }

}
