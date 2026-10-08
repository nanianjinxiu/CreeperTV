package com.nanianjinxiu.creepertv.entity.ai.goal.agent;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class MinecartAgent extends Mob {
    private final AbstractMinecart cart;

    public MinecartAgent(AbstractMinecart cart, Level level) {
        super(EntityType.CREEPER, level);
        this.cart = cart;
        setNoAi(true);
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.syncFromCart();
        this.refreshDimensions();
    }

    @Override
    @NotNull
    public EntityDimensions getDimensions(@NotNull Pose pose) {
        return cart.getDimensions(pose);
    }

    public void syncFromCart() {
        this.setPos(cart.getX(), cart.getY(), cart.getZ());
    }

    @Override
    public boolean onGround() { return cart.onGround(); }

    @Override
    public @NotNull Level level() { return cart.level(); }

    @Override
    public void move(@NotNull MoverType type, @NotNull Vec3 movement) { }

    @Override
    public void setDeltaMovement(@NotNull Vec3 motion) { }

    @Override
    public void setDeltaMovement(double x, double y, double z) { }

    @Override
    public void tick() { }
}