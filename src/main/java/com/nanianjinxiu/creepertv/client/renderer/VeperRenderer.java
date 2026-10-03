package com.nanianjinxiu.creepertv.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nanianjinxiu.creepertv.CreeperTV;
import com.nanianjinxiu.creepertv.client.model.VeperModel;
import com.nanianjinxiu.creepertv.entity.animal.Veper;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class VeperRenderer extends MobRenderer<Veper, VeperModel<Veper>> {
    public VeperRenderer(EntityRendererProvider.Context context) {
        super(context, new VeperModel<>(context.bakeLayer(VeperModel.LAYER_LOCATION)), 0.5F);
    }

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(CreeperTV.MODID, "textures/entity/veper.png");

    @Override
    protected float getWhiteOverlayProgress(Veper entity, float partialTicks) {
        float swell = entity.getSwelling(partialTicks);
        return swell > 0.0F ? (int)(swell * 10.0F) % 2 == 0 ? 0.0F : swell : 0.0F;
    }

    @Override
    protected void scale(Veper entity, PoseStack poseStack, float partialTicks) {
        float swell = entity.getSwelling(partialTicks);
        if (swell > 0) {
            float f = 1.0F + Mth.sin(swell * 100.0F) * swell * 0.01F;
            float scaleXZ = (1.0F + swell * 0.4F) * f;
            float scaleY = (1.0F + swell * 0.1F) / f;
            poseStack.scale(scaleXZ, scaleY, scaleXZ);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(Veper entity) {
        return TEXTURE;
    }
}
