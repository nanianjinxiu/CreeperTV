package com.nanianjinxiu.creepertv.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.nanianjinxiu.creepertv.entity.animal.IronGolper;
import net.minecraft.client.model.IronGolemModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class IronGolperTNTLayer extends RenderLayer<IronGolper, IronGolemModel<IronGolper>> {
    private final BlockRenderDispatcher blockRenderer;

    public IronGolperTNTLayer(RenderLayerParent<IronGolper, IronGolemModel<IronGolper>> pRenderer, BlockRenderDispatcher pBlockRenderer) {
        super(pRenderer);
        this.blockRenderer = pBlockRenderer;
    }

    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, IronGolper pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (pLivingEntity.getOfferTNTTick() != 0) {
            pPoseStack.pushPose();
            ModelPart modelpart = this.getParentModel().getFlowerHoldingArm();
            modelpart.translateAndRotate(pPoseStack);
            pPoseStack.translate(-1.1875F, 1.0F, -0.8125F);
            pPoseStack.translate(0.5F, 0.5F, 0.5F);
            pPoseStack.scale(0.245F, 0.245F, 0.245F);
            pPoseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
            pPoseStack.translate(-0.5F, -0.5F, -0.5F);
            this.blockRenderer.renderSingleBlock(Blocks.TNT.defaultBlockState(), pPoseStack, pBuffer, pPackedLight, OverlayTexture.NO_OVERLAY);
            pPoseStack.popPose();
        }
    }
}