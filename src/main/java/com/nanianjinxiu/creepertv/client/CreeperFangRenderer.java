package com.nanianjinxiu.creepertv.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.nanianjinxiu.creepertv.entity.CreeperFangEntity;
import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

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

        Player player = entity.level().getNearestPlayer(entity, 16.0D);

        poseStack.pushPose();

        // 让整个模型绕 Y 轴朝向玩家
        if (player != null) {
            double dx = player.getX() - entity.getX();
            double dz = player.getZ() - entity.getZ();
            float bodyYaw = (float) (Mth.atan2(-dx, dz) * (180.0 / Math.PI));
            poseStack.mulPose(Axis.YP.rotationDegrees(bodyYaw));
        }

        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.scale(1.0F, progress, 1.0F);
        poseStack.translate(0.0F, -1.5F, 0.0F);

        this.model.setupAnim(entity, 0, 0, 0, 0, 0);
        this.model.renderToBuffer(poseStack,
                buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity))),
                packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(CreeperFangEntity entity) {
        return TEXTURE;
    }
}