package com.rabbitminers.buildersbundle.container;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.rabbitminers.buildersbundle.ArchitectsSatchel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class SatchelGui extends AbstractContainerScreen<SatchelContainerMenu> {
    private final int containerRows;
    private static final ResourceLocation CONTAINER_BACKGROUND =
            new ResourceLocation(ArchitectsSatchel.MOD_ID, "textures/gui/bundle.png");

    public SatchelGui(SatchelContainerMenu satchelGui, Inventory inventory, Component text) {
        super(satchelGui, inventory, text);
        this.passEvents = false;
        int i = 222;
        int j = 114;
        this.containerRows = 3;
        this.imageHeight = 114 + this.containerRows * 18;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int i, int j) {
        // Disable inventory label
        this.font.draw(poseStack, this.title, (float) this.titleLabelX,
                (float) this.titleLabelY + 2, /*4210752*/ 16711423);
    }

    public void renderActiveSlotOverlay(PoseStack ms, int x, int y) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }


    @Override
    protected void renderBg(@NotNull PoseStack poseStack, float f, int i, int j) {}

    protected void renderBack(PoseStack p_98413_, float p_98414_, int p_98415_, int p_98416_) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, CONTAINER_BACKGROUND);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(p_98413_, i, j, 0, 0, this.imageWidth, containerRows * 18 + 17);
        this.blit(p_98413_, i, j + containerRows * 18 + 17, 0, 126, this.imageWidth, 96);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        this.renderBack(matrixStack, partialTicks, mouseX, mouseY);
        this.renderActiveSlotOverlay(matrixStack, 0, 0);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    public void removed() {
        super.removed();
        Minecraft.getInstance().keyboardHandler.setSendRepeatsToGui(false);
    }

    @Override
    public void init() {
        super.init();
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }
}

