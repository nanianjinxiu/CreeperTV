package com.nanianjinxiu.creepertv.aicode.aivillagerboomer;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.VillagerPanicTrigger;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerPanicTrigger.class)
public class VillagerPanicTriggerMixin {

    @Inject(method = "start", at = @At("HEAD"), cancellable = true)
    private void onStart(ServerLevel level, Villager villager, long gameTime, CallbackInfo ci) {
        if (villager.getTags().contains("creepertv:boomer")) {
            ci.cancel();
        }
    }
}