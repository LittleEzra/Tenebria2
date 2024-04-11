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
                        pOutput.accept(ModBlocks.RIFTSTONE.get());

                        pOutput.accept(ModBlocks.DUST.get());
                        pOutput.accept(ModBlocks.DUST_BLOCK.get());

                    })
                    .build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
