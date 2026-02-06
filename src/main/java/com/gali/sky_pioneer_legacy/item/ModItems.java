package com.gali.sky_pioneer_legacy.item;

import com.gali.sky_pioneer_legacy.Sky_pioneer_legacy;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShearsItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Sky_pioneer_legacy.MODID);

    public static final Supplier<Item> WOODEN_SHEARS = ITEMS.register("wooden_shears",
            () -> new ShearsItem(new Item.Properties().durability(32)));

    public static final Supplier<Item> GRASS_ASH = ITEMS.register("grass_ash",
            () -> new BoneMealItem(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
