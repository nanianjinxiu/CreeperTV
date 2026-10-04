package com.nanianjinxiu.creepertv.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nanianjinxiu.creepertv.client.renderer.layers.EnpermanEyesLayer;
import com.nanianjinxiu.creepertv.client.renderer.layers.EnpermanTntLayer;
import com.nanianjinxiu.creepertv.entity.animal.EnperMan;
import net.minecraft.client.model.EndermanModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class EnpermanRenderer extends MobRenderer<EnperMan, EndermanModel<EnperMan>> {
    private static final ResourceLocation ENDERMAN_LOCATION = new ResourceLocation("textures/entity/enderman/enderman.png");

    public EnpermanRenderer(EntityRendererProvider.Context context) {
        super(context, new EndermanModel<>(context.bakeLayer(ModelLayers.ENDERMAN)), 0.5F);
        this.addLayer(new EnpermanEyesLayer<>(this));
        this.addLayer(new EnpermanTntLayer(this, context.getBlockRenderDispatcher()));
    }

    @Override
    public void render(EnperMan pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        EndermanModel<EnperMan> endermanmodel = this.getModel();
        endermanmodel.carrying = true;
        endermanmodel.creepy = pEntity.isCreepy();
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(EnperMan entity) {
        return ENDERMAN_LOCATION;
    }
}