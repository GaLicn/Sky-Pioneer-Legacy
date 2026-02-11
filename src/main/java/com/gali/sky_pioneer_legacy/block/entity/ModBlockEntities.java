package com.gali.sky_pioneer_legacy.block.entity;

import com.gali.sky_pioneer_legacy.Sky_pioneer_legacy;
import com.gali.sky_pioneer_legacy.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Sky_pioneer_legacy.MODID);

    public static final Supplier<BlockEntityType<AuraPowerBlockEntity>> AURA_POWER =
            BLOCK_ENTITIES.register("aura_power",
                    () -> BlockEntityType.Builder.of(AuraPowerBlockEntity::new, ModBlocks.AURA_POWER.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
