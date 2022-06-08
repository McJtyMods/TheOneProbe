package mcjty.theoneprobe.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import javax.annotation.Nonnull;

import com.mojang.math.Matrix4f;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.CompoundText;
import mcjty.theoneprobe.api.IOverlayStyle;
import mcjty.theoneprobe.api.TextStyleClass;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.config.Config;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static mcjty.theoneprobe.api.TextStyleClass.*;
import static mcjty.theoneprobe.rendering.RenderHelper.drawTexturedModalRect;

public class GuiConfig extends Screen {
    private static final int WIDTH = 230;
    private static final int HEIGHT = 230;

    private int guiLeft;
    private int guiTop;

    private static final ResourceLocation background = new ResourceLocation(TheOneProbe.MODID, "textures/gui/config.png");
    private static final ResourceLocation scene = new ResourceLocation(TheOneProbe.MODID, "textures/gui/scene.png");

    private static final List<Preset> presets = new ArrayList<>();

    public GuiConfig() {
        super(Component.literal("TOP Config"));
    }

    private List<HitBox> hitboxes = Collections.emptyList();

    static {
        presets.add(new Preset("Default style", 0xff999999, 0x55006699, 2, 0));
        presets.add(new Preset("WAILA style", 0xff4503d0, 0xff000000, 1, 1));
        presets.add(new Preset("Full transparent style", 0x00000000, 0x00000000, 0, 0));
        presets.add(new Preset("Black & White style", 0xffffffff, 0xff000000, 2, 0,
                Pair.of(MODNAME, "white,italic"),
                Pair.of(NAME, "white,bold"),
                Pair.of(INFO, "white"),
                Pair.of(INFOIMP, "white,bold"),
                Pair.of(WARNING, "white"),
                Pair.of(ERROR, "white,underline"),
                Pair.of(OBSOLETE, "white,strikethrough"),
                Pair.of(LABEL, "white,underline"),
                Pair.of(OK, "white"),
                Pair.of(PROGRESS, "white"),
                Pair.of(HIGHLIGHTED, "white")
        ));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void init() {
        super.init();
        guiLeft = (this.width - WIDTH - WIDTH) / 2;
        guiTop = (this.height - HEIGHT) / 2;
    }

//    @Override
//    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
//
//    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        RenderSystem.setShaderTexture(0, background);
        Matrix4f matrix = matrixStack.last().pose();
        drawTexturedModalRect(matrix, guiLeft + WIDTH, guiTop, 0, 0, WIDTH, HEIGHT);
        RenderSystem.setShaderTexture(0, scene);
        drawTexturedModalRect(matrix, guiLeft, guiTop, 0, 0, WIDTH, HEIGHT);

        renderProbe(matrixStack);

        int x = WIDTH + guiLeft + 10;
        int y = guiTop + 10;
        RenderHelper.renderText(Minecraft.getInstance(), matrixStack, x, y, ChatFormatting.GOLD + "Placement:");
        y += 12;
        RenderHelper.renderText(Minecraft.getInstance(), matrixStack, x+10, y, "Click on corner in screenshot");
        y += 10;
        RenderHelper.renderText(Minecraft.getInstance(), matrixStack, x+10, y, "to move tooltip there");
        y += 10;

        y += 20;

        hitboxes = new ArrayList<>();
        RenderHelper.renderText(Minecraft.getInstance(), matrixStack, x, y, ChatFormatting.GOLD + "Presets:");
        y += 12;
        for (Preset preset : presets) {
            y = addPreset(matrixStack, x, y, preset);
        }

        y += 20;

        RenderHelper.renderText(Minecraft.getInstance(), matrixStack, x, y, ChatFormatting.GOLD + "Scale:");
        y += 12;
        addButton(matrixStack, x+10, y, 30, 14, "--", () -> { Config.setScale(1.2f);}); x += 36;
        addButton(matrixStack, x+10, y, 30, 14, "-", () -> { Config.setScale(1.1f);}); x += 36;
        addButton(matrixStack, x+10, y, 30, 14, "0", () -> { Config.setScale(1f);}); x += 36;
        addButton(matrixStack, x+10, y, 30, 14, "+", () -> { Config.setScale(0.9f);}); x += 36;
        addButton(matrixStack, x+10, y, 30, 14, "++", () -> { Config.setScale(0.8f);}); x += 36;

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
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        boolean rc = super.mouseClicked(mouseX, mouseY, mouseButton);
        if (rc) {
            return true;
        }
        if (mouseButton == 0) {
            for (HitBox box : hitboxes) {
                if (box.isHit((int)mouseX-guiLeft, (int)mouseY-guiTop)) {
                    box.call();
                    return true;
                }
            }
        }
        return false;
    }

