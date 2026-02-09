package com.gali.sky_pioneer_legacy.block;

import com.gali.sky_pioneer_legacy.Sky_pioneer_legacy;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, Sky_pioneer_legacy.MODID);

    public static final Supplier<Block> AURA_POWER = BLOCKS.register("aura_power",
            () -> new AuraPowerBlock(BlockBehaviour.Properties.of()
                    .sound(SoundType.METAL)
                    .strength(3.0F)));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
