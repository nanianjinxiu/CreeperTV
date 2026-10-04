package com.nanianjinxiu.creepertv.client.renderer.layers;

import com.nanianjinxiu.creepertv.CreeperTV;
import com.nanianjinxiu.creepertv.entity.animal.EnperMan;
import net.minecraft.client.model.EndermanModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;

public class EnpermanEyesLayer<T extends EnperMan, M extends EndermanModel<T>> extends EyesLayer<T, M> {
    private static final RenderType RENDER_TYPE = RenderType.eyes(
            ResourceLocation.fromNamespaceAndPath(CreeperTV.MODID, "textures/entity/enperman_eyes.png"));

    public EnpermanEyesLayer(RenderLayerParent<T, M> parent) {
        super(parent);
    }

    @Override
    public RenderType renderType() {
        return RENDER_TYPE;
    }
}