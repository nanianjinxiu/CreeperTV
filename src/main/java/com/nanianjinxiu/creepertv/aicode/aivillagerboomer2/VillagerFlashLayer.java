package com.nanianjinxiu.creepertv.aicode.aivillagerboomer2;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.Villager;

import java.lang.reflect.Field;

public class VillagerFlashLayer extends RenderLayer<Villager, VillagerModel<Villager>> {
    private static final ResourceLocation WHITE = new ResourceLocation("textures/misc/white.png");

    private static final Field HAT_FIELD;
    static {
        try {
            HAT_FIELD = VillagerModel.class.getDeclaredField("hat");
            HAT_FIELD.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("找不到 VillagerModel.hat", e);
        }
    }

    public VillagerFlashLayer(RenderLayerParent<Villager, VillagerModel<Villager>> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                       Villager villager, float limbSwing, float limbSwingAmount,
                       float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!VillagerBoomerState.isFlashing(villager.getUUID())) return;

        ModelPart hat;
        boolean hatWasVisible = false;
        try {
            hat = (ModelPart) HAT_FIELD.get(this.getParentModel());
            hatWasVisible = hat.visible;
            hat.visible = false;
        } catch (IllegalAccessException ignored) {
            hat = null;
        }

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(WHITE));
        this.getParentModel().renderToBuffer(poseStack, consumer, packedLight,
                OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.7F);

        if (hat != null) {
            hat.visible = hatWasVisible;
        }
    }
}