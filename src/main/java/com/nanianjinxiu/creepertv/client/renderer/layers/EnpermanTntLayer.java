package com.nanianjinxiu.creepertv.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.nanianjinxiu.creepertv.entity.animal.EnperMan;
import net.minecraft.client.model.EndermanModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EnpermanTntLayer extends RenderLayer<EnperMan, EndermanModel<EnperMan>> {
    private final BlockRenderDispatcher blockRenderer;

    public EnpermanTntLayer(RenderLayerParent<EnperMan, EndermanModel<EnperMan>> pRenderer, BlockRenderDispatcher pBlockRenderer) {
        super(pRenderer);
        this.blockRenderer = pBlockRenderer;
    }

    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, EnperMan pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        pPoseStack.pushPose();
        pPoseStack.translate(0.0F, 0.6875F, -0.75F);
        pPoseStack.mulPose(Axis.XP.rotationDegrees(20.0F));
        pPoseStack.mulPose(Axis.YP.rotationDegrees(45.0F));
        pPoseStack.translate(0.25F, 0.1875F, 0.25F);
        float f = 0.5F;
        pPoseStack.scale(-0.5F, -0.5F, 0.5F);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        this.blockRenderer.renderSingleBlock(Blocks.TNT.defaultBlockState(), pPoseStack, pBuffer, pPackedLight, OverlayTexture.NO_OVERLAY);
        pPoseStack.popPose();
    }
}