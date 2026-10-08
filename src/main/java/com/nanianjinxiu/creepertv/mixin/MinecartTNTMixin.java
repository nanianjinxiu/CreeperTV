package com.nanianjinxiu.creepertv.mixin;

import com.nanianjinxiu.creepertv.entity.animal.MinecartWithTNT;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecartTNT.class)
public class MinecartTNTMixin {
    @Inject(method = "explode(D)V", at = @At("HEAD"), cancellable = true)
    private void creepertv$preventCollisionExplode(double radius, CallbackInfo ci) {
        MinecartTNT self = (MinecartTNT) (Object) this;
        if (!(self instanceof MinecartWithTNT)) return;
        if (self.getFuse() > 0) {
            ci.cancel();
        }
    }
}