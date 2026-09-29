package com.nanianjinxiu.creepertv.tab;

import com.nanianjinxiu.creepertv.CreeperTV;
import com.nanianjinxiu.creepertv.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModTabs {

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreeperTV.MODID);

    public static final RegistryObject<CreativeModeTab> CREEPERTV_TAB =
            TABS.register("creepertv_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.creepertv.creepertv_tab"))
                    .icon(() -> new ItemStack(Items.CREEPER_HEAD))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.PHANPER_SPAWN_EGG.get());
                        output.accept(ModItems.EVOPER_SPAWN_EGG.get());
                        output.accept(ModItems.VEPER_SPAWN_EGG.get());
                    })
                    .build());
}
