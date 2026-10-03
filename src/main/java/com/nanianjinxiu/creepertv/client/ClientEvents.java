package com.nanianjinxiu.creepertv.client;

import com.nanianjinxiu.creepertv.CreeperTV;
import com.nanianjinxiu.creepertv.client.model.EvoperModel;
import com.nanianjinxiu.creepertv.client.model.PhanperModel;
import com.nanianjinxiu.creepertv.client.model.VeperModel;
import com.nanianjinxiu.creepertv.client.renderer.*;
import com.nanianjinxiu.creepertv.entity.ModEntities;
import com.nanianjinxiu.creepertv.entity.animal.IronGolper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CreeperTV.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.PHANPER.get(), PhanperRenderer::new);
        event.registerEntityRenderer(ModEntities.EVOPER.get(), EvoperRenderer::new);
        event.registerEntityRenderer(ModEntities.CREEPER_FANG.get(), CreeperFangRenderer::new);
        event.registerEntityRenderer(ModEntities.VEPER.get(), VeperRenderer::new);
        event.registerEntityRenderer(ModEntities.IRON_GOLPER.get(), IronGolperRenderer::new);
    }


    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(PhanperModel.LAYER_LOCATION, PhanperModel::createBodyLayer);
        event.registerLayerDefinition(EvoperModel.LAYER_LOCATION, EvoperModel::createBodyLayer);
        event.registerLayerDefinition(VeperModel.LAYER_LOCATION, VeperModel::createBodyLayer);
    }
}
