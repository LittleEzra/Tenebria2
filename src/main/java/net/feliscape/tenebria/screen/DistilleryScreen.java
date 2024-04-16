package net.feliscape.tenebria.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.feliscape.tenebria.Tenebria;
import net.feliscape.tenebria.util.ColorUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class DistilleryScreen extends AbstractContainerScreen<DistilleryMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Tenebria.MOD_ID, "textures/gui/distillery.png");

    @Override
    protected void init() {
        super.init();
    }

    public DistilleryScreen(DistilleryMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = this.leftPos;
        int y = this.topPos;
        guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);
        guiGraphics.drawString(this.font, Integer.toString(this.menu.getSoulAmount()), x + 121, y + 39, 4210752, false);
        if (this.menu.isLit()) {
            int litProgress = this.menu.getLitProgress();
            guiGraphics.blit(TEXTURE, x + 56, y + 36 + 12 - litProgress, 176, 12 - litProgress, 14, litProgress + 1);
        }

        int burnProgress = this.menu.getBurnProgress();
        guiGraphics.blit(TEXTURE, x + 79, y + 34, 176, 14, burnProgress + 1, 16);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
