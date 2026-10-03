package com.nanianjinxiu.creepertv.spawnevents;

import com.nanianjinxiu.creepertv.CreeperTV;
import com.nanianjinxiu.creepertv.entity.ModEntities;
import com.nanianjinxiu.creepertv.entity.animal.Phanper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CreeperTV.MODID)
public class PhantomSpawnEvents {

    @SubscribeEvent
    public static void onFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event) {
        if (event.getLevel().isClientSide()) return;
        //限定自然生成，好像没有方法能只监听自然生成事件，别忘记了啊，别一个坑死三次
        if (event.getSpawnType() != MobSpawnType.NATURAL) return;
        if (event.getEntity() instanceof Phantom) {
            if (Math.random() < 0.1F) {
                event.setSpawnCancelled(true);
                Phanper phanper = ModEntities.PHANPER.get().create(event.getLevel().getLevel());
                Entity original = event.getEntity();
                if (phanper != null) {
                    phanper.moveTo(original.getX(), original.getY(), original.getZ(),
                            original.getYRot(), original.getXRot());
                    event.getLevel().getLevel().addFreshEntity(phanper);
                }
            }
        }
    }
}
