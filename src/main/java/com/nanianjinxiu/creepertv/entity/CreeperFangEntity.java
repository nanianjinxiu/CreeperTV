package com.nanianjinxiu.creepertv.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;

import java.util.List;

public class CreeperFangEntity extends Entity {

    private static final int GROW_TIME = 10;

    private static final EntityDataAccessor<Integer> DATA_DELAY =
            SynchedEntityData.defineId(CreeperFangEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_GROW =
            SynchedEntityData.defineId(CreeperFangEntity.class, EntityDataSerializers.INT);

    private int aliveTime;
    private boolean exploded;
    private boolean playedPrimedSound;

    public CreeperFangEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public CreeperFangEntity(Level level, double x, double y, double z, float yaw, int delay) {
        this(ModEntities.CREEPER_FANG.get(), level);
        this.setPos(x, y, z);
        this.setYRot(yaw);
        this.entityData.set(DATA_DELAY, Math.max(0, delay));
        this.entityData.set(DATA_GROW, 0);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_DELAY, 0);
        this.entityData.define(DATA_GROW, 0);
    }

    @Override
    public void tick() {
        super.tick();

        int delay = this.entityData.get(DATA_DELAY);
        int grow = this.entityData.get(DATA_GROW);

        if (delay > 0) {
            this.entityData.set(DATA_DELAY, delay - 1);
            return;
        }

        if (grow < GROW_TIME) {
            this.entityData.set(DATA_GROW, grow + 1);
            return;
        }

        // 刚长满：播滋滋声
        if (!playedPrimedSound) {
            playedPrimedSound = true;
            if (!this.level().isClientSide) {
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                        SoundEvents.CREEPER_PRIMED, SoundSource.HOSTILE, 1.0F, 1.0F);
            }
        }

        if (this.level().isClientSide) return;
        if (exploded) return;

        List<LivingEntity> hits = this.level().getEntitiesOfClass(
                LivingEntity.class,
                this.getBoundingBox().inflate(0.5D),
                e -> e.isAlive() && !(e instanceof Raider) && !(e instanceof Vex)
        );

        if (!hits.isEmpty()) {
            exploded = true;
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 2.0F, Level.ExplosionInteraction.MOB);
            this.discard();
            return;
        }

        aliveTime++;
        if (aliveTime > 25) {
            if (this.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.POOF,
                        this.getX(), this.getY() + 0.3D, this.getZ(),
                        20, 0.3D, 0.4D, 0.3D, 0.02D);
                serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE,
                        this.getX(), this.getY() + 0.8D, this.getZ(),
                        10, 0.2D, 0.5D, 0.2D, 0.01D);
            }
            this.discard();
        }
    }

    public boolean isFullyGrown() {
        return this.entityData.get(DATA_DELAY) <= 0 && this.entityData.get(DATA_GROW) >= GROW_TIME;
    }

    public float getWarmupProgress(float partialTicks) {
        if (this.entityData.get(DATA_DELAY) > 0) return 0.0F;
        int grow = this.entityData.get(DATA_GROW);
        if (grow >= GROW_TIME) return 1.0F;
        return (grow + partialTicks) / GROW_TIME;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.entityData.set(DATA_DELAY, tag.getInt("Delay"));
        this.entityData.set(DATA_GROW, tag.getInt("Grow"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Delay", this.entityData.get(DATA_DELAY));
        tag.putInt("Grow", this.entityData.get(DATA_GROW));
    }
}