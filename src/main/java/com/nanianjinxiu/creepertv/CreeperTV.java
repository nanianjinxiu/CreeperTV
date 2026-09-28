package com.nanianjinxiu.creepertv;
import com.nanianjinxiu.creepertv.entity.ModEntities;
import com.nanianjinxiu.creepertv.entity.Phanper;
import com.nanianjinxiu.creepertv.item.ModItems;
import com.nanianjinxiu.creepertv.tab.ModTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CreeperTV.MODID)
public class CreeperTV
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "creepertv";

    public CreeperTV(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();
        //物品注册
        ModItems.ITEMS.register(modEventBus);
        //实体注册
        ModTabs.TABS.register(modEventBus);
        //创造模式物品栏注册
        ModEntities.ENTITIES.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.PHANPER.get(), Phanper.createAttributes().build());
    }
}
