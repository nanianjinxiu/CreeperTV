package com.nanianjinxiu.creepertv.entity.ai.goal.irongolper;

import java.util.EnumSet;
import com.nanianjinxiu.creepertv.entity.animal.IronGolper;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;

public class OfferTNTGoal extends Goal {
    private static final TargetingConditions OFFER_TARGET_CONTEXT = TargetingConditions.forNonCombat().range(6.0D);
    public static final int OFFER_TICKS = 400;
    private final IronGolper golem;
    private LivingEntity target;
    private int tick;

    public OfferTNTGoal(IronGolper golem) {
        this.golem = golem;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.golem.getRandom().nextInt(300) != 0) {
            return false;
        }

        Player player = this.golem.level().getNearestEntity(
                Player.class,
                OFFER_TARGET_CONTEXT,
                this.golem,
                this.golem.getX(),
                this.golem.getY(),
                this.golem.getZ(),
                this.golem.getBoundingBox().inflate(6.0D, 2.0D, 6.0D)
        );
        if (player != null) {
            this.target = player;
            return true;
        }

        if (this.golem.getRandom().nextInt(50) != 0) {
            return false;
        }

        Villager villager = this.golem.level().getNearestEntity(
                Villager.class,
                OFFER_TARGET_CONTEXT,
                this.golem,
                this.golem.getX(),
                this.golem.getY(),
                this.golem.getZ(),
                this.golem.getBoundingBox().inflate(6.0D, 2.0D, 6.0D)
        );
        if (villager != null) {
            this.target = villager;
            return true;
        }

        return false;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.tick <= 0) return false;
        if (this.target == null || !this.target.isAlive()) return false;
        if (this.golem.distanceToSqr(this.target) > 36.0D) return false;
        if (this.golem.getOfferTNTTick() <= 0) return false;
        return true;
    }

    @Override
    public void start() {
        this.tick = this.adjustedTickDelay(OFFER_TICKS);
        this.golem.offerTNT(true);
    }

    @Override
    public void stop() {
        this.golem.offerTNT(false);
        this.golem.getNavigation().stop();
        this.target = null;
    }

    @Override
    public void tick() {
        this.golem.getLookControl().setLookAt(this.target, 30.0F, 30.0F);

        // 身体朝向也转过去
        double dx = this.target.getX() - this.golem.getX();
        double dz = this.target.getZ() - this.golem.getZ();
        float targetYaw = (float)(Mth.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0F;
        this.golem.setYRot(targetYaw);
        this.golem.yBodyRot = targetYaw;
        this.golem.yHeadRot = targetYaw;

        if (this.golem.distanceToSqr(this.target) > 4.0D) {
            if (this.tick % 10 == 0) {
                this.golem.getNavigation().moveTo(this.target, 0.6D);
            }
        } else {
            this.golem.getNavigation().stop();
        }

        --this.tick;
    }
}