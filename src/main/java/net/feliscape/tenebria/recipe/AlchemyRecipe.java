package net.feliscape.tenebria.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import it.unimi.dsi.fastutil.ints.IntList;
import net.feliscape.tenebria.Tenebria;
import net.feliscape.tenebria.item.custom.SoulContainerItem;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class AlchemyRecipe implements Recipe<SimpleContainer> {
    private final ItemStack result;
    private final ResourceLocation id;
    final NonNullList<Ingredient> ingredients;
    final int soulCost;
    private final boolean isSimple;

    public static final int MAX_INGREDIENTS = 3;

    public AlchemyRecipe(NonNullList<Ingredient> pIngredients, ItemStack pResult, int pSoulCost, ResourceLocation pId) {
        this.ingredients = pIngredients;
        this.result = pResult;
        this.id = pId;
        this.soulCost = pSoulCost;
        this.isSimple = pIngredients.stream().allMatch(Ingredient::isSimple);
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        //if (SoulContainerItem.getSouls(pContainer.getItem(3)) < soulCost) return false;

        NonNullList<Ingredient> neededReagents = copyIngredients();

        int reagentCount = 0;

        for (int i = 0; i < MAX_INGREDIENTS; i++){ // Get all reagents
            ItemStack itemStack = pContainer.getItem(i);
            if (itemStack.isEmpty()){
                continue;
            }
            reagentCount++;
            for (int j = 0; j < neededReagents.size(); j++){
                if (neededReagents.get(j).test(itemStack)){
                    neededReagents.set(j, Ingredient.EMPTY);
                    break;
                }
            }
        }

        return reagentCount == neededReagents.size() && neededReagents.stream().allMatch(Ingredient::isEmpty);
    }

    private NonNullList<Ingredient> copyIngredients() {
        NonNullList<Ingredient> reagentsCopy = NonNullList.withSize(ingredients.size(), Ingredient.EMPTY);
        for (int i = 0; i < ingredients.size(); i++){
            reagentsCopy.set(i, ingredients.get(i));
        }
        return reagentsCopy;
    }
    public int getSoulCost(){
        return soulCost;
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return result.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }


    public static class Type implements RecipeType<AlchemyRecipe>{
        public static final Type INSTANCE = new Type();
        public static final String ID = "alchemy";
    }

    public static class Serializer implements RecipeSerializer<AlchemyRecipe>{
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = Tenebria.asResource("alchemy");

        @Override
        public AlchemyRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            NonNullList<Ingredient> ingredients = ingredientsFromJson(GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients"));
            if (ingredients.isEmpty()) {
                throw new JsonParseException("No ingredients for alchemy");
            } else if (ingredients.size() > MAX_INGREDIENTS) {
                throw new JsonParseException("Too many ingredients for alchemy. The maximum is " + MAX_INGREDIENTS);
            } else {
                ItemStack itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "result"));
                int soulCost = GsonHelper.getAsInt(pSerializedRecipe, "soul_cost");
                return new AlchemyRecipe(ingredients, itemstack, soulCost, pRecipeId);
            }
        }

        private static NonNullList<Ingredient> ingredientsFromJson(JsonArray pIngredientArray) {
            NonNullList<Ingredient> ingredients = NonNullList.create();

            for(int i = 0; i < pIngredientArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(pIngredientArray.get(i), false);
                ingredients.add(ingredient);
            }

            return ingredients;
        }

        @Override
        public @Nullable AlchemyRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            int i = pBuffer.readVarInt();
            NonNullList<Ingredient> reagents = NonNullList.withSize(i, Ingredient.EMPTY);

            for(int j = 0; j < reagents.size(); ++j) {
                reagents.set(j, Ingredient.fromNetwork(pBuffer));
            }

            int soulCost = pBuffer.readInt();
            ItemStack result = pBuffer.readItem();

            return new AlchemyRecipe(reagents, result, soulCost, pRecipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, AlchemyRecipe pRecipe) {
            pBuffer.writeVarInt(pRecipe.ingredients.size());

            for(Ingredient ingredient : pRecipe.ingredients) {
                ingredient.toNetwork(pBuffer);
            }

            pBuffer.writeInt(pRecipe.soulCost);
            pBuffer.writeItemStack(pRecipe.getResultItem(null), false);
        }
    }
}
