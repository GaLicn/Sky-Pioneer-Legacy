package com.gali.sky_pioneer_legacy;

import com.gali.sky_pioneer_legacy.block.ModBlocks;
import com.gali.sky_pioneer_legacy.block.entity.ModBlockEntities;
import com.gali.sky_pioneer_legacy.item.ModCreativeTabs;
import com.gali.sky_pioneer_legacy.item.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Sky_pioneer_legacy.MODID)
public class Sky_pioneer_legacy {
    public static final String MODID = "sky_pioneer_legacy";

    public Sky_pioneer_legacy(IEventBus modEventBus) {
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
    }
}
