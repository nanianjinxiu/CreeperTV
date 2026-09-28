package com.nanianjinxiu.creepertv.spawnevents;

import com.nanianjinxiu.creepertv.CreeperTV;
import com.nanianjinxiu.creepertv.entity.Evoper;
import com.nanianjinxiu.creepertv.entity.ModEntities;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CreeperTV.MODID)
public class EvoperSpawnEvents {
    @SubscribeEvent
    public static void onFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event) {
        if (event.getLevel().isClientSide()) return;
        //限定自然生成，好像没有方法能只监听自然生成事件，别忘记了啊，别一个坑死三次
        if (event.getSpawnType() != MobSpawnType.NATURAL) return;
        if (event.getEntity() instanceof Evoker) {
            if (Math.random() < 0.05F) {
                event.setSpawnCancelled(true);
                Evoper evoper = ModEntities.EVOPER.get().create(event.getLevel().getLevel());
                if (evoper != null) {
                    evoper.moveTo(event.getX(), event.getY(), event.getZ(), 0.0F, 0.0F);
                    event.getLevel().getLevel().addFreshEntity(evoper);
                }
            }
        }
    }
}
