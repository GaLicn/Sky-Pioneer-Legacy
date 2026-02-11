package com.gali.sky_pioneer_legacy.block.entity;

import de.ellpeck.naturesaura.api.aura.chunk.IAuraChunk;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.EnergyStorage;

public class AuraPowerBlockEntity extends BlockEntity {
    private static final int AURA_PER_TICK = 1280;
    private static final int RF_PER_TICK = 128;

    private final AuraEnergyStorage energyStorage = new AuraEnergyStorage();

    public AuraPowerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.AURA_POWER.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AuraPowerBlockEntity blockEntity) {
        if (level.isClientSide) {
            return;
        }

        blockEntity.pushEnergy(level);

        int space = blockEntity.energyStorage.getMaxEnergyStored() - blockEntity.energyStorage.getEnergyStored();
        if (space < RF_PER_TICK) {
            return;
        }

        int AuraCheck = IAuraChunk.getAuraInArea(level,pos,8);
        if(AuraCheck <= 50000 ){
            return;
        }

        IAuraChunk.getAuraChunk(level, pos).drainAura(pos, AURA_PER_TICK);
        blockEntity.energyStorage.addEnergy(RF_PER_TICK);
        blockEntity.setChanged();
    }

    private void pushEnergy(Level level) {
        if (this.energyStorage.getEnergyStored() <= 0) {
            return;
        }

        int toSend = Math.min(this.energyStorage.getEnergyStored(), RF_PER_TICK);
        if (toSend <= 0) {
            return;
        }

        for (Direction direction : Direction.values()) {
            BlockPos targetPos = this.worldPosition.relative(direction);
            var target = level.getBlockEntity(targetPos);
            if (target == null) {
                continue;
            }
            var targetState = level.getBlockState(targetPos);
            var storage = level.getCapability(Capabilities.EnergyStorage.BLOCK, targetPos, targetState, target, direction.getOpposite());
            if (storage == null) {
                continue;
            }

            int accepted = storage.receiveEnergy(toSend, false);
            if (accepted > 0) {
                this.energyStorage.extractEnergy(accepted, false);
                this.setChanged();
                break;
            }
        }
    }

    public EnergyStorage getEnergyStorage() {
        return this.energyStorage;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("Energy", this.energyStorage.getEnergyStored());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.energyStorage.setEnergy(tag.getInt("Energy"));
    }

    private static class AuraEnergyStorage extends EnergyStorage {
        public AuraEnergyStorage() {
            super(10000, 0, 128);
        }

        public void setEnergy(int energy) {
            this.energy = Math.min(energy, this.capacity);
        }

        public void addEnergy(int amount) {
            this.energy = Math.min(this.energy + amount, this.capacity);
        }

    }
}
