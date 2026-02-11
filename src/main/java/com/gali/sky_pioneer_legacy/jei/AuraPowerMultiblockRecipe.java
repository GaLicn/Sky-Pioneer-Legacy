package com.gali.sky_pioneer_legacy.jei;

import net.minecraft.world.item.ItemStack;

import java.util.Map;

public record AuraPowerMultiblockRecipe(String[] pattern, Map<Character, ItemStack> key, ItemStack output) {
    public int width() {
        return pattern.length == 0 ? 0 : pattern[0].length();
    }

    public int height() {
        return pattern.length;
    }

    public ItemStack getStack(char c) {
        return key.getOrDefault(c, ItemStack.EMPTY);
    }
}
