package com.nanianjinxiu.creepertv.entity.ai.goal.enperman;

import com.nanianjinxiu.creepertv.entity.animal.EnperMan;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class LookForPlayerGoal extends Goal {

    private final EnperMan enderman;
    @Nullable private Player target;

    private int aggroTime;
    private int placeDelay;
    private int cooldown;
    private boolean locked;

    private static final int AGGRO_TIME = 5;
    private static final int ACTION_DELAY = 8;
    private static final int COOLDOWN = 67;

    public LookForPlayerGoal(EnperMan enderman) {
        this.enderman = enderman;
        this.setFlags(EnumSet.of(Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.cooldown > 0) {
            this.cooldown--;
            return false;
        }
        Player p = this.enderman.level().getNearestPlayer(this.enderman, 24.0D);
        if (p == null) return false;
        if (p.isCreative() || p.isSpectator()) return false;
        if (!this.enderman.isLookingAtMe(p)) return false;
        this.target = p;
        return true;
    }

    @Override
    public void start() {
        this.aggroTime = AGGRO_TIME;
        this.placeDelay = -1;
        this.locked = false;
        this.enderman.setBeingStaredAt();
    }

    @Override
    public boolean canContinueToUse() {
        if (this.target == null) return false;
        if (!this.target.isAlive()) return false;
        if (this.locked) return true;
        return this.enderman.isLookingAtMe(this.target);
    }

    @Override
    public void tick() {
        if (this.target == null) return;

        // 每 tick 锁定视线到玩家
        this.enderman.getLookControl().setLookAt(
                this.target.getX(), this.target.getEyeY(), this.target.getZ());

        if (!this.locked) {
            this.tickObserving();
        } else {
            this.tickPlacing();
        }
    }

    private void tickObserving() {
        if (--this.aggroTime > 0) return;
        if (!this.enderman.teleportNear(this.target)) {
            this.aggroTime = AGGRO_TIME;
            return;
        }
        this.locked = true;
        this.placeDelay = ACTION_DELAY;
    }

    private void tickPlacing() {
        if (--this.placeDelay > 0) return;
        this.placeTNTNear(this.target);
        this.enderman.teleportAway();
        this.cooldown = COOLDOWN;
        this.target = null;
    }

    @Override
    public void stop() {
        this.target = null;
        this.locked = false;
        this.aggroTime = 0;
        this.placeDelay = -1;
    }

    private void placeTNTNear(Player player) {
        RandomSource rnd = this.enderman.getRandom();
        Level level = this.enderman.level();

        for (int attempt = 0; attempt < 8; attempt++) {
            int x = Mth.floor(player.getX() - 1.0D + rnd.nextDouble() * 3.0D);
            int y = Mth.floor(player.getY() + rnd.nextDouble() * 2.0D);
            int z = Mth.floor(player.getZ() - 1.0D + rnd.nextDouble() * 3.0D);

            BlockPos pos = new BlockPos(x, y, z);
            BlockPos below = pos.below();
            BlockState state = level.getBlockState(pos);
            BlockState belowState = level.getBlockState(below);

            if (!state.isAir()) continue;
            if (belowState.isAir()) continue;
            if (belowState.is(Blocks.BEDROCK)) continue;
            if (!belowState.isCollisionShapeFullBlock(level, below)) continue;

            PrimedTnt tnt = new PrimedTnt(level, x + 0.5D, y, z + 0.5D, this.enderman);
            tnt.setFuse(80);
            level.addFreshEntity(tnt);

            level.playSound(null, x + 0.5D, y, z + 0.5D,
                    SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
            return;
        }
    }
}