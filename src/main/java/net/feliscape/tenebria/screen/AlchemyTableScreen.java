package net.feliscape.tenebria.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.feliscape.tenebria.Tenebria;
import net.feliscape.tenebria.item.custom.SoulContainerItem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AlchemyTableScreen extends AbstractContainerScreen<AlchemyTableMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Tenebria.MOD_ID, "textures/gui/alchemy_table.png");

    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;
    }

    public AlchemyTableScreen(AlchemyTableMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        renderFire(guiGraphics, x, y);
        if (menu.hasRecipe()){
            guiGraphics.drawString(this.font, Integer.toString(menu.getCurrentRecipe().get().getSoulCost()), x + 64, y + 40, 4210752, false);
        }
    }


    private void renderFire(GuiGraphics guiGraphics, int x, int y) {
        if(menu.hasEnoughFuel()) {
            guiGraphics.blit(TEXTURE, x + 46, y + 36, 176, 0, 14, 14);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}

