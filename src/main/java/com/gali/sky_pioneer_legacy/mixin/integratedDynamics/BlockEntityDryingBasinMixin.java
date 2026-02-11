package com.gali.sky_pioneer_legacy.mixin.integratedDynamics;

import org.cyclops.integrateddynamics.blockentity.BlockEntityDryingBasin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "org.cyclops.integrateddynamics.blockentity.BlockEntityDryingBasin$TickerServer")
public class BlockEntityDryingBasinMixin {
    @Redirect(
            method = "update(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lorg/cyclops/integrateddynamics/blockentity/BlockEntityDryingBasin;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/cyclops/integrateddynamics/blockentity/BlockEntityDryingBasin;getFire()I",
                    ordinal = 0
            )
    )
    private int redirectGetFireForCheck(BlockEntityDryingBasin instance) {
        return -1;
    }
}
