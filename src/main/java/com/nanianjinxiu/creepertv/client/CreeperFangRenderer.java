package com.nanianjinxiu.creepertv.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nanianjinxiu.creepertv.entity.CreeperFangEntity;
import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class CreeperFangRenderer extends EntityRenderer<CreeperFangEntity> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/creeper/creeper.png");

    private final CreeperModel<CreeperFangEntity> model;

    public CreeperFangRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new CreeperModel<>(context.bakeLayer(ModelLayers.CREEPER));
    }

    @Override
    public void render(CreeperFangEntity entity, float entityYaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
        float progress = entity.getWarmupProgress(partialTicks);
        if (progress <= 0.0F) return;

        // 白闪进度：跟 PhanperRenderer 一样的脉冲逻辑
        float whiteProgress = 0.0F;
        if (entity.isFullyGrown()) {
            int alive = entity.tickCount;
            whiteProgress = (alive / 5) % 2 == 0 ? 1.0F : 0.0F;
        }

        // 关键：把白闪强度编进 overlay 坐标，交给 shader 叠加白色
        int overlay = OverlayTexture.pack(OverlayTexture.u(whiteProgress), OverlayTexture.v(false));

        poseStack.pushPose();
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.scale(1.0F, progress, 1.0F);
        poseStack.translate(0.0F, -1.5F, 0.0F);
        this.model.setupAnim(entity, 0, 0, 0, 0, 0);
        this.model.renderToBuffer(poseStack,
                buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity))),
                packedLight, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(CreeperFangEntity entity) {
        return TEXTURE;
    }
}