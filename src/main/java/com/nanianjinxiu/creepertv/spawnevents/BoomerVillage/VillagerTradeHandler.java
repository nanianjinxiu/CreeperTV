package com.nanianjinxiu.creepertv.spawnevents.BoomerVillage;

import com.nanianjinxiu.creepertv.CreeperTV;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.TradeWithVillagerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CreeperTV.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VillagerTradeHandler {

    @SubscribeEvent
    public static void onTradeWithVillager(TradeWithVillagerEvent event) {
        // 只在服务端处理
        if (event.getEntity().level().isClientSide()) return;

        AbstractVillager villager = event.getAbstractVillager();
        if (!(villager instanceof Villager)) return;

        // 检查村民是否带有我们的标签
        if (villager.getTags().contains("boomer")) {
            // 触发爆炸
            Level level = villager.level();
            level.explode(villager,
                    villager.getX(), villager.getY(), villager.getZ(),
                    3.0F,
                    Level.ExplosionInteraction.MOB);
            // 移除村民
            villager.discard();
        }
    }
}