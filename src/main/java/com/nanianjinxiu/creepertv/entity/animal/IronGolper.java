package com.nanianjinxiu.creepertv.entity.animal;

import com.nanianjinxiu.creepertv.entity.ai.goal.irongolper.OfferTNTGoal;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class IronGolper extends IronGolem {
    private int offerTNTTick;
    private int tntFuse = -1;

    public IronGolper(EntityType<? extends IronGolem> type, Level level) {
        super(type, level);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(DamageTypeTags.IS_EXPLOSION) || super.isInvulnerableTo(source);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new OfferTNTGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9D, 32.0F));
        this.goalSelector.addGoal(2, new MoveBackToVillageGoal(this, 0.6D, false));
        this.goalSelector.addGoal(4, new GolemRandomStrollInVillageGoal(this, 0.6D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new DefendVillageTargetGoal(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Mob.class, 5, false, false, (mob) -> {
            return mob instanceof Enemy && !(mob instanceof Creeper);
        }));
        this.targetSelector.addGoal(4, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.offerTNTTick > 0) {
            --this.offerTNTTick;
        }
        if (this.tntFuse > 0) {
            --this.tntFuse;
            if (this.tntFuse == 0) {
                this.explodeTNT();
            }
        }

        // 按血量阶段播放滋滋声
        if (!this.level().isClientSide) {
            IronGolem.Crackiness crackiness = this.getCrackiness();

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
                    interval = 0;
            }

            if (interval > 0 && this.tickCount % interval == 0) {
                this.level().playSound(
                        null,
                        this.getX(), this.getY(), this.getZ(),
                        SoundEvents.CREEPER_PRIMED,
                        SoundSource.HOSTILE,
                        0.6F,
                        1.5F
                );
            }
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 11) {
            this.offerTNTTick = 400;
        } else if (id == 34) {
            this.offerTNTTick = 0;
        } else {
            super.handleEntityEvent(id);
        }
    }

    public void offerTNT(boolean offering) {
        if (offering) {
            this.offerTNTTick = 400;
            this.level().broadcastEntityEvent(this, (byte) 11);
            this.level().playSound(
                    null,
                    this.getX(), this.getY(), this.getZ(),
                    SoundEvents.TNT_PRIMED,
                    SoundSource.HOSTILE,
                    1.0F,
                    1.0F
            );
            this.tntFuse = 40;
        } else {
            this.offerTNTTick = 0;
            this.level().broadcastEntityEvent(this, (byte) 34);
            this.tntFuse = -1;
        }
    }

    private void explodeTNT() {
        if (this.level().isClientSide) return;
        this.level().explode(
                this,
                this.getX(), this.getY(), this.getZ(),
                4.0F,
                Level.ExplosionInteraction.MOB
        );

        this.hurt(this.damageSources().explosion(null), 20.0F);

        this.offerTNT(false);
    }

    public int getOfferTNTTick() {
        return this.offerTNTTick;
    }

    @Override
    public int getOfferFlowerTick() {
        return this.offerTNTTick;
    }

    @Override
    public void die(DamageSource cause) {
        if (!this.level().isClientSide) {
            this.offerTNTTick = 0;
            this.tntFuse = -1;

            this.level().explode(
                    this,
                    this.getX(), this.getY(), this.getZ(),
                    4.0F,
                    Level.ExplosionInteraction.MOB
            );
        }
        super.die(cause);
    }


}