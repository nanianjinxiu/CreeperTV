package com.nanianjinxiu.creepertv.entity.ai.goal.enperman;

import com.nanianjinxiu.creepertv.entity.animal.EnperMan;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class FreezeWhenLookedAtGoal extends Goal {

    private final EnperMan enderman;
    @Nullable private Player target;

    public FreezeWhenLookedAtGoal(EnperMan enderman) {
        this.enderman = enderman;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        Player p = this.enderman.level().getNearestPlayer(this.enderman, 16.0D);
        if (p == null) return false;
        if (p.isCreative() || p.isSpectator()) return false;
        if (!this.enderman.isLookingAtMe(p)) return false;
        this.target = p;
        return true;
    }

    @Override
    public void start() {
        this.enderman.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (this.target == null) return;

        // 强制让身体 + 头都朝向玩家
        double dx = this.target.getX() - this.enderman.getX();
        double dz = this.target.getZ() - this.enderman.getZ();
        float yaw = (float)(Mth.atan2(dz, dx) * 180.0D / Math.PI) - 90.0F;

        this.enderman.setYRot(yaw);
        this.enderman.yHeadRot = yaw;
        this.enderman.yBodyRot = yaw;
        this.enderman.yHeadRotO = yaw;
        this.enderman.yBodyRotO = yaw;

        // 头的俯仰（上下看）
        this.enderman.getLookControl().setLookAt(
                this.target.getX(), this.target.getEyeY(), this.target.getZ());
    }
}