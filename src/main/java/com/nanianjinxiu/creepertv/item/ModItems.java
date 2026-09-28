package com.nanianjinxiu.creepertv.item;

import com.nanianjinxiu.creepertv.CreeperTV;
import com.nanianjinxiu.creepertv.entity.ModEntities;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CreeperTV.MODID);

    public static final RegistryObject<ForgeSpawnEggItem> PHANPER_SPAWN_EGG =
            ITEMS.register("phanper_spawn_egg", () -> new ForgeSpawnEggItem(
                    ModEntities.PHANPER,   // 关联的实体类型
                    0x2D2D2D,              // 底色（深灰色）
                    0x4CAF50,              // 斑点色（绿色）
                    new Item.Properties()
            ));
}
