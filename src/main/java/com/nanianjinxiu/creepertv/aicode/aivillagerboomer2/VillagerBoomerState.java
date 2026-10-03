package com.nanianjinxiu.creepertv.aicode.aivillagerboomer2;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public class VillagerBoomerState {

    private static final Map<UUID, Long> FLASHES = new HashMap<>();
    private static final long FLASH_DURATION_MS = 150;

    public static void flash(UUID id) {
        FLASHES.put(id, System.currentTimeMillis());
    }

    public static boolean isFlashing(UUID id) {
        Long t = FLASHES.get(id);
        if (t == null) return false;
        return System.currentTimeMillis() - t < FLASH_DURATION_MS;
    }
}