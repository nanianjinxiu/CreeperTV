package com.nanianjinxiu.creepertv.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nanianjinxiu.creepertv.CreeperTV;
import com.nanianjinxiu.creepertv.client.model.PhanperModel;
import com.nanianjinxiu.creepertv.entity.animal.Phanper;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class PhanperRenderer extends MobRenderer<Phanper, PhanperModel<Phanper>> {

    public PhanperRenderer(EntityRendererProvider.Context context) {
        super(context, new PhanperModel<>(context.bakeLayer(PhanperModel.LAYER_LOCATION)), 0.5F);
    }

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(CreeperTV.MODID, "textures/entity/phanper.png");

    @Override
    protected float getWhiteOverlayProgress(Phanper entity, float partialTicks) {
        float swell = entity.getSwelling(partialTicks);
        return swell > 0.0F ? (int)(swell * 10.0F) % 2 == 0 ? 0.0F : swell : 0.0F;
    }

    @Override
    protected void scale(Phanper entity, PoseStack poseStack, float partialTicks) {
        float swell = entity.getSwelling(partialTicks);
        if (swell > 0) {
            float f = 1.0F + Mth.sin(swell * 100.0F) * swell * 0.01F;
            float scaleXZ = (1.0F + swell * 0.4F) * f;
            float scaleY = (1.0F + swell * 0.1F) / f;
            poseStack.scale(scaleXZ, scaleY, scaleXZ);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(Phanper entity) {
        return TEXTURE;
    }
}
