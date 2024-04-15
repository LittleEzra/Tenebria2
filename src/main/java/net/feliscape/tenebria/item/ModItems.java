package net.feliscape.tenebria.item;

import net.feliscape.tenebria.Tenebria;
import net.feliscape.tenebria.item.custom.CapturedSoulsItem;
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
    public static final RegistryObject<Item> CAPTURED_SOULS = ITEMS.register("captured_souls",
            () -> new CapturedSoulsItem(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
