package com.nanianjinxiu.creepertv.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.level.Level;

public class Veper extends Vex {
    private static final EntityDataAccessor<Integer> DATA_SWELL =
            SynchedEntityData.defineId(Veper.class, EntityDataSerializers.INT);
    boolean swellDir = false;
    private final int MAX_SWELL = 30;
    public Veper(EntityType<? extends Vex> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SWELL, 0);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 14.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.7D)
                .add(Attributes.FLYING_SPEED, 0.4D)
                .add(Attributes.ATTACK_DAMAGE, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 16.0D);
    }

    @Override
    public void tick(){
        super.tick();
        if (this.level().isClientSide) return;
        LivingEntity target = this.getTarget();
        if (target != null) {
            if (this.closerThan(target, 3.0D)) swellDir = true;
            else if (!this.closerThan(target, 3.0D)) swellDir = false;
        }
        int swell = this.entityData.get(DATA_SWELL);
        if (swellDir) swell++;
        else if (!swellDir && swell > 0) swell--;
        this.entityData.set(DATA_SWELL, swell);
        if (swell >= MAX_SWELL) {
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 2.0F, Level.ExplosionInteraction.MOB);
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
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
    }

    public float getSwelling(float partialTicks) {
        return this.entityData.get(DATA_SWELL) / (float) MAX_SWELL;
    }
}
