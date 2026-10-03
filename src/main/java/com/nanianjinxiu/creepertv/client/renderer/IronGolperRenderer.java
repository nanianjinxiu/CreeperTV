package com.nanianjinxiu.creepertv.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.nanianjinxiu.creepertv.CreeperTV;
import com.nanianjinxiu.creepertv.client.renderer.layers.IronGolperTNTLayer;
import com.nanianjinxiu.creepertv.entity.animal.IronGolper;
import net.minecraft.client.model.IronGolemModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class IronGolperRenderer extends MobRenderer<IronGolper, IronGolemModel<IronGolper>> {
    private static final ResourceLocation GOLEM_LOCATION = new ResourceLocation(CreeperTV.MODID, "textures/entity/iron_golper.png");

    public IronGolperRenderer(EntityRendererProvider.Context context) {
        super(context, new IronGolemModel<>(context.bakeLayer(ModelLayers.IRON_GOLEM)), 0.7F);
        this.addLayer(new IronGolperTNTLayer(this, context.getBlockRenderDispatcher()));
    }

    @Override
    public ResourceLocation getTextureLocation(IronGolper entity) {
        return GOLEM_LOCATION;
    }

    @Override
    protected float getWhiteOverlayProgress(IronGolper entity, float partialTicks) {
        IronGolem.Crackiness crackiness = entity.getCrackiness();

        int interval;
        switch (crackiness) {
            case LOW:
                interval = 20;
                break;
            case MEDIUM:
                interval = 10;
                break;
            case HIGH:
                interval = 5;
                break;
            default:
                return 0.0F;
        }

        int flashDuration = 2;

        int time = entity.tickCount;
        return (time % interval) < flashDuration ? 1.0F : 0.0F;
    }

    @Override
    protected void setupRotations(IronGolper entityLiving, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks) {
        super.setupRotations(entityLiving, poseStack, ageInTicks, rotationYaw, partialTicks);
        if (!((double)entityLiving.walkAnimation.speed() < 0.01D)) {
            float f = 13.0F;
            float f1 = entityLiving.walkAnimation.position(partialTicks) + 6.0F;
            float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
            poseStack.mulPose(Axis.ZP.rotationDegrees(6.5F * f2));
        }
    }
}