package com.gali.sky_pioneer_legacy;

import com.gali.sky_pioneer_legacy.block.entity.ModBlockEntities;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public final class ModCapabilities {
    private ModCapabilities() {
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModBlockEntities.AURA_POWER.get(),
                (blockEntity, context) -> blockEntity.getEnergyStorage());
    }
}