    private void applyPreset(Preset preset) {
        Config.setBoxStyle(preset.getBoxThickness(), preset.getBoxBorderColor(), preset.getBoxFillColor(), preset.getBoxOffset());

        for (Map.Entry<TextStyleClass, String> entry : Config.defaultTextStyleClasses.entrySet()) {
            Config.setTextStyle(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<TextStyleClass, String> entry : preset.getTextStyleClasses().entrySet()) {
            Config.setTextStyle(entry.getKey(), entry.getValue());
        }
    }

    private int addPreset(PoseStack matrixStack, int x, int y, Preset preset) {
        fill(matrixStack, x + 10, y - 1, x + 10 + WIDTH - 50, y + 10, 0xff000000);
        RenderHelper.renderText(Minecraft.getInstance(), matrixStack, x + 20, y, preset.getName());
        hitboxes.add(new HitBox(x + 10 - guiLeft, y - 1 - guiTop, x + 10 + WIDTH - 50 - guiLeft, y + 10 - guiTop, () -> {
            applyPreset(preset);
        }));
        y += 14;
        return y;
    }

    private void addButton(PoseStack matrixStack, int x, int y, int width, int height, String text, Runnable runnable) {
        fill(matrixStack, x, y, x + width-1, y + height-1, 0xff000000);
        RenderHelper.renderText(Minecraft.getInstance(), matrixStack, x + 3, y + 3, text);
        hitboxes.add(new HitBox(x - guiLeft, y - guiTop, x + width -1 - guiLeft, y + height -1 - guiTop, runnable));
    }

    private void renderProbe(PoseStack matrixStack) {
        Block block = Blocks.OAK_LOG;
        String modName = Tools.getModName(block);
        ProbeInfo probeInfo = TheOneProbe.theOneProbeImp.create();
        ItemStack pickBlock = new ItemStack(block);
        probeInfo.horizontal()
                .item(pickBlock)
                .vertical()
                .text(CompoundText.create().name(pickBlock.getDescriptionId()))
                .text(CompoundText.create().style(MODNAME).text(modName));
        probeInfo.text(CompoundText.createLabelInfo("Fuel: ","5 volts"));
        probeInfo.text(CompoundText.create().style(LABEL).text("Error: ").style(ERROR).text("Oups!"));

        renderElements(probeInfo, Config.getDefaultOverlayStyle(), matrixStack);
    }

    private void renderElements(ProbeInfo probeInfo, IOverlayStyle style, PoseStack matrixStack) {
        matrixStack.pushPose();
        float scale = (float) (1 / Config.tooltipScale.get());
        matrixStack.scale(scale, scale, scale);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.translate(0, 0, 1);

        int w = probeInfo.getWidth();
        int h = probeInfo.getHeight();

        int offset = style.getBorderOffset();
        int thick = style.getBorderThickness();
        int margin = 0;
        if (thick > 0) {
            w += (offset + thick + 3) * 2;
            h += (offset + thick + 3) * 2;
            margin = offset + thick + 3;
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

        double factor = (Config.tooltipScale.get() - 1) * 1.4 + 1;
        x = (int) (x * factor);
        y = (int) (y * factor);

        if (thick > 0) {
            int x2 = x + w - 1;
            int y2 = y + h - 1;
            if (offset > 0) {
                RenderHelper.drawThickBeveledBox(matrixStack, x, y, x2, y2, thick, style.getBoxColor(), style.getBoxColor(), style.getBoxColor());
            }
            RenderHelper.drawThickBeveledBox(matrixStack, x+offset, y+offset, x2-offset, y2-offset, thick, style.getBorderColor(), style.getBorderColor(), style.getBoxColor());
        }

        if (!Minecraft.getInstance().isPaused()) {
            RenderHelper.rot += .5f;
        }

        probeInfo.render(matrixStack, x + margin, y + margin);

        matrixStack.popPose();
    }

    public static void open() {
        Minecraft.getInstance().setScreen(new GuiConfig());
    }
}
