package com.nanianjinxiu.creepertv.spawnevents.BoomerVillage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;

import java.util.Map;

public class VillagerHash {

    public static boolean isBoomerVillager(Entity entity) {
        if (entity.level() instanceof ServerLevel serverLevel) {
            BlockPos pos = entity.blockPosition();

            // 1. 获取该位置所有结构
            Map<Structure, it.unimi.dsi.fastutil.longs.LongSet> structures =
                    serverLevel.structureManager().getAllStructuresAt(pos);

            // 2. 找村庄结构
            Structure villageStructure = null;
            for (Structure structure : structures.keySet()) {
                ResourceLocation id = serverLevel.registryAccess()
                        .registryOrThrow(Registries.STRUCTURE)
                        .getKey(structure);
                if (id != null && id.getPath().startsWith("village_")) {
                    villageStructure = structure;
                    break;
                }
            }

            if (villageStructure == null) {
                return false;
            }

            // 3. 获取 StructureStart，拿包围盒中心
            StructureStart start = serverLevel.structureManager()
                    .getStructureAt(pos, villageStructure);

            if (start == null || !start.isValid()) {
                return false;
            }

            BlockPos center = start.getBoundingBox().getCenter();

            // 4. splitmix64 哈希
            long seed = ((long) center.getX() << 32) | (center.getZ() & 0xFFFFFFFFL);
            float roll = hashToFloat(seed);

            // 5. 判断沙漠：直接比较生物群系注册名
            boolean isDesert = serverLevel.getBiome(center).unwrapKey()
                    .map(key -> key.location().equals(
                            new ResourceLocation("minecraft", "desert")))
                    .orElse(false);

            float threshold = isDesert ? 1F : 0.05F;

            return roll < threshold;
        }
        return false;
    }

    private static float hashToFloat(long x) {
        x = (x ^ (x >>> 30)) * 0xbf58476d1ce4e5b9L;
        x = (x ^ (x >>> 27)) * 0x94d049bb133111ebL;
        x = x ^ (x >>> 31);
        return (x >>> 40) * 0x1.0p-24F;
    }
}