package net.feliscape.tenebria.datagen.advancements;

import net.feliscape.tenebria.Tenebria;
import net.feliscape.tenebria.advancements.criterion.TransmuteTrigger;
import net.feliscape.tenebria.block.ModBlocks;
import net.feliscape.tenebria.entity.ModEntityTypes;
import net.feliscape.tenebria.item.ModItems;
import net.feliscape.tenebria.item.custom.MorphoFluidItem;
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

import java.util.function.Consumer;

public class ModAdvancements implements ForgeAdvancementProvider.AdvancementGenerator {
    @Override
    public void generate(HolderLookup.Provider registries, Consumer<Advancement> pWriter, ExistingFileHelper existingFileHelper) {
        // ROOT
        Advancement tenebriaRoot = Advancement.Builder.advancement()
                .display(ModItems.EREBITE.get(),
                        title("root"), description("root"), new ResourceLocation(Tenebria.MOD_ID, "textures/gui/advancements/backgrounds/tenebria.png"),
                        FrameType.TASK, false, false, false)
                .addCriterion("entered_abyss", new ImpossibleTrigger.TriggerInstance()) // Make possible later
                .save(pWriter, advLocation("root"), existingFileHelper);

        // TRANSMUTE ITEM
        Advancement transmuteItem = Advancement.Builder.advancement().parent(tenebriaRoot)
                .display(ModBlocks.ALCHEMY_TABLE.get(), title("transmute_item"), description("transmute_item"),
                        (ResourceLocation)null, FrameType.TASK, true, true, false)
                .addCriterion("transmute_item", TransmuteTrigger.TriggerInstance.anyItem())
                .save(pWriter, advLocation("transmute_item"), existingFileHelper);

        // USE STORM BOTTLE
        Advancement.Builder.advancement().parent(transmuteItem)
                .display(ModItems.STORM_BOTTLE.get(), title("use_storm_bottle"), description("use_storm_bottle"),
                        (ResourceLocation)null, FrameType.TASK, true, true, false)
                .addCriterion("use_storm_bottle", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.STORM_BOTTLE.get()))
                .save(pWriter, advLocation("use_storm_bottle"), existingFileHelper);
        // GET CHARGED EREBITE
        Advancement chargeErebite = getItemAdvancement(ModItems.CHARGED_EREBITE.get(), transmuteItem, FrameType.TASK, pWriter, existingFileHelper);


        // BURN LUMINESCENCE
        Advancement burnLuminescence = getItemAdvancement(ModBlocks.BURNING_LUMINESCENCE_BLOCK.get(), transmuteItem, FrameType.TASK, pWriter, existingFileHelper);
        // GET PLATINUM DUST
        Advancement getPlatinumDust = getItemAdvancement(ModItems.PLATINUM_DUST.get(), transmuteItem, FrameType.TASK, pWriter, existingFileHelper);

        // GET CAPTURED ENERGY
        Advancement getCapturedEnergy = getItemAdvancement(ModItems.CAPTURED_ENERGY.get(), transmuteItem, FrameType.TASK, pWriter, existingFileHelper);
        // KILL DYING STAR
        Advancement killDyingStar = Advancement.Builder.advancement().parent(getCapturedEnergy)
                .display(ModItems.STAR_SOOT.get(), title("shoot_dying_star"), description("shoot_dying_star"),
                        (ResourceLocation)null, FrameType.TASK, true, true, false)
                .addCriterion("kill_dying_star", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(ModEntityTypes.DYING_STAR.get())))
                .save(pWriter, advLocation("shoot_dying_star"), existingFileHelper);

        // USE MORPHO FLUID
        Advancement useMorphoFluid = Advancement.Builder.advancement().parent(getCapturedEnergy)
                .display(ModItems.MORPHO_FLUID.get(), title("use_morpho_fluid"), description("use_morpho_fluid"),
                        (ResourceLocation)null, FrameType.TASK, true, true, false)
                .addCriterion("use_morpho_fluid", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
                        LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(MorphoFluidItem.MORPHABLES.get().keySet()).build()),
                        ItemPredicate.Builder.item().of(ModItems.MORPHO_FLUID.get())))
                .save(pWriter, advLocation("use_morpho_fluid"), existingFileHelper);

        // GET DIVING ARMOR
        Advancement.Builder.advancement().parent(getPlatinumDust)
                .display(ModItems.DIVING_HELMET.get(), title("diving_armor"), description("diving_armor"),
                        (ResourceLocation)null, FrameType.CHALLENGE, true, true, false).rewards(AdvancementRewards.Builder.experience(100))
                .addCriterion("diving_armor", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.DIVING_HELMET.get(), ModItems.DIVING_CHESTPLATE.get(), ModItems.DIVING_LEGGINGS.get(), ModItems.DIVING_BOOTS.get()))
                .save(pWriter, advLocation("diving_armor"), existingFileHelper);

        // PUT LENS IN VOID COLLECTOR
        Advancement.Builder.advancement().parent(transmuteItem)
                .display(ModItems.COLLECTOR_LENS.get(), title("complete_void_collector"), description("complete_void_collector"),
                        (ResourceLocation)null, FrameType.CHALLENGE, true, true, false)
                .addCriterion("use_lens", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
                        LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(ModBlocks.VOID_COLLECTOR.get()).build()),
                        ItemPredicate.Builder.item().of(ModTags.Items.LENSES)))
                .save(pWriter, advLocation("complete_void_collector"), existingFileHelper);

        // INTERACT WITH STATUE
        Advancement.Builder.advancement().parent(tenebriaRoot)
                .display(ModBlocks.STATUE.get(), title("interact_with_statue"), description("interact_with_statue"),
                        (ResourceLocation) null, FrameType.TASK, true, true, false)
                .addCriterion("interact_with_statue",ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
                        LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(ModBlocks.STATUE.get()).build()),
                        ItemPredicate.Builder.item()))
                .save(pWriter, advLocation("interact_with_statue"), existingFileHelper);

    }

    protected Advancement getItemAdvancement(ItemLike pItem, Advancement parent, FrameType frameType, Consumer<Advancement> pWriter, ExistingFileHelper existingFileHelper){
        String name = "get_" + ForgeRegistries.ITEMS.getKey(pItem.asItem()).getPath();
        return Advancement.Builder.advancement().parent(parent).display(pItem, title(name), description(name),
                        (ResourceLocation)null, frameType, true, true, false)
                .addCriterion(name, InventoryChangeTrigger.TriggerInstance.hasItems(pItem))
                .save(pWriter, advLocation(name), existingFileHelper);
    }

    protected Component title(String advancementName){
        return Component.translatable("advancements.tenebria." + advancementName + ".title");
    }
    protected Component description(String advancementName){
        return Component.translatable("advancements.tenebria." + advancementName + ".description");
    }
    protected ResourceLocation advLocation(String advancementName){
        return new ResourceLocation(Tenebria.MOD_ID, "tenebria/" + advancementName);
    }
}
