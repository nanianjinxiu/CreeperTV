package com.nanianjinxiu.creepertv.entity.animal;

import com.nanianjinxiu.creepertv.entity.ai.goal.enperman.FreezeWhenLookedAtGoal;
import com.nanianjinxiu.creepertv.entity.ai.goal.enperman.LookForPlayerGoal;
import com.nanianjinxiu.creepertv.entity.ai.goal.enperman.PlaceTNTGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class EnperMan extends EnderMan {

    public EnperMan(EntityType<? extends EnderMan> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new FreezeWhenLookedAtGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D, 0.0F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(10, new PlaceTNTGoal(this));

        this.targetSelector.addGoal(1, new LookForPlayerGoal(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(
                this, Endermite.class, true, false));
        this.targetSelector.addGoal(4, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    public boolean isLookingAtMe(Player player) {
        ItemStack helmet = player.getInventory().armor.get(3);
        if (helmet.is(Items.CARVED_PUMPKIN)) {
            return false;
        }
        Vec3 look = player.getViewVector(1.0F).normalize();
        Vec3 toMe = new Vec3(
                this.getX() - player.getX(),
                this.getEyeY() - player.getEyeY(),
                this.getZ() - player.getZ());
        double len = toMe.length();
        toMe = toMe.normalize();
        double dot = look.dot(toMe);
        return dot > 1.0D - 0.025D / len && player.hasLineOfSight(this);
    }

    public void teleportAway() {
        for (int i = 0; i < 64; i++) {
            if (this.teleport()) return;
        }
    }

    public boolean teleportNear(Entity target) {
        for (int i = 0; i < 32; i++) {
            double angle = this.random.nextDouble() * Math.PI * 2;
            double radius = 2.0D + this.random.nextDouble() * 2.0D;
            double x = target.getX() + Math.cos(angle) * radius;
            double y = target.getY();
            double z = target.getZ() + Math.sin(angle) * radius;
            if (this.enperTeleport(x, y, z)) return true;
        }
        return false;
    }

    private boolean enperTeleport(double x, double y, double z) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, y, z);
        while (pos.getY() > this.level().getMinBuildHeight()
                && !this.level().getBlockState(pos).blocksMotion()) {
            pos.move(Direction.DOWN);
        }
        BlockState state = this.level().getBlockState(pos);
        if (!state.blocksMotion()) return false;
        if (state.getFluidState().is(FluidTags.WATER)) return false;

        Vec3 old = this.position();
        boolean ok = this.randomTeleport(x, y, z, true);
        if (ok) {
            this.level().gameEvent(GameEvent.TELEPORT, old, GameEvent.Context.of(this));
            if (!this.isSilent()) {
                this.level().playSound(null, this.xo, this.yo, this.zo,
                        SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
                this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
            }
        }
        return ok;
    }
}