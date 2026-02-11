package com.gali.sky_pioneer_legacy.jei;

import com.gali.sky_pioneer_legacy.Sky_pioneer_legacy;
import com.gali.sky_pioneer_legacy.block.ModBlocks;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.LinkedHashMap;
import java.util.Map;

public class AuraPowerMultiblockCategory implements IRecipeCategory<AuraPowerMultiblockRecipe> {
    public static final RecipeType<AuraPowerMultiblockRecipe> RECIPE_TYPE =
        RecipeType.create(Sky_pioneer_legacy.MODID, "aura_power_multiblock", AuraPowerMultiblockRecipe.class);

    private static final int VIEW_Y = 56;
    private static final float VIEW_SCALE = 14.0F;
    private static final int LIST_X = 120;
    private static final int LIST_Y = 6;
    private static final int SLOT = 18;

    private final IDrawable background;
    private final IDrawable icon;

    public AuraPowerMultiblockCategory(IGuiHelper guiHelper) {
        int width = 176;
        int height = 108;
        this.background = guiHelper.createBlankDrawable(width, height);
        this.icon = guiHelper.createDrawableIngredient(mezz.jei.api.constants.VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.AURA_POWER.get()));
    }

    @Override
    public RecipeType<AuraPowerMultiblockRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei." + Sky_pioneer_legacy.MODID + ".aura_power_multiblock");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AuraPowerMultiblockRecipe recipe, IFocusGroup focuses) {
        Map<Character, Integer> counts = countBlocks(recipe);
        int y = LIST_Y;
        for (Map.Entry<Character, Integer> entry : counts.entrySet()) {
            ItemStack stack = recipe.getStack(entry.getKey());
            if (stack.isEmpty()) {
                continue;
            }
            ItemStack display = stack.copy();
            display.setCount(Math.min(64, entry.getValue()));
            builder.addSlot(RecipeIngredientRole.INPUT, LIST_X, y)
                .addItemStack(display);
            y += SLOT;
        }
        if (showOutputSlot()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, LIST_X, 90)
                .addItemStack(recipe.output());
        }
    }

    @Override
    public void draw(AuraPowerMultiblockRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        drawStructure3d(recipe, guiGraphics);
        guiGraphics.drawString(Minecraft.getInstance().font, Component.literal("5x5x1"), LIST_X, 74, 0xFFAAAAAA, false);
    }

    private void drawStructure3d(AuraPowerMultiblockRecipe recipe, GuiGraphics guiGraphics) {
        int height = recipe.height();
        int width = recipe.width();
        if (height <= 0 || width <= 0) {
            return;
        }
        RenderSystem.enableDepthTest();

        PoseStack pose = guiGraphics.pose();
        pose.pushPose();
        int leftAreaWidth = LIST_X - 4;
        int centerX = leftAreaWidth / 2;
        pose.translate(centerX, VIEW_Y, 200.0F);
        pose.scale(VIEW_SCALE, VIEW_SCALE, VIEW_SCALE);
        pose.mulPose(Axis.XP.rotationDegrees(30.0F));
        pose.mulPose(Axis.YP.rotationDegrees(45.0F));
        pose.translate(-width / 2.0F, -height / 2.0F, 0.0F);

        Minecraft mc = Minecraft.getInstance();
        BlockRenderDispatcher dispatcher = mc.getBlockRenderer();
        MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
        int light = 0xF000F0;

        for (int y = 0; y < height; y++) {
            String row = recipe.pattern()[y];
            for (int x = 0; x < width; x++) {
                char key = row.charAt(x);
                if (key == ' ') {
                    continue;
                }
                ItemStack stack = recipe.getStack(key);
                if (stack.isEmpty()) {
                    continue;
                }
                Block block = Block.byItem(stack.getItem());
                if (block.defaultBlockState().isAir()) {
                    continue;
                }
                pose.pushPose();
                pose.translate(x, y, 0);
                dispatcher.renderSingleBlock(block.defaultBlockState(), pose, buffer, light, OverlayTexture.NO_OVERLAY);
                pose.popPose();
            }
        }
        buffer.endBatch();
        pose.popPose();
        RenderSystem.disableDepthTest();
    }

    private Map<Character, Integer> countBlocks(AuraPowerMultiblockRecipe recipe) {
        Map<Character, Integer> counts = new LinkedHashMap<>();
        for (String row : recipe.pattern()) {
            for (int i = 0; i < row.length(); i++) {
                char key = row.charAt(i);
                if (key == ' ') {
                    continue;
                }
                if (recipe.getStack(key).isEmpty()) {
                    continue;
                }
                counts.put(key, counts.getOrDefault(key, 0) + 1);
            }
        }
        return counts;
    }

    private boolean showOutputSlot() {
        return false;
    }
}
