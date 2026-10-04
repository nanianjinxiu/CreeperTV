package com.nanianjinxiu.creepertv.entity.ai.goal.enperman;

import com.nanianjinxiu.creepertv.entity.animal.EnperMan;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.ForgeEventFactory;

public class PlaceTNTGoal extends Goal {

    private final EnperMan enderman;

    public PlaceTNTGoal(EnperMan enderman) {
        this.enderman = enderman;
    }

    @Override
    public boolean canUse() {
        if (!ForgeEventFactory.getMobGriefingEvent(this.enderman.level(), this.enderman)) {
            return false;
        }
        return this.enderman.getRandom().nextInt(reducedTickDelay(1200)) == 0;
    }

    @Override
    public void tick() {
        RandomSource rnd = this.enderman.getRandom();
        Level level = this.enderman.level();

        int i = Mth.floor(this.enderman.getX() - 1.0D + rnd.nextDouble() * 2.0D);
        int j = Mth.floor(this.enderman.getY() + rnd.nextDouble() * 2.0D);
        int k = Mth.floor(this.enderman.getZ() - 1.0D + rnd.nextDouble() * 2.0D);

        BlockPos pos = new BlockPos(i, j, k);
        BlockPos belowPos = pos.below();

        BlockState state = level.getBlockState(pos);
        BlockState belowState = level.getBlockState(belowPos);

        if (this.canPlaceTNT(level, pos, state, belowState, belowPos)) {
            PrimedTnt tnt = new PrimedTnt(level, i + 0.5D, j, k + 0.5D, this.enderman);
            tnt.setFuse(80);
            level.addFreshEntity(tnt);

            level.playSound(null, i + 0.5D, j, k + 0.5D,
                    SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
            this.enderman.teleportAway();
        }
    }

    private boolean canPlaceTNT(Level level, BlockPos pos,
                                BlockState state, BlockState belowState, BlockPos belowPos) {
        if (!state.isAir()) return false;
        if (belowState.isAir()) return false;
        if (belowState.is(Blocks.BEDROCK)) return false;
        if (belowState.is(Tags.Blocks.ENDERMAN_PLACE_ON_BLACKLIST)) return false;
        if (!belowState.isCollisionShapeFullBlock(level, belowPos)) return false;
        return level.getEntities(
                this.enderman,
                AABB.unitCubeFromLowerCorner(Vec3.atLowerCornerOf(pos))
        ).isEmpty();
    }
}