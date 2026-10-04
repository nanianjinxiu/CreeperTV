package com.nanianjinxiu.creepertv;

import com.nanianjinxiu.creepertv.entity.animal.*;
import com.nanianjinxiu.creepertv.entity.ModEntities;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CreeperTV.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.PHANPER.get(), Phanper.createAttributes().build());
        event.put(ModEntities.EVOPER.get(), Evoper.createAttributes().build());
        event.put(ModEntities.VEPER.get(), Veper.createAttributes().build());
        event.put(ModEntities.IRON_GOLPER.get(), IronGolper.createAttributes().build());
        event.put(ModEntities.ENPER_MAN.get(), EnperMan.createAttributes().build());
    }
}