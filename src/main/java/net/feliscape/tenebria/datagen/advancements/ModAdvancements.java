package net.feliscape.tenebria.datagen.advancements;

import net.feliscape.tenebria.Tenebria;
import net.feliscape.tenebria.advancements.ModAdvancement;
import net.feliscape.tenebria.block.ModBlocks;
import net.feliscape.tenebria.entity.ModEntityTypes;
import net.feliscape.tenebria.item.ModItems;
import net.feliscape.tenebria.util.ModTags;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class ModAdvancements implements ForgeAdvancementProvider.AdvancementGenerator {

    public static final List<ModAdvancement> ENTRIES = new ArrayList<>();

    public static final ModAdvancement
            ROOT = create("root", b -> b.icon(ModItems.DUST.get())
            .title("The Abyss")
            .description("I hope you brought a light")
            .when(ConsumeItemTrigger.TriggerInstance.usedItem())
            .special(ModAdvancement.TaskType.SILENT)),

            ANCIENT_DUST = create("ancient_dust", b -> b.icon(ModItems.ANCIENT_DUST.get())
            .title("From Many Eons Ago")
            .description("Collect Ancient Dust")
            .when(ConsumeItemTrigger.TriggerInstance.usedItem())),

    END = null;


    private static ModAdvancement create(String id, UnaryOperator<ModAdvancement.Builder> b) {
        return new ModAdvancement(id, b);
    }

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<Advancement> pWriter, ExistingFileHelper existingFileHelper) {
        /*Advancement breweryRoot = Advancement.Builder.advancement()
                .display(ModBlocks.KEG.get(),
                        title("root"), description("root"), new ResourceLocation(Brewery.MOD_ID, "textures/gui/advancements/backgrounds/brewery.png"),
                        FrameType.TASK, false, false, false)
                .addCriterion("brewed_alcohol", BrewingTrigger.TriggerInstance.anyItem())
                .save(pWriter, advLocation("root"), existingFileHelper);

        getItemAdvancement(ModItems.BEER.get(), breweryRoot, FrameType.TASK, pWriter, existingFileHelper);*/

        for (ModAdvancement advancement : ENTRIES)
            advancement.save(pWriter);
    }

    /*protected Advancement getItemAdvancement(ItemLike pItem, Advancement parent, FrameType frameType, Consumer<Advancement> pWriter, ExistingFileHelper existingFileHelper){
        String name = "get_" + ForgeRegistries.ITEMS.getKey(pItem.asItem()).getPath();
        return Advancement.Builder.advancement().parent(parent).display(pItem, title(name), description(name),
                        (ResourceLocation)null, frameType, true, false, false)
                .addCriterion(name, InventoryChangeTrigger.TriggerInstance.hasItems(pItem))
                .save(pWriter, advLocation(name), existingFileHelper);
    }*/

    protected Component title(String advancementName){
        return Component.translatable("advancements.tenebria." + advancementName + ".title");
    }
    protected Component description(String advancementName){
        return Component.translatable("advancements.tenebria." + advancementName + ".description");
    }
    protected ResourceLocation advLocation(String advancementName){
        return new ResourceLocation(Tenebria.MOD_ID, "tenebria/" + advancementName);
    }

    protected String nameOf(ItemLike itemLike){
        return ForgeRegistries.ITEMS.getKey(itemLike.asItem()).getPath();
    }
}
