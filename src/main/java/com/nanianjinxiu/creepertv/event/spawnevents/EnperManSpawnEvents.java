package com.nanianjinxiu.creepertv.event.spawnevents;

import com.nanianjinxiu.creepertv.CreeperTV;
import com.nanianjinxiu.creepertv.entity.ModEntities;
import com.nanianjinxiu.creepertv.entity.animal.EnperMan;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CreeperTV.MODID)
public class EnperManSpawnEvents {
    public static void onFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event) {
        if (event.getLevel().isClientSide()) return;
        //限定自然生成，好像没有方法能只监听自然生成事件，别忘记了啊，别一个坑死三次
        if (event.getSpawnType() != MobSpawnType.NATURAL) return;
        if (event.getEntity() instanceof EnderMan) {
            if(!(event.getLevel().getLevel().dimension() == Level.END)) {
                if (Math.random() < 0.05F) {
                    event.setSpawnCancelled(true);
                    EnperMan enperMan = ModEntities.ENPER_MAN.get().create(event.getLevel().getLevel());
                    Entity original = event.getEntity();
                    if (enperMan != null) {
                        enperMan.moveTo(original.getX(), original.getY(), original.getZ(),
                                original.getYRot(), original.getXRot());
                        event.getLevel().getLevel().addFreshEntity(enperMan);
                    }
                }
            }
            else if (Math.random() < 0.005F) {
                event.setSpawnCancelled(true);
                EnperMan enperMan = ModEntities.ENPER_MAN.get().create(event.getLevel().getLevel());
                Entity original = event.getEntity();
                if (enperMan != null) {
                    enperMan.moveTo(original.getX(), original.getY(), original.getZ(),
                            original.getYRot(), original.getXRot());
                    event.getLevel().getLevel().addFreshEntity(enperMan);
                }
            }
        }
    }
}
