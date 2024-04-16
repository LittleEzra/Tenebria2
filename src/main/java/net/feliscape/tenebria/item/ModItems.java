package net.feliscape.tenebria.item;

import net.feliscape.tenebria.Tenebria;
import net.feliscape.tenebria.item.custom.SlowFoodItem;
import net.feliscape.tenebria.item.custom.SoulContainerItem;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Tenebria.MOD_ID);

    public static final RegistryObject<Item> DUST = ITEMS.register("dust",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ANCIENT_DUST = ITEMS.register("ancient_dust",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CAPTURED_SOUL_BOTTLE = ITEMS.register("captured_soul_bottle",
            () -> new SoulContainerItem(new Item.Properties().stacksTo(1), 16, () -> Items.GLASS_BOTTLE));
    public static final RegistryObject<Item> SOUL_JAR = ITEMS.register("soul_jar",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CAPTURED_SOUL_JAR = ITEMS.register("captured_soul_jar",
            () -> new SoulContainerItem(new Item.Properties().stacksTo(1), 32, ModItems.SOUL_JAR));
    public static final RegistryObject<Item> SOUL_STEEL_INGOT = ITEMS.register("soul_steel_ingot",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> STONE_BREAD = ITEMS.register("stone_bread",
            () -> new SlowFoodItem(new Item.Properties().food(ModFoods.STONE_BREAD)));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
