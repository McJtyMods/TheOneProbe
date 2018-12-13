package mcjty.theoneprobe.gui;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.ContainerGui;
import net.minecraft.container.Container;
import net.minecraft.util.Identifier;

import static mcjty.theoneprobe.config.Config.*;
import static net.minecraft.text.TextFormat.*;

public class GuiNote extends ContainerGui {
    private static final int WIDTH = 256;
    private static final int HEIGHT = 160;

    private static final int BUTTON_WIDTH = 70;
    private static final int BUTTON_MARGIN = 80;
    public static final int BUTTON_HEIGHT = 16;

    private int guiLeft;
    private int guiTop;

    private static final Identifier background = new Identifier(TheOneProbe.MODID, "textures/gui/note.png");

    public GuiNote(Container var1) {
        super(var1);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }


    @Override
    protected void onInitialized() {
        super.onInitialized();
        guiLeft = (this.width - WIDTH) / 2;
        guiTop = (this.height - HEIGHT) / 2;
    }

    @Override
    protected void drawBackground(float var1, int var2, int var3) {

    }



    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        super.draw(mouseX, mouseY, partialTicks);
        client.getTextureManager().bindTexture(background);
        drawTexturedRect(guiLeft, guiTop, 0, 0, WIDTH, HEIGHT);
        int x = guiLeft+5;
        int y = guiTop+8;
        RenderHelper.renderText(MinecraftClient.getInstance(), x, y, "Things you should know about" + GOLD + " The One Probe"); y += 10;
        y += 10;

        RenderHelper.renderText(MinecraftClient.getInstance(), x, y, BOLD + "This mod can show a tooltip on screen"); y += 10;
        RenderHelper.renderText(MinecraftClient.getInstance(), x, y, BOLD + "when you look at a block or an entity"); y += 10;

        y += 10;
        switch (Config.needsProbe) {
            case PROBE_NEEDED:
                RenderHelper.renderText(MinecraftClient.getInstance(), x, y, "In this pack the probe is configured to be"); y += 10;
                RenderHelper.renderText(MinecraftClient.getInstance(), x, y, "required in order to see the tooltip"); y += 10;
                y += 16;
                y = setInConfig(x, y);
                break;
            case PROBE_NOTNEEDED:
                RenderHelper.renderText(MinecraftClient.getInstance(), x, y, "In this pack the probe is configured to be not"); y += 10;
                RenderHelper.renderText(MinecraftClient.getInstance(), x, y, "required in order to see the tooltip"); y += 10;
                y += 16;
                y = setInConfig(x, y);
                break;
            case PROBE_NEEDEDFOREXTENDED:
                RenderHelper.renderText(MinecraftClient.getInstance(), x, y, "In this pack the probe is configured to be"); y += 10;
                RenderHelper.renderText(MinecraftClient.getInstance(), x, y, "required to see extended information (when"); y += 10;
                RenderHelper.renderText(MinecraftClient.getInstance(), x, y, "sneaking) but not for basic information"); y += 10;
                y += 6;
                y = setInConfig(x, y);
                break;
            case PROBE_NEEDEDHARD:
                RenderHelper.renderText(MinecraftClient.getInstance(), x, y, "In this pack the probe is configured to be"); y += 10;
                RenderHelper.renderText(MinecraftClient.getInstance(), x, y, "required in order to see the tooltip"); y += 10;
                RenderHelper.renderText(MinecraftClient.getInstance(), x, y, "This is set server side"); y += 10;
                break;
        }

        y += 10;

        RenderHelper.renderText(MinecraftClient.getInstance(), x, y, "Check out the 'Mod Options... for many client'"); y += 10;
        RenderHelper.renderText(MinecraftClient.getInstance(), x, y, "side configuration settings or sneak-right click"); y += 10;
        RenderHelper.renderText(MinecraftClient.getInstance(), x, y, "this note for more user-friendly setup"); y += 10;
    }

    private int hitX;
    private int hitY;

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        boolean rc = super.mouseClicked(mouseX, mouseY, mouseButton);
        if (rc) {
            return rc;
        }
        mouseX += guiLeft;
        mouseY += guiTop;
        if (mouseY >= hitY && mouseY < hitY + BUTTON_HEIGHT) {
            if (mouseX >= hitX && mouseX < hitX + BUTTON_WIDTH) {
                // @todo fabric
//                Config.setProbeNeeded(PROBE_NEEDED);
                return true;
            } else if (mouseX >= hitX+BUTTON_MARGIN && mouseX < hitX + BUTTON_WIDTH+BUTTON_MARGIN) {
//                Config.setProbeNeeded(PROBE_NOTNEEDED);
                return true;
            } else if (mouseX >= hitX+BUTTON_MARGIN*2 && mouseX < hitX + BUTTON_WIDTH+BUTTON_MARGIN*2) {
//                Config.setProbeNeeded(PROBE_NEEDEDFOREXTENDED);
                return true;
            }
        }
        return false;
    }

    private int setInConfig(int x, int y) {
        RenderHelper.renderText(MinecraftClient.getInstance(), x, y, BOLD + "" + GREEN + "You can change this here:");
        y += 10;

        hitY = y + guiTop;
        hitX = x + guiLeft;
        drawRect(x, y, x + BUTTON_WIDTH, y + BUTTON_HEIGHT, 0xff000000);
        RenderHelper.renderText(MinecraftClient.getInstance(), x + 3, y + 4, "Needed"); x += BUTTON_MARGIN;

        drawRect(x, y, x + BUTTON_WIDTH, y + BUTTON_HEIGHT, 0xff000000);
        RenderHelper.renderText(MinecraftClient.getInstance(), x + 3, y + 4, "Not needed"); x += BUTTON_MARGIN;

        drawRect(x, y, x + BUTTON_WIDTH, y + BUTTON_HEIGHT, 0xff000000);
        RenderHelper.renderText(MinecraftClient.getInstance(), x + 3, y + 4, "Extended"); x += BUTTON_MARGIN;

        y += BUTTON_HEIGHT - 4;
        return y;
    }

}
