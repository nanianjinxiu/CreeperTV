package com.nanianjinxiu.creepertv.event.spawnevents;

import com.nanianjinxiu.creepertv.CreeperTV;
import com.nanianjinxiu.creepertv.entity.ModEntities;
import com.nanianjinxiu.creepertv.entity.animal.MinecartWithTNT;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CreeperTV.MODID)
public class MinecartWithTNTSpawnEvents {
    @SubscribeEvent
    public static void onFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event) {
        if (event.getLevel().isClientSide()) return;
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        if (event.getSpawnType() != MobSpawnType.NATURAL) return;
        Entity original = event.getEntity();
        MinecartWithTNT minecartWithTNT= ModEntities.MINECART_WITH_TNT.get().create(event.getLevel().getLevel());
        if (!isInMineshaft(level, original.blockPosition())) return;
        if (Math.random() < 0.05F) {
            event.setSpawnCancelled(true);
            if (minecartWithTNT != null) {
                minecartWithTNT.moveTo(original.getX(), original.getY(), original.getZ(),
                        original.getYRot(), original.getXRot());
                event.getLevel().getLevel().addFreshEntity(minecartWithTNT);
            }
        }
    }

    public static boolean isInMineshaft(ServerLevel level, BlockPos pos) {
        Structure mineshaft = level.registryAccess()
                .registryOrThrow(Registries.STRUCTURE)
                .getHolderOrThrow(BuiltinStructures.MINESHAFT)
                .value();
        StructureStart start = level.structureManager()
                .getStructureWithPieceAt(pos, mineshaft);
        return start != null && start.isValid();
    }
}
