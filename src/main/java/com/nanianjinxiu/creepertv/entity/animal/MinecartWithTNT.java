package com.nanianjinxiu.creepertv.entity.animal;

import com.nanianjinxiu.creepertv.entity.ai.goal.agent.MinecartAgent;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class MinecartWithTNT extends MinecartTNT {

    private static final double EXPLOSION_RADIUS = 3.0D;

    private final GroundPathNavigation navigation;
    private final MinecartAgent agent;
    private int repathCooldown = 0;
    private Vec3 lastTargetPos = Vec3.ZERO;

    public MinecartWithTNT(EntityType<? extends MinecartTNT> type, Level level) {
        super(type, level);
        this.agent = new MinecartAgent(this, level);
        this.navigation = new GroundPathNavigation(agent, level) {
            @Override
            protected boolean canUpdatePath() {
                return true;
            }
        };
    }

    @Override
    public void checkDespawn() {
        Player nearest = this.level().getNearestPlayer(this, -1.0D);
        if (nearest == null) {
            this.discard();
            return;
        }
        double d = nearest.distanceToSqr(this);
        if (d > 128.0D * 128.0D) {
            this.discard();
        } else if (d > 32.0D * 32.0D && this.level().random.nextInt(800) == 0) {
            this.discard();
        }
    }

    private boolean isOnRail() {
        BlockPos pos = this.getCurrentRailPosition();
        return this.level().getBlockState(pos).is(BlockTags.RAILS);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isRemoved()) return;
        if (this.level().isClientSide) return;

        if (this.getFuse() < 0) {
            Player p = this.level().getNearestPlayer(this, EXPLOSION_RADIUS);
            if (p != null && !p.isCreative() && !p.isSpectator()) {
                this.primeFuse();
            }
        }

        this.agent.syncFromCart();
        this.agent.targetSelector.tick();

        LivingEntity t = this.agent.getTarget();
        if (isOnRail()) {
            if (t instanceof Player target && target.isAlive() && !target.isSpectator()) {
                tickRailMovement(target);
            }
        } else {
            tickFreeMovement();
        }
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (this.level().isClientSide) return true;
        if (this.isInvulnerableTo(source)) return false;

        this.remove(RemovalReason.KILLED);
        if (this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            this.spawnAtLocation(new ItemStack(this.getDropItem()));
        }
        return true;
    }

    private void tickRailMovement(Player target) {
        double dx = target.getX() - this.getX();
        double dz = target.getZ() - this.getZ();

        Vec3 dir = new Vec3(dx, 0, dz).normalize();
        double force = this.getFuse() >= 0 ? 0.15D : 0.06D;

        Vec3 motion = this.getDeltaMovement();
        this.setDeltaMovement(
                motion.x + dir.x * force,
                motion.y,
                motion.z + dir.z * force
        );
    }

    private void tickFreeMovement() {
        LivingEntity t = this.agent.getTarget();
        if (!(t instanceof Player target) || !target.isAlive() || target.isSpectator()) {
            this.navigation.stop();
            Vec3 m = this.getDeltaMovement();
            this.setDeltaMovement(0, m.y, 0);
            return;
        }

        if (this.getFuse() >= 0) {
            this.navigation.stop();
            this.applyDirectMovement(target);
            return;
        }

        boolean needRepath = this.navigation.isDone()
                || target.position().distanceToSqr(this.lastTargetPos) > 4.0D;

        if (needRepath && this.repathCooldown-- <= 0) {
            this.repathCooldown = 10;
            this.navigation.moveTo(target, 1.0D);
            this.lastTargetPos = target.position();
        }

        this.navigation.tick();
        this.applyNavigationMovement();
    }

    private void applyNavigationMovement() {
        Path path = this.navigation.getPath();
        if (path == null || path.isDone()) return;

        Node next = path.getNextNode();
        double dx = next.x + 0.5 - this.getX();
        double dz = next.z + 0.5 - this.getZ();

        while (dx * dx + dz * dz < 0.5D) {
            path.advance();
            if (path.isDone()) return;
            next = path.getNextNode();
            dx = next.x + 0.5 - this.getX();
            dz = next.z + 0.5 - this.getZ();
        }

        Vec3 dir = new Vec3(dx, 0, dz).normalize();

        double targetSpeed = 0.1D;
        double setSpeed = targetSpeed / 0.996D;

        Vec3 motion = this.getDeltaMovement();
        this.setDeltaMovement(
                dir.x * setSpeed,
                motion.y,
                dir.z * setSpeed
        );

        if (motion.horizontalDistanceSqr() > 1.0E-4D) {
            float targetYaw = (float) (Mth.atan2(dir.z, dir.x) * (180F / Math.PI)) - 90F;
            float newYaw = Mth.approachDegrees(this.getYRot(), targetYaw, 15F);
            this.setYRot(newYaw);
            this.yRotO = newYaw;
        }
    }

    private void applyDirectMovement(Player target) {
        double dx = target.getX() - this.getX();
        double dz = target.getZ() - this.getZ();

        Vec3 dir = new Vec3(dx, 0, dz).normalize();

        double targetSpeed = 0.35D;
        double setSpeed = targetSpeed / 0.996D;

        Vec3 motion = this.getDeltaMovement();
        this.setDeltaMovement(
                dir.x * setSpeed,
                motion.y,
                dir.z * setSpeed
        );

        if (motion.horizontalDistanceSqr() > 1.0E-4D) {
            float targetYaw = (float) (Mth.atan2(dir.z, dir.x) * (180F / Math.PI)) - 90F;
            float newYaw = Mth.approachDegrees(this.getYRot(), targetYaw, 15F);
            this.setYRot(newYaw);
            this.yRotO = newYaw;
        }
    }
}