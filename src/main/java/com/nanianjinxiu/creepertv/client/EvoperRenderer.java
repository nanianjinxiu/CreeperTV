package com.nanianjinxiu.creepertv.client;

import com.nanianjinxiu.creepertv.CreeperTV;
import com.nanianjinxiu.creepertv.entity.Evoper;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.resources.ResourceLocation;

public class EvoperRenderer extends MobRenderer<Evoper, EvoperModel<Evoper>> {

    public EvoperRenderer(EntityRendererProvider.Context context) {
        super(context, new EvoperModel<>(context.bakeLayer(EvoperModel.LAYER_LOCATION)), 0.5F);
        this.addLayer(new CrossedArmsItemLayer<>(this, context.getItemInHandRenderer()));
    }

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(CreeperTV.MODID, "textures/entity/evoper.png");

    @Override
    public ResourceLocation getTextureLocation(Evoper entity) {
        return TEXTURE;
    }
}
