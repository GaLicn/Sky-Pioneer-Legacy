package com.gali.sky_pioneer_legacy.event;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public final class PlayerSneakBonemealHandler {
    private static final String TAG_LAST_CROUCH = "sky_pioneer_legacy:last_crouch";
    private static final int RADIUS = 2;

    private PlayerSneakBonemealHandler() {
    }

    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Level level = player.level();
        if (level.isClientSide) {
            return;
        }

        CompoundTag data = player.getPersistentData();
        boolean wasCrouching = data.getBoolean(TAG_LAST_CROUCH);
        boolean isCrouching = player.isCrouching();

        if (isCrouching && !wasCrouching) {
            applyBonemealNearby(player, level);
        }

        if (wasCrouching != isCrouching) {
            data.putBoolean(TAG_LAST_CROUCH, isCrouching);
        }
    }

    private static void applyBonemealNearby(Player player, Level level) {
        BlockPos center = player.blockPosition();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int dx = -RADIUS; dx <= RADIUS; dx++) {
            for (int dy = -RADIUS; dy <= RADIUS; dy++) {
                for (int dz = -RADIUS; dz <= RADIUS; dz++) {
                    pos.set(center.getX() + dx, center.getY() + dy, center.getZ() + dz);
                    BlockState state = level.getBlockState(pos);
                    if (!state.is(BlockTags.SAPLINGS)) {
                        continue;
                    }
                    ItemStack boneMeal = new ItemStack(Items.BONE_MEAL);
                    BoneMealItem.applyBonemeal(boneMeal, level, pos, player);
                }
            }
        }
    }
}
