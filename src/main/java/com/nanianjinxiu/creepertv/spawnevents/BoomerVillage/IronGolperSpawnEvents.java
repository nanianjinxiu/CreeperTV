package com.nanianjinxiu.creepertv.spawnevents.BoomerVillage;

import com.nanianjinxiu.creepertv.CreeperTV;
import com.nanianjinxiu.creepertv.entity.ModEntities;

import com.nanianjinxiu.creepertv.entity.animal.IronGolper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CreeperTV.MODID)
public class IronGolperSpawnEvents {
    @SubscribeEvent
    public static void onFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event) {
        if (event.getLevel().isClientSide()) return;
        if (!(event.getEntity() instanceof IronGolem)) return;
        if (VillagerHash.isBoomerVillager(event.getEntity())) {
            event.setSpawnCancelled(true);
            IronGolper ironGolper = ModEntities.IRON_GOLPER.get().create(event.getLevel().getLevel());
            Entity original = event.getEntity();
            if (ironGolper != null) {
                ironGolper.moveTo(original.getX(), original.getY(), original.getZ(),
                        original.getYRot(), original.getXRot());
                event.getLevel().getLevel().addFreshEntity(ironGolper);
            }
        }
    }
}
