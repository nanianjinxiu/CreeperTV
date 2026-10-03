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
                    ModEntities.PHANPER,
                    0x2D2D2D,
                    0x4CAF50,
                    new Item.Properties()
            ));
    public static final RegistryObject<ForgeSpawnEggItem> EVOPER_SPAWN_EGG =
            ITEMS.register("evoper_spawn_egg", () -> new ForgeSpawnEggItem(
                    ModEntities.EVOPER,
                    0x4A7A3A,
                    0x1A1A2E,
                    new Item.Properties()
            ));
    public static final RegistryObject<ForgeSpawnEggItem> VEPER_SPAWN_EGG =
            ITEMS.register("veper_spawn_egg", () -> new ForgeSpawnEggItem(
                    ModEntities.VEPER,
                    0x5B6E8C,
                    0x4CAF50,
                    new Item.Properties()
            ));
    public static final RegistryObject<Item> IRON_GOLPER_SPAWN_EGG =
            ITEMS.register("iron_golper_spawn_egg", () -> new ForgeSpawnEggItem(
                    ModEntities.IRON_GOLPER,
                    0xDBDBDB,  // 主色
                    0x4A4A4A,  // 副色
                    new Item.Properties()
            ));
}
