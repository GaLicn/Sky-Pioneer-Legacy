package com.gali.sky_pioneer_legacy;

import com.gali.sky_pioneer_legacy.item.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Sky_pioneer_legacy.MODID)
public class Sky_pioneer_legacy {
    public static final String MODID = "sky_pioneer_legacy";

    public Sky_pioneer_legacy(IEventBus modEventBus) {
        ModItems.register(modEventBus);
    }
}
