package com.rabbitminers.buildersbundle.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.rabbitminers.buildersbundle.container.SatchelInventory;
import com.rabbitminers.buildersbundle.registry.BuildersBundleItems;
import com.rabbitminers.buildersbundle.satchel.SatchelItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {
    @Shadow public abstract void renderStatic(ItemStack itemStack, ItemTransforms.TransformType transformType, int i, int j, PoseStack poseStack, MultiBufferSource multiBufferSource, int k);

    @Shadow public abstract void render(ItemStack itemStack, ItemTransforms.TransformType transformType, boolean bl, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, BakedModel bakedModel);

    @Inject(
        method = "renderGuiItem(Lnet/minecraft/world/item/ItemStack;IILnet/minecraft/client/resources/model/BakedModel;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemTransforms$TransformType;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V",
            shift = At.Shift.BEFORE
        )
    )
    public void renderGuiItem(ItemStack stack, int i, int j, BakedModel bakedModel, CallbackInfo ci) {
        if (stack.getItem() != BuildersBundleItems.BUILDERS_BUNDLE.get())
            return;

        SatchelInventory inventory = SatchelItem.getInventory(stack);
        ItemStack selectedItem = inventory.getSelectedItem();

        if (selectedItem == ItemStack.EMPTY ||
                !(selectedItem.getItem() instanceof BlockItem blockItem))
            return;

        PoseStack localMs = new PoseStack();

        localMs.translate(-1 / 4f, -1 / 4f, 1);
        localMs.scale(.5f, .5f, .5f);

        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();

        BakedModel selectedBakedModel = Minecraft.getInstance()
                .getBlockRenderer()
                .getBlockModel(blockItem.getBlock().defaultBlockState());

        this.render(selectedItem, ItemTransforms.TransformType.GUI, false,
                localMs, bufferSource,15728880, OverlayTexture.NO_OVERLAY, selectedBakedModel);
    }
}
