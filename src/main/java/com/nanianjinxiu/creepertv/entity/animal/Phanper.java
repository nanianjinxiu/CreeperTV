package com.nanianjinxiu.creepertv.entity.animal;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Phantom;

import net.minecraft.world.level.Level;

import java.lang.reflect.Field;

public class Phanper extends net.minecraft.world.entity.monster.Phantom {
    private static final EntityDataAccessor<Integer> DATA_SWELL =
            SynchedEntityData.defineId(Phanper.class, EntityDataSerializers.INT);
    private final int MAX_SWELL = 25;
    boolean swellDir = false;

    public Phanper(EntityType<? extends Phantom> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    private static final Field ATTACK_PHASE_FIELD;
    static {
        Field found = null;
        for (Field f : Phantom.class.getDeclaredFields()) {
            if (f.getType().getName().endsWith("AttackPhase")) {
                f.setAccessible(true);
                found = f;
                break;
            }
        }
        ATTACK_PHASE_FIELD = found;
    }

    private boolean isSwooping() {
        if (ATTACK_PHASE_FIELD == null) return false;
        try {
            Object phase = ATTACK_PHASE_FIELD.get(this);
            return phase != null && phase.toString().equals("SWOOP");
        } catch (IllegalAccessException e) {
            return false;
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SWELL, 0);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.FLYING_SPEED, 0.4D)
                .add(Attributes.ATTACK_DAMAGE, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 64.0D);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) return;
        LivingEntity target = this.getTarget();
        double speed = this.getDeltaMovement().length();
        // 判定是否安拉
        if (target != null) {
            boolean swooping = this.isSwooping();
            if (swooping && this.closerThan(target, 4.0D)) swellDir = true;
            else if (!swooping && !this.closerThan(target, 2.0D)) swellDir = false;
        }
        int swell = this.entityData.get(DATA_SWELL);
        if (swellDir && !(speed > 0.4)) swell++;
        else if (swellDir && speed > 0.4) swell+=2;
        else if (!swellDir && swell > 0) swell--;
        this.entityData.set(DATA_SWELL, swell);
        if (swell >= MAX_SWELL) {
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 3.0F, Level.ExplosionInteraction.MOB);
            this.discard();
        }
        if (swell == 1) {
            this.level().playSound(
                    null,
                    this.getX(), this.getY(), this.getZ(),
                    SoundEvents.CREEPER_PRIMED,
                    SoundSource.HOSTILE,
                    1.0F,
                    1.0F
            );
        }
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
    }

    // 归一化膨胀值，方便缩放
    public float getSwelling(float partialTicks) {
        return this.entityData.get(DATA_SWELL) / (float) MAX_SWELL;
    }
}