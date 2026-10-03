package com.nanianjinxiu.creepertv;

import com.nanianjinxiu.creepertv.entity.animal.Evoper;
import com.nanianjinxiu.creepertv.entity.ModEntities;
import com.nanianjinxiu.creepertv.entity.animal.IronGolper;
import com.nanianjinxiu.creepertv.entity.animal.Phanper;
import com.nanianjinxiu.creepertv.entity.animal.Veper;
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
    }
}