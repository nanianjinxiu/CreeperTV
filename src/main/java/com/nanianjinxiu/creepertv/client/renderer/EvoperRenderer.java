package com.nanianjinxiu.creepertv.client.renderer;

import com.nanianjinxiu.creepertv.CreeperTV;
import com.nanianjinxiu.creepertv.entity.animal.Evoper;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EvokerRenderer;
import net.minecraft.resources.ResourceLocation;

public class EvoperRenderer extends EvokerRenderer<Evoper> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(CreeperTV.MODID, "textures/entity/evoper.png");

    public EvoperRenderer(EntityRendererProvider.Context p_174108_) {
        super(p_174108_);
    }

    @Override
    public ResourceLocation getTextureLocation(Evoper entity) {
        return TEXTURE;
    }
}