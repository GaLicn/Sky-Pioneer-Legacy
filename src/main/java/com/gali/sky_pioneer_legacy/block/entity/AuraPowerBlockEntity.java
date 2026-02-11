package com.gali.sky_pioneer_legacy.block.entity;

import com.gali.sky_pioneer_legacy.block.ModBlocks;
import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import de.ellpeck.naturesaura.api.aura.chunk.IAuraChunk;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.EnergyStorage;

public class AuraPowerBlockEntity extends BlockEntity {
    private static final int AURA_PER_TICK = 1280;
    private static final int RF_PER_TICK = 128;
    private static final int STRUCTURE_CHECK_INTERVAL = 20;
    private static final int EFFECT_POWDER_COLOR = 0xe00a3c;
    private static final Block ANCIENT_PLANKS = BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("naturesaura", "ancient_planks"));
    private static final String[][] PATTERN = new String[][]{
            {"abcba"},
            {"b   b"},
            {"c d c"},
            {"b   b"},
            {"abcba"},
    };
    private static final BlockPos CONTROLLER_OFFSET = findControllerOffset();

    private final AuraEnergyStorage energyStorage = new AuraEnergyStorage();
    private boolean formed;
    private boolean lastRotated;
    private long lastStructureCheck = -1;

    public AuraPowerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.AURA_POWER.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AuraPowerBlockEntity blockEntity) {
        if (level.isClientSide) {
            return;
        }

        if (!blockEntity.isStructureFormed(level, pos)) {
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

    public static void clientTick(Level level, BlockPos pos, BlockState state, AuraPowerBlockEntity blockEntity) {
        if (!blockEntity.isStructureFormed(level, pos)) {
            return;
        }
        if (level.getGameTime() % 5 == 0) {
            blockEntity.spawnEffectPowderParticles(level, pos);
        }
    }

    private boolean isStructureFormed(Level level, BlockPos controllerPos) {
        long now = level.getGameTime();
        if (lastStructureCheck >= 0 && now - lastStructureCheck < STRUCTURE_CHECK_INTERVAL) {
            return formed;
        }
        lastStructureCheck = now;
        formed = checkStructure(level, controllerPos);
        return formed;
    }

    private boolean checkStructure(Level level, BlockPos controllerPos) {
        if (checkStructure(level, controllerPos, false)) {
            lastRotated = false;
            return true;
        }
        if (checkStructure(level, controllerPos, true)) {
            lastRotated = true;
            return true;
        }
        return false;
    }

    private boolean checkStructure(Level level, BlockPos controllerPos, boolean rotated) {
        int sizeZ = PATTERN[0].length;
        BlockPos controllerOffset = rotated ? rotateOffset(CONTROLLER_OFFSET, sizeZ) : CONTROLLER_OFFSET;
        BlockPos origin = controllerPos.subtract(controllerOffset);

        for (int y = 0; y < PATTERN.length; y++) {
            String[] layer = PATTERN[y];
            for (int z = 0; z < layer.length; z++) {
                String row = layer[z];
                for (int x = 0; x < row.length(); x++) {
                    char key = row.charAt(x);
                    BlockPos offset = rotated ? new BlockPos(sizeZ - 1 - z, y, x) : new BlockPos(x, y, z);
                    BlockPos checkPos = origin.offset(offset);
                    BlockState checkState = level.getBlockState(checkPos);
                    if (!matchesKey(key, checkState)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void spawnEffectPowderParticles(Level level, BlockPos controllerPos) {
        int sizeY = PATTERN.length;
        int sizeZ = PATTERN[0].length;
        int sizeX = PATTERN[0][0].length();
        boolean rotated = this.lastRotated;
        int width = rotated ? sizeZ : sizeX;
        int depth = rotated ? sizeX : sizeZ;
        BlockPos controllerOffset = rotated ? rotateOffset(CONTROLLER_OFFSET, sizeZ) : CONTROLLER_OFFSET;
        BlockPos origin = controllerPos.subtract(controllerOffset);
        double minX = origin.getX();
        double minY = origin.getY();
        double minZ = origin.getZ();
        double maxX = minX + width;
        double maxY = minY + sizeY;
        double maxZ = minZ + depth;
        AABB bounds = new AABB(minX, minY, minZ, maxX, maxY, maxZ);

        double x = bounds.minX + level.random.nextDouble() * bounds.getXsize();
        double y = bounds.minY + level.random.nextDouble() * bounds.getYsize();
        double z = bounds.minZ + level.random.nextDouble() * bounds.getZsize();
        NaturesAuraAPI.instance().spawnMagicParticle(
            x, y, z,
            level.random.nextGaussian() * 0.005F,
            level.random.nextFloat() * 0.03F,
            level.random.nextGaussian() * 0.005F,
            EFFECT_POWDER_COLOR, level.random.nextFloat() * 3F + 1F, 120, 0F, true, true);
    }

    private boolean matchesKey(char key, BlockState state) {
        return switch (key) {
            case 'a' -> state.is(ANCIENT_PLANKS);
            case 'b' -> state.is(Blocks.GLASS);
            case 'c' -> state.is(Blocks.REDSTONE_BLOCK);
            case ' ' -> state.isAir();
            case 'd' -> state.is(ModBlocks.AURA_POWER.get());
            default -> false;
        };
    }

    private static BlockPos findControllerOffset() {
        for (int y = 0; y < PATTERN.length; y++) {
            String[] layer = PATTERN[y];
            for (int z = 0; z < layer.length; z++) {
                String row = layer[z];
                for (int x = 0; x < row.length(); x++) {
                    if (row.charAt(x) == 'd') {
                        return new BlockPos(x, y, z);
                    }
                }
            }
        }
        return BlockPos.ZERO;
    }

    private static BlockPos rotateOffset(BlockPos offset, int sizeZ) {
        return new BlockPos(sizeZ - 1 - offset.getZ(), offset.getY(), offset.getX());
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
