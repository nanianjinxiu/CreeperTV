package com.nanianjinxiu.creepertv.mixin.aicode.aivillagerboomermixin;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Villager.class)
public class VillagerBoomerMixin {

    private static final String NATURAL_TAG = "creepertv:natural_boomer";   // 天生标签
    private static final String ACTIVE_TAG = "creepertv:boomer";            // 激活标签
    private static final String NBT_START = "BoomerStartTime";
    private static final String NBT_FUSE = "BoomerFuseEnd";
    private static final int CHASE_TIME = 120;
    private static final int FUSE_TIME = 30;

    @Inject(method = "customServerAiStep", at = @At("HEAD"))
    private void onHead(CallbackInfo ci) {
        Villager self = (Villager) (Object) this;

        // 被钟声影响
        if (self.getBrain().hasMemoryValue(MemoryModuleType.HEARD_BELL_TIME)) {
            // 只有天生带标签的村民，才会被激活
            if (self.getTags().contains(NATURAL_TAG)
                    && !self.getTags().contains(ACTIVE_TAG)) {
                self.addTag(ACTIVE_TAG);
                self.getPersistentData().putLong(NBT_START, self.level().getGameTime());
                self.getPersistentData().putLong(NBT_FUSE, 0L);
            }
        }

        // 清理钟声记忆，防止恐慌打断已激活的村民
        if (self.getTags().contains(ACTIVE_TAG)) {
            self.getBrain().eraseMemory(MemoryModuleType.HEARD_BELL_TIME);
        }
    }

    @Inject(method = "customServerAiStep", at = @At("TAIL"))
    private void onTail(CallbackInfo ci) {
        Villager self = (Villager) (Object) this;
        if (!self.getTags().contains(ACTIVE_TAG)) return;

        long gameTime = self.level().getGameTime();
        long startTime = self.getPersistentData().getLong(NBT_START);
        long fuseEnd = self.getPersistentData().getLong(NBT_FUSE);

        Player player = self.level().getNearestPlayer(self, 64.0D);

        // 引信阶段
        if (fuseEnd > 0) {
            long remaining = fuseEnd - gameTime;

            if (remaining <= 0) {
                self.level().explode(self, self.getX(), self.getY(), self.getZ(),
                        4.0F, Level.ExplosionInteraction.MOB);
                self.removeTag(ACTIVE_TAG);
                self.removeTag(NATURAL_TAG);
                self.getPersistentData().remove(NBT_START);
                self.getPersistentData().remove(NBT_FUSE);
                self.discard();
                return;
            }

            if (player != null) {
                self.getBrain().setMemory(MemoryModuleType.WALK_TARGET,
                        new WalkTarget(player, 0.5F, 0));
            }

            if (remaining % 10 == 0) {
                self.level().broadcastEntityEvent(self, (byte) 100);
            }
            if (remaining % 20 == 0) {
                self.level().playSound(null, self.getX(), self.getY(), self.getZ(),
                        SoundEvents.CREEPER_PRIMED, SoundSource.HOSTILE, 0.6F, 1.5F);
            }
            return;
        }

        // 追踪超时 → 取消激活，恢复普通村民（保留天生标签，下次敲钟可再激活）
        if (gameTime - startTime > CHASE_TIME) {
            self.removeTag(ACTIVE_TAG);
            self.getPersistentData().remove(NBT_START);
            self.getPersistentData().remove(NBT_FUSE);
            return;
        }

        if (player == null) return;

        // 3 格内 → 开始引信
        if (self.distanceToSqr(player) < 9.0D) {
            self.getPersistentData().putLong(NBT_FUSE, gameTime + FUSE_TIME);
            return;
        }

        // 追玩家
        self.getBrain().setMemory(MemoryModuleType.WALK_TARGET,
                new WalkTarget(player, 0.5F, 0));
    }
}