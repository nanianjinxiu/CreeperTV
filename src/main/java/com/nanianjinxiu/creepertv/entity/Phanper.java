package com.nanianjinxiu.creepertv.entity;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class Phanper extends net.minecraft.world.entity.monster.Phantom {
    private static final EntityDataAccessor<Integer> DATA_SWELL =
            SynchedEntityData.defineId(Phanper.class, EntityDataSerializers.INT);
    private final int MAX_SWELL = 30;
    boolean swellDir = false;

    public Phanper(EntityType<? extends Phantom> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
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
        // 判定是否安拉
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
                    null,                          // null 表示附近所有玩家都能听到
                    this.getX(), this.getY(), this.getZ(),  // 播放位置
                    SoundEvents.CREEPER_PRIMED,    // 苦力怕的滋滋声
                    SoundSource.HOSTILE,           // 音效类别（敌对生物）
                    1.0F,                          // 音量
                    1.0F                           // 音高
            );
        }
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
        super.dropCustomDeathLoot(source, looting, recentlyHit);

        int gunpowderCount = 1 + this.random.nextInt(3) + looting;
        this.spawnAtLocation(new ItemStack(Items.GUNPOWDER, gunpowderCount));

        int membraneCount = 1 + this.random.nextInt(2) + looting;
        this.spawnAtLocation(new ItemStack(Items.PHANTOM_MEMBRANE, membraneCount));
    }
    // 归一化膨胀值，方便缩放
    public float getSwelling(float partialTicks) {
        return this.entityData.get(DATA_SWELL) / (float) MAX_SWELL;
    }
}