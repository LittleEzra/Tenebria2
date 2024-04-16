package net.feliscape.tenebria.recipe;

import net.feliscape.tenebria.Tenebria;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Tenebria.MOD_ID);

    public static final RegistryObject<RecipeSerializer<AlchemyRecipe>> ALCHEMY =
            SERIALIZERS.register("alchemy", () -> AlchemyRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus){
        SERIALIZERS.register(eventBus);
    }
}
