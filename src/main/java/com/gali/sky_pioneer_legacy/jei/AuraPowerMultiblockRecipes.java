package com.gali.sky_pioneer_legacy.jei;

import com.gali.sky_pioneer_legacy.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.Map;

public final class AuraPowerMultiblockRecipes {
    private AuraPowerMultiblockRecipes() {
    }

    public static List<AuraPowerMultiblockRecipe> getAll() {
        String[] pattern = new String[]{
            "abcba",
            "b   b",
            "c d c",
            "b   b",
            "abcba"
        };

        Map<Character, ItemStack> key = Map.of(
            'a', stack("naturesaura", "ancient_planks"),
            'b', new ItemStack(Blocks.GLASS),
            'c', new ItemStack(Blocks.REDSTONE_BLOCK),
            'd', new ItemStack(ModBlocks.AURA_POWER.get())
        );

        return List.of(new AuraPowerMultiblockRecipe(pattern, key, new ItemStack(ModBlocks.AURA_POWER.get())));
    }

    private static ItemStack stack(String namespace, String path) {
        Block block = BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath(namespace, path));
        if (block == null || block == Blocks.AIR) {
            return ItemStack.EMPTY;
        }
        return new ItemStack(block);
    }
}
