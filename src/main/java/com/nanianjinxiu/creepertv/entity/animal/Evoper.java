package com.nanianjinxiu.creepertv.entity.animal;

import com.nanianjinxiu.creepertv.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;

import java.util.EnumSet;

public class Evoper extends Evoker {

    public Evoper(EntityType<? extends Evoker> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.5D)
                .add(Attributes.FOLLOW_RANGE, 12.0D)
                .add(Attributes.MAX_HEALTH, 24.0D);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide && !super.isCastingSpell()) {
            this.setIsCastingSpell(SpellcasterIllager.IllagerSpell.NONE);
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new EvoperCastingSpellGoal());
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 8.0F, 0.6D, 1.0D));
        this.goalSelector.addGoal(4, new SummonVeperSpellGoal());
        this.goalSelector.addGoal(5, new CreeperFangsSpellGoal());
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.6D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Raider.class).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, false));
    }

    public boolean isCastingSpell() {
        return super.isCastingSpell();
    }

    public AbstractIllager.IllagerArmPose getArmPose() {
        return super.getArmPose();
    }

    class EvoperCastingSpellGoal extends Goal {

        public EvoperCastingSpellGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return Evoper.this.isCastingSpell();
        }

        @Override
        public boolean canContinueToUse() {
            return Evoper.this.isCastingSpell();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity target = Evoper.this.getTarget();
            if (target != null) {
                Evoper.this.getLookControl().setLookAt(target,
                        Evoper.this.getMaxHeadYRot(), Evoper.this.getMaxHeadXRot());
            }
        }
    }

    class SummonVeperSpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {

        private final TargetingConditions veperCountTargeting =
                TargetingConditions.forNonCombat().range(16.0D).ignoreLineOfSight().ignoreInvisibilityTesting();

        @Override
        public boolean canUse() {
            if (!super.canUse()) return false;
            int i = Evoper.this.level().getNearbyEntities(Veper.class,
                    this.veperCountTargeting, Evoper.this,
                    Evoper.this.getBoundingBox().inflate(16.0D)).size();
            return Evoper.this.random.nextInt(8) + 1 > i;
        }

        @Override
        protected int getCastWarmupTime() { return 100; }

        @Override
        protected int getCastingTime() { return 100; }

        @Override
        protected int getCastingInterval() { return 340; }

        @Override
        protected void performSpellCasting() {
            ServerLevel serverlevel = (ServerLevel) Evoper.this.level();
            for (int i = 0; i < 3; i++) {
                BlockPos blockpos = Evoper.this.blockPosition().offset(
                        -2 + Evoper.this.random.nextInt(5), 1,
                        -2 + Evoper.this.random.nextInt(5));
                Veper veper = ModEntities.VEPER.get().create(Evoper.this.level());
                if (veper != null) {
                    veper.moveTo(blockpos, 0.0F, 0.0F);
                    veper.finalizeSpawn(serverlevel,
                            Evoper.this.level().getCurrentDifficultyAt(blockpos),
                            MobSpawnType.MOB_SUMMONED, null, null);
                    veper.setOwner(Evoper.this);
                    veper.setBoundOrigin(blockpos);
                    veper.setLimitedLife(20 * (30 + Evoper.this.random.nextInt(90)));
                    serverlevel.addFreshEntityWithPassengers(veper);
                }
            }
        }

        @Override
        protected SoundEvent getSpellPrepareSound() { return SoundEvents.EVOKER_PREPARE_SUMMON; }

        @Override
        protected SpellcasterIllager.IllagerSpell getSpell() {
            return SpellcasterIllager.IllagerSpell.SUMMON_VEX;
        }
    }


    class CreeperFangsSpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {

        @Override
        protected int getCastWarmupTime() { return 40; }

        @Override
        protected int getCastingTime() { return 40; }

        @Override
        protected int getCastingInterval() { return 100; }

        @Override
        protected void performSpellCasting() {
            LivingEntity target = Evoper.this.getTarget();
            if (target == null) return;

            double startX = Evoper.this.getX();
            double startY = Evoper.this.getY();
            double startZ = Evoper.this.getZ();
            float yaw = Evoper.this.getYRot();

            double dx = target.getX() - startX;
            double dz = target.getZ() - startZ;
            double len = Math.sqrt(dx * dx + dz * dz);
            if (len < 0.1D) return;
            double nx = dx / len;
            double nz = dz / len;

            int delay = 0;
            for (int i = 0; i < 13; i++) {
                double offset = 1.0D + i * 1.0D;
                double x = startX + nx * offset;
                double z = startZ + nz * offset;
                Evoper.this.level().addFreshEntity(new CreeperFangEntity(
                        Evoper.this.level(), x, startY, z, yaw, delay));
                delay += 2;
            }
        }

        @Override
        protected SoundEvent getSpellPrepareSound() { return SoundEvents.EVOKER_PREPARE_ATTACK; }

        @Override
        protected SpellcasterIllager.IllagerSpell getSpell() {
            return SpellcasterIllager.IllagerSpell.FANGS;
        }
    }
}