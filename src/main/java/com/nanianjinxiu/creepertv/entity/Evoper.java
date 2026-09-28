package com.nanianjinxiu.creepertv.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;

public class Evoper extends Evoker {

    public Evoper(EntityType<? extends Evoker> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 24.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.5D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.FOLLOW_RANGE, 12.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SummonSilverfishGoal());
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 0.6D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Mob.class, 8.0F));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Raider.class).setAlertOthers(Raider.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false));
    }

    public boolean isCastingSpell() {
        return super.isCastingSpell();
    }

    public AbstractIllager.IllagerArmPose getArmPose() {
        return super.getArmPose();
    }

    //继承原版施法基类
    class SummonSilverfishGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {

        @Override
        protected void performSpellCasting() {
            Evoper evoper = Evoper.this;
            if (!(evoper.level() instanceof ServerLevel serverLevel)) return;

            int count = 2 + evoper.getRandom().nextInt(3);
            for (int i = 0; i < count; i++) {
                Silverfish silverfish = EntityType.SILVERFISH.create(serverLevel);
                if (silverfish != null) {
                    double x = evoper.getX() + (evoper.getRandom().nextDouble() - 0.5D) * 4.0D;
                    double y = evoper.getY();
                    double z = evoper.getZ() + (evoper.getRandom().nextDouble() - 0.5D) * 4.0D;
                    silverfish.moveTo(x, y, z, evoper.getYRot(), 0.0F);
                    serverLevel.addFreshEntity(silverfish);
                }
            }
        }

        @Override
        protected int getCastWarmupTime() {
            return 100;
        }

        @Override
        protected int getCastingTime() {
            return 20;
        }

        @Override
        protected int getCastingInterval() {
            return 340;
        }

        @Override
        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        @Override
        protected SpellcasterIllager.IllagerSpell getSpell() {
            return SpellcasterIllager.IllagerSpell.SUMMON_VEX;
        }
    }
}