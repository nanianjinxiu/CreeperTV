package com.nanianjinxiu.creepertv.spawnevents.BoomerVillage;

import com.nanianjinxiu.creepertv.CreeperTV;
import net.minecraft.world.entity.npc.Villager;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CreeperTV.MODID)
public class BoomerVillagerSpawnEvents {
    @SubscribeEvent
    public static void onFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event) {
        if (event.getLevel().isClientSide()) return;
        if (!(event.getEntity() instanceof Villager)) return;
        if (VillagerHash.isBoomerVillager(event.getEntity())){
            event.getEntity().addTag("boomer");
        }
    }
}