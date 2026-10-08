package com.nanianjinxiu.creepertv.mixin.aicode.aivillagerboomermixin;

import com.nanianjinxiu.creepertv.aicode.aivillagerboomer.VillagerFlashLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerRenderer.class)
public class VillagerRendererMixin {

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(EntityRendererProvider.Context ctx, CallbackInfo ci) {
        LivingEntityRendererAccessor acc = (LivingEntityRendererAccessor) (Object) this;
        acc.getLayers().add(new VillagerFlashLayer((RenderLayerParent) (Object) this));
    }
}