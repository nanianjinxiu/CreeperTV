package com.nanianjinxiu.creepertv.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nanianjinxiu.creepertv.CreeperTV;
import com.nanianjinxiu.creepertv.entity.Evoper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class EvoperRenderer extends MobRenderer<Evoper, EvoperModel<Evoper>> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(CreeperTV.MODID, "textures/entity/evoper.png");

    public EvoperRenderer(EntityRendererProvider.Context context) {
        super(context, new EvoperModel<>(context.bakeLayer(EvoperModel.LAYER_LOCATION)), 0.5F);

        this.addLayer(new ItemInHandLayer<Evoper, EvoperModel<Evoper>>(this, context.getItemInHandRenderer()) {
            @Override
            public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                               Evoper entity, float limbSwing, float limbSwingAmount,
                               float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                // 只在施法时渲染手持物品，和原版 EvokerRenderer 一样
                if (entity.isCastingSpell()) {
                    super.render(poseStack, buffer, packedLight, entity, limbSwing, limbSwingAmount,
                            partialTicks, ageInTicks, netHeadYaw, headPitch);
                }
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(Evoper entity) {
        return TEXTURE;
    }
}