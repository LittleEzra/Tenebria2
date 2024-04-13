package net.feliscape.tenebria.item;

import net.feliscape.tenebria.Tenebria;
import net.feliscape.tenebria.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Tenebria.MOD_ID);

    public static final RegistryObject<CreativeModeTab> ABYSSAL_NATURE_TAB = CREATIVE_MODE_TABS.register("abyssal_nature_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.RIFTSTONE.get()))
                    .title(Component.translatable("creativetab.tenebria.abyssal_nature_tab"))
                    .displayItems((pParameters, pOutput) -> {

                    })
                    .build());
    public static final RegistryObject<CreativeModeTab> ABYSS_BUILDING_TAB = CREATIVE_MODE_TABS.register("abyss_building_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.RIFTSTONE.get()))
                    .title(Component.translatable("creativetab.tenebria.abyssal_building_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModBlocks.DUST_PILE.get());
                        pOutput.accept(ModBlocks.DUST_BLOCK.get());

                        pOutput.accept(ModBlocks.RIFTSTONE.get());
                        pOutput.accept(ModBlocks.RIFTSTONE_SLAB.get());
                        pOutput.accept(ModBlocks.RIFTSTONE_STAIRS.get());
                        pOutput.accept(ModBlocks.RIFTSTONE_WALL.get());

                        pOutput.accept(ModBlocks.POLISHED_RIFTSTONE.get());
                        pOutput.accept(ModBlocks.POLISHED_RIFTSTONE_SLAB.get());
                        pOutput.accept(ModBlocks.POLISHED_RIFTSTONE_STAIRS.get());
                        pOutput.accept(ModBlocks.POLISHED_RIFTSTONE_BUTTON.get());
                        pOutput.accept(ModBlocks.POLISHED_RIFTSTONE_PRESSURE_PLATE.get());
                        pOutput.accept(ModBlocks.POLISHED_RIFTSTONE_WALL.get());

                        pOutput.accept(ModBlocks.POLISHED_RIFTSTONE_BRICKS.get());
                        pOutput.accept(ModBlocks.POLISHED_RIFTSTONE_BRICKS_SLAB.get());
                        pOutput.accept(ModBlocks.POLISHED_RIFTSTONE_BRICKS_STAIRS.get());
                        pOutput.accept(ModBlocks.POLISHED_RIFTSTONE_BRICKS_WALL.get());
                    })
                    .build());
    public static final RegistryObject<CreativeModeTab> ABYSS_RESOURCES_TAB = CREATIVE_MODE_TABS.register("abyss_resources_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.DUST.get()))
                    .title(Component.translatable("creativetab.tenebria.abyss_resources_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.DUST.get());

                    })
                    .build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
