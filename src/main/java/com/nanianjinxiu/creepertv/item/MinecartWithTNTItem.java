package com.nanianjinxiu.creepertv.item;

import com.nanianjinxiu.creepertv.entity.ModEntities;
import com.nanianjinxiu.creepertv.entity.animal.MinecartWithTNT;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class MinecartWithTNTItem extends Item {

    public MinecartWithTNTItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide) return InteractionResult.SUCCESS;

        BlockPos pos = context.getClickedPos();

        MinecartWithTNT cart = ModEntities.MINECART_WITH_TNT.get().create(level);
        if (cart == null) return InteractionResult.FAIL;

        cart.moveTo(
                pos.getX() + 0.5,
                pos.getY() + 1.0,
                pos.getZ() + 0.5,
                context.getPlayer() != null ? context.getPlayer().getYRot() : 0F,
                0F
        );
        level.addFreshEntity(cart);

        if (context.getPlayer() != null && !context.getPlayer().getAbilities().instabuild) {
            context.getItemInHand().shrink(1);
        }
        return InteractionResult.SUCCESS;
    }
}