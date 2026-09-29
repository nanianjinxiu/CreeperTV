package com.nanianjinxiu.creepertv.entity;

import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
            if (this.closerThan(target, 6.0D)) swellDir = true;
            else if (!this.closerThan(target, 6.0D)) swellDir = false;
        }
        int swell = this.entityData.get(DATA_SWELL);
        if (swellDir) swell++;
        else if (!swellDir && swell > 0) swell--;
        this.entityData.set(DATA_SWELL, swell);
        if (swell >= MAX_SWELL) {
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 3.0F, Level.ExplosionInteraction.MOB);
            this.discard();
        }
        if (swell == 1) {  // swell 刚从 0 变成 1，说明刚开始膨胀
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
        super.dropCustomDeathLoot(source, looting, recentlyHit);

        int gunpowderCount = 1 + this.random.nextInt(2) + looting;
        this.spawnAtLocation(new ItemStack(Items.GUNPOWDER, gunpowderCount));
    }

    public float getSwelling(float partialTicks) {
        return this.entityData.get(DATA_SWELL) / (float) MAX_SWELL;
    }
}
