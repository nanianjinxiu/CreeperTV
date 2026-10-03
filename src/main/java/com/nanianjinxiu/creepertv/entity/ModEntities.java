package com.nanianjinxiu.creepertv.entity;

import com.nanianjinxiu.creepertv.CreeperTV;
import com.nanianjinxiu.creepertv.entity.animal.*;
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
    public static final RegistryObject<EntityType<CreeperFangEntity>> CREEPER_FANG =
            ENTITIES.register("creeper_fang", () -> EntityType.Builder
                    .<CreeperFangEntity>of(CreeperFangEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.8F)
                    .clientTrackingRange(6)
                    .build("creepertv:creeper_fang"));
    public static final RegistryObject<EntityType<Veper>> VEPER =
            ENTITIES.register("veper", () -> EntityType.Builder
                    .<Veper>of(Veper::new, MobCategory.MONSTER)
                    .sized(0.4F, 0.8F)
                    .clientTrackingRange(8)
                    .build("creepertv:veper"));
    public static final RegistryObject<EntityType<IronGolper>> IRON_GOLPER =
            ENTITIES.register("iron_golper", () -> EntityType.Builder
                    .<IronGolper>of(IronGolper::new, MobCategory.MISC)
                    .sized(1.4F, 2.7F)
                    .clientTrackingRange(10)
                    .build("creepertv:iron_golper"));
}
