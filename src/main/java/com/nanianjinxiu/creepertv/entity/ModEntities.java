package com.nanianjinxiu.creepertv.entity;

import com.nanianjinxiu.creepertv.CreeperTV;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CreeperTV.MODID);

    public static final RegistryObject<EntityType<Phanper>> PHANPER =
            ENTITIES.register("phanper", () -> EntityType.Builder
                    .of(Phanper::new, MobCategory.MONSTER)
                    .sized(0.9F, 0.5F)
                    .clientTrackingRange(10)
                    .build("creepertv:phanper"));
    public static final RegistryObject<EntityType<Evoper>> EVOPER =
            ENTITIES.register("evoper", () -> EntityType.Builder
                    .of(Evoper::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(10)
                    .build("creepertv:evoper"));
}
