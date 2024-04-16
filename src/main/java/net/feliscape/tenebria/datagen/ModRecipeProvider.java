package net.feliscape.tenebria.datagen;

import net.feliscape.tenebria.Tenebria;
import net.feliscape.tenebria.block.ModBlocks;
import net.feliscape.tenebria.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        //region Riftstone Blocks
        slab(pWriter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RIFTSTONE_SLAB.get(), ModBlocks.RIFTSTONE.get());
        stairBuilder(ModBlocks.RIFTSTONE_STAIRS.get(), Ingredient.of(ModBlocks.RIFTSTONE.get()))
                .unlockedBy(getHasName(ModBlocks.RIFTSTONE.get()), has(ModBlocks.RIFTSTONE.get()))
                .save(pWriter);
        wall(pWriter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RIFTSTONE_WALL.get(), ModBlocks.RIFTSTONE.get());

        stonecutterResultFromBase(pWriter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RIFTSTONE_SLAB.get(), ModBlocks.RIFTSTONE.get(), 2);
        stonecutterResultFromBase(pWriter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RIFTSTONE_STAIRS.get(), ModBlocks.RIFTSTONE.get());
        stonecutterResultFromBase(pWriter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RIFTSTONE_WALL.get(), ModBlocks.RIFTSTONE.get());
        //endregion
        //region Polished Riftstone Blocks
        slab(pWriter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.POLISHED_RIFTSTONE_SLAB.get(), ModBlocks.POLISHED_RIFTSTONE.get());
        stairBuilder(ModBlocks.POLISHED_RIFTSTONE_STAIRS.get(), Ingredient.of(ModBlocks.POLISHED_RIFTSTONE.get()))
                .unlockedBy(getHasName(ModBlocks.POLISHED_RIFTSTONE.get()), has(ModBlocks.POLISHED_RIFTSTONE.get()))
                .save(pWriter);
        wall(pWriter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.POLISHED_RIFTSTONE_WALL.get(), ModBlocks.POLISHED_RIFTSTONE.get());
        buttonBuilder(ModBlocks.POLISHED_RIFTSTONE_BUTTON.get(), Ingredient.of(ModBlocks.POLISHED_RIFTSTONE.get()))
                .unlockedBy(getHasName(ModBlocks.POLISHED_RIFTSTONE.get()), has(ModBlocks.POLISHED_RIFTSTONE.get()))
                .save(pWriter);
        pressurePlate(pWriter, ModBlocks.POLISHED_RIFTSTONE_PRESSURE_PLATE.get(), ModBlocks.POLISHED_RIFTSTONE.get());


        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.POLISHED_RIFTSTONE.get(), 4)
                .pattern("##")
                .pattern("##")
                .define('#', ModBlocks.RIFTSTONE.get())
                .unlockedBy(getHasName(ModBlocks.RIFTSTONE.get()), has(ModBlocks.RIFTSTONE.get()))
                .save(pWriter);

        stonecutterResultFromBase(pWriter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.POLISHED_RIFTSTONE.get(), ModBlocks.RIFTSTONE.get());
        stonecutterResultFromBase(pWriter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.POLISHED_RIFTSTONE_SLAB.get(), ModBlocks.POLISHED_RIFTSTONE.get(), 2);
        stonecutterResultFromBase(pWriter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.POLISHED_RIFTSTONE_STAIRS.get(), ModBlocks.POLISHED_RIFTSTONE.get());
        stonecutterResultFromBase(pWriter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.POLISHED_RIFTSTONE_WALL.get(), ModBlocks.POLISHED_RIFTSTONE.get());
        //endregion
        //region Polished Riftstone Brick Blocks
        slab(pWriter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.POLISHED_RIFTSTONE_BRICKS_SLAB.get(), ModBlocks.POLISHED_RIFTSTONE_BRICKS.get());
        stairBuilder(ModBlocks.POLISHED_RIFTSTONE_BRICKS_STAIRS.get(), Ingredient.of(ModBlocks.POLISHED_RIFTSTONE_BRICKS.get()))
                .unlockedBy(getHasName(ModBlocks.POLISHED_RIFTSTONE_BRICKS.get()), has(ModBlocks.POLISHED_RIFTSTONE_BRICKS.get()))
                .save(pWriter);
        wall(pWriter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.POLISHED_RIFTSTONE_BRICKS_WALL.get(), ModBlocks.POLISHED_RIFTSTONE_BRICKS.get());


        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.POLISHED_RIFTSTONE_BRICKS.get(), 4)
                .pattern("##")
                .pattern("##")
                .define('#', ModBlocks.POLISHED_RIFTSTONE.get())
                .unlockedBy(getHasName(ModBlocks.POLISHED_RIFTSTONE.get()), has(ModBlocks.POLISHED_RIFTSTONE.get()))
                .save(pWriter);

        stonecutterResultFromBase(pWriter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.POLISHED_RIFTSTONE_BRICKS.get(), ModBlocks.POLISHED_RIFTSTONE.get());
        stonecutterResultFromBase(pWriter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.POLISHED_RIFTSTONE_BRICKS_SLAB.get(), ModBlocks.POLISHED_RIFTSTONE_BRICKS.get(), 2);
        stonecutterResultFromBase(pWriter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.POLISHED_RIFTSTONE_BRICKS_STAIRS.get(), ModBlocks.POLISHED_RIFTSTONE_BRICKS.get());
        stonecutterResultFromBase(pWriter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.POLISHED_RIFTSTONE_BRICKS_WALL.get(), ModBlocks.POLISHED_RIFTSTONE_BRICKS.get());
        //endregion

        nineBlockStorageRecipes(pWriter, RecipeCategory.MISC, ModItems.SOUL_STEEL_INGOT.get(), RecipeCategory.BUILDING_BLOCKS, ModBlocks.SOUL_STEEL_BLOCK.get());
    }

    protected static void oreSmelting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.BLASTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static void oreCooking(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult,
                    pExperience, pCookingTime, pCookingSerializer)
                    .group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(pFinishedRecipeConsumer, Tenebria.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }

    }
}
