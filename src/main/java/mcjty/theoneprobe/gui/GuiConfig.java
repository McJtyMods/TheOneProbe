package mcjty.theoneprobe.gui;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.IOverlayStyle;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static mcjty.theoneprobe.api.TextStyleClass.*;

public class GuiConfig extends GuiScreen {
    private static final int WIDTH = 230;
    private static final int HEIGHT = 230;

    private int guiLeft;
    private int guiTop;

    private static final ResourceLocation background = new ResourceLocation(TheOneProbe.MODID, "textures/gui/config.png");
    private static final ResourceLocation scene = new ResourceLocation(TheOneProbe.MODID, "textures/gui/scene.png");

    private static final List<Preset> presets = new ArrayList<>();

    private List<HitBox> hitboxes = Collections.emptyList();

    static {
        presets.add(new Preset("Default style", 0xff999999, 0x55006699, 2));
        presets.add(new Preset("WAILA style", 0xff4503d0, 0xff000000, 2));
        presets.add(new Preset("Full transparent style", 0x00000000, 0x00000000, 0));
        presets.add(new Preset("Black & White style", 0xffffffff, 0xff000000, 2));
    }


    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        super.initGui();
        guiLeft = (this.width - WIDTH - WIDTH) / 2;
        guiTop = (this.height - HEIGHT) / 2;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft + WIDTH, guiTop, 0, 0, WIDTH, HEIGHT);
        mc.getTextureManager().bindTexture(scene);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, WIDTH, HEIGHT);

        renderProbe();

        int x = WIDTH + guiLeft + 10;
        int y = guiTop + 10;
        RenderHelper.renderText(Minecraft.getMinecraft(), x, y, TextFormatting.GOLD + "Placement:");
        y += 12;
        RenderHelper.renderText(Minecraft.getMinecraft(), x+10, y, "Click on corner in screenshot");
        y += 10;
        RenderHelper.renderText(Minecraft.getMinecraft(), x+10, y, "to move tooltip there");
        y += 10;

        y += 20;

        hitboxes = new ArrayList<>();
        RenderHelper.renderText(Minecraft.getMinecraft(), x, y, TextFormatting.GOLD + "Presets:");
        y += 12;
        for (Preset preset : presets) {
            y = addPreset(x, y, preset);
        }

        y += 20;

        RenderHelper.renderText(Minecraft.getMinecraft(), x, y, TextFormatting.GOLD + "Scale:");
        y += 12;
        addButton(x+10, y, 30, 14, "--", () -> { Config.setScale(1.2f);}); x += 36;
        addButton(x+10, y, 30, 14, "-", () -> { Config.setScale(1.1f);}); x += 36;
        addButton(x+10, y, 30, 14, "0", () -> { Config.setScale(1f);}); x += 36;
        addButton(x+10, y, 30, 14, "+", () -> { Config.setScale(0.9f);}); x += 36;
        addButton(x+10, y, 30, 14, "++", () -> { Config.setScale(0.8f);}); x += 36;

        int margin = 90;
        hitboxes.add(new HitBox(0, 0, margin, margin, () -> {
            Config.setPos(5, 5, -1, -1);
        }));
        hitboxes.add(new HitBox(margin, 0, WIDTH - margin, margin, () -> {
            Config.setPos(-1, 5, -1, -1);
        }));
        hitboxes.add(new HitBox(WIDTH - margin, 0, WIDTH, margin, () -> {
            Config.setPos(-1, 5, 5, -1);
        }));
        hitboxes.add(new HitBox(0, margin, margin, HEIGHT - margin, () -> {
            Config.setPos(5, -1, -1, -1);
        }));
        hitboxes.add(new HitBox(margin, margin, WIDTH - margin, HEIGHT - margin, () -> {
            Config.setPos(-1, -1, -1, -1);
        }));
        hitboxes.add(new HitBox(WIDTH - margin, margin, WIDTH, HEIGHT - margin, () -> {
            Config.setPos(-1, -1, 5, -1);
        }));
        hitboxes.add(new HitBox(0, HEIGHT - margin, margin, HEIGHT, () -> {
            Config.setPos(5, -1, -1, 5);
        }));
        hitboxes.add(new HitBox(margin, HEIGHT - margin, WIDTH - margin, HEIGHT, () -> {
            Config.setPos(-1, -1, -1, 20);
        }));
        hitboxes.add(new HitBox(WIDTH - margin, HEIGHT - margin, WIDTH, HEIGHT, () -> {
            Config.setPos(-1, -1, 5, 5);
        }));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0) {
            for (HitBox box : hitboxes) {
                if (box.isHit(mouseX-guiLeft, mouseY-guiTop)) {
                    box.call();
                }
            }
        }
    }

    private void applyPreset(Preset preset) {
        Config.setBoxStyle(preset.getBoxThickness(), preset.getBoxBorderColor(), preset.getBoxFillColor());
    }

    private int addPreset(int x, int y, Preset preset) {
        drawRect(x + 10, y - 1, x + 10 + WIDTH - 50, y + 10, 0xff000000);
        RenderHelper.renderText(Minecraft.getMinecraft(), x + 20, y, preset.getName());
        hitboxes.add(new HitBox(x + 10 - guiLeft, y - 1 - guiTop, x + 10 + WIDTH - 50 - guiLeft, y + 10 - guiTop, () -> {
            applyPreset(preset);
        }));
        y += 14;
        return y;
    }

    private void addButton(int x, int y, int width, int height, String text, Runnable runnable) {
        drawRect(x, y, x + width-1, y + height-1, 0xff000000);
        RenderHelper.renderText(Minecraft.getMinecraft(), x + 3, y + 3, text);
        hitboxes.add(new HitBox(x - guiLeft, y - guiTop, x + width -1 - guiLeft, y + height -1 - guiTop, runnable));
    }

    private void renderProbe() {
        Block block = Blocks.LOG;
        String modid = Tools.getModName(block);
        ProbeInfo probeInfo = TheOneProbe.theOneProbeImp.create();
        ItemStack pickBlock = new ItemStack(block);
        probeInfo.horizontal()
                .item(pickBlock)
                .vertical()
                .text(NAME + pickBlock.getDisplayName())
                .text(MODNAME + modid);
        probeInfo.text(LABEL + "Fuel: " + INFO + "5 volts");
        probeInfo.text(LABEL + "Error: " + ERROR + "Oups!");

        renderElements(probeInfo, Config.getDefaultOverlayStyle());
    }

    private void renderElements(ProbeInfo probeInfo, IOverlayStyle style) {

        GlStateManager.pushMatrix();
        GlStateManager.scale(1/Config.tooltipScale, 1/Config.tooltipScale, 1/Config.tooltipScale);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();

        int w = probeInfo.getWidth();
        int h = probeInfo.getHeight();

        int thick = style.getBorderThickness();
        int margin = 0;
        if (thick > 0) {
            w += (thick + 3) * 2;
            h += (thick + 3) * 2;
            margin = thick + 3;
        }

        int x;
        int y;
        if (style.getLeftX() != -1) {
            x = style.getLeftX();
        } else if (style.getRightX() != -1) {
            x = WIDTH - w - style.getRightX();
        } else {
            x = (WIDTH - w) / 2;
        }
        if (style.getTopY() != -1) {
            y = style.getTopY();
        } else if (style.getBottomY() != -1) {
            y = HEIGHT - h - style.getBottomY();
        } else {
            y = (HEIGHT - h) / 2;
        }


        x += guiLeft;
        y += guiTop;

        double factor = (Config.tooltipScale - 1) * 1.4 + 1;
        x *= factor;
        y *= factor;

        if (thick > 0) {
            int x2 = x + w - 1;
            int y2 = y + h - 1;
            RenderHelper.drawThickBeveledBox(x, y, x2, y2, thick, style.getBorderColor(), style.getBorderColor(), style.getBoxColor());
        }

        if (!Minecraft.getMinecraft().isGamePaused()) {
            RenderHelper.rot += .5f;
        }

        probeInfo.render(x + margin, y + margin);

        GlStateManager.popMatrix();
    }

}
