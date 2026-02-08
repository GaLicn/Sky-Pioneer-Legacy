package com.gali.sky_pioneer_legacy.item;

import com.gali.sky_pioneer_legacy.Sky_pioneer_legacy;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Sky_pioneer_legacy.MODID);

    public static final Supplier<CreativeModeTab> SKY_PIONEER_LEGACY_TAB =
            CREATIVE_MODE_TABS.register("sky_pioneer_legacy",
                    () -> CreativeModeTab.builder()
                            .title(Component.translatable("itemGroup.sky_pioneer_legacy"))
                            .icon(() -> new ItemStack(ModItems.WOODEN_SHEARS.get()))
                            .displayItems((parameters, output) ->
                                    ModItems.ITEMS.getEntries()
                                            .forEach(entry -> output.accept(entry.get())))
                            .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
