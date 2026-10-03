package com.nanianjinxiu.creepertv.aicode.aivillagerboomer;

import com.nanianjinxiu.creepertv.aicode.aivillagerboomer2.VillagerBoomerState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityEventMixin {

    @Inject(method = "handleEntityEvent", at = @At("HEAD"))
    private void onHandleEvent(byte id, CallbackInfo ci) {
        if (id != 100) return;
        Entity self = (Entity) (Object) this;
        if (self instanceof Villager v && v.level().isClientSide()) {
            VillagerBoomerState.flash(v.getUUID());
        }
    }
}