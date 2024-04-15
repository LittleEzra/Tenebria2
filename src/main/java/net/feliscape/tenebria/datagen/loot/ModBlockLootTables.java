package net.feliscape.tenebria.datagen.loot;

import net.feliscape.tenebria.block.ModBlocks;
import net.feliscape.tenebria.block.custom.DustLayerBlock;
import net.feliscape.tenebria.item.ModItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    //protected static final LootItemCondition.Builder HAS_SHEARS = MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS));

    @Override
    protected void generate() {
        // If you want a block to drop nothing ever, call .noLootTable() on the block. That will make it exempt from this.

        /* Special Loot Tables:
        this.add(ModBlocks.BLOCK.get(),
                block -> BUILDERFUNCTION);*/

        dropSelf(ModBlocks.DISTILLERY.get());

        dropSelf(ModBlocks.RIFTSTONE.get());
        dropSelf(ModBlocks.RIFTSTONE_STAIRS.get());
        dropSelf(ModBlocks.RIFTSTONE_WALL.get());

        dropSelf(ModBlocks.POLISHED_RIFTSTONE.get());
        dropSelf(ModBlocks.POLISHED_RIFTSTONE_STAIRS.get());
        dropSelf(ModBlocks.POLISHED_RIFTSTONE_BUTTON.get());
        dropSelf(ModBlocks.POLISHED_RIFTSTONE_PRESSURE_PLATE.get());
        dropSelf(ModBlocks.POLISHED_RIFTSTONE_WALL.get());

        dropSelf(ModBlocks.POLISHED_RIFTSTONE_BRICKS.get());
        dropSelf(ModBlocks.POLISHED_RIFTSTONE_BRICKS_STAIRS.get());
        dropSelf(ModBlocks.POLISHED_RIFTSTONE_BRICKS_WALL.get());

        this.add(ModBlocks.RIFTSTONE_SLAB.get(), block ->
                createSlabItemTable(ModBlocks.RIFTSTONE_SLAB.get()));
        this.add(ModBlocks.POLISHED_RIFTSTONE_SLAB.get(), block ->
                createSlabItemTable(ModBlocks.POLISHED_RIFTSTONE_SLAB.get()));
        this.add(ModBlocks.POLISHED_RIFTSTONE_BRICKS_SLAB.get(), block ->
                createSlabItemTable(ModBlocks.POLISHED_RIFTSTONE_BRICKS_SLAB.get()));

        this.add(ModBlocks.DUST_BLOCK.get(), (block) -> {
            return this.createSingleItemTableWithSilkTouch(block, ModItems.DUST.get(), ConstantValue.exactly(4.0F));
        });
        this.add(ModBlocks.DUST_PILE.get(), (block) -> {
            return LootTable.lootTable().withPool(LootPool.lootPool().when(LootItemEntityPropertyCondition.entityPresent(LootContext.EntityTarget.THIS)).add(AlternativesEntry.alternatives(AlternativesEntry.alternatives(DustLayerBlock.LAYERS.getPossibleValues(), (integer) -> {
                return LootItem.lootTableItem(ModItems.DUST.get()).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DustLayerBlock.LAYERS, integer))).apply(SetItemCountFunction.setCount(ConstantValue.exactly((float)integer.intValue())));
            }).when(HAS_NO_SILK_TOUCH), AlternativesEntry.alternatives(DustLayerBlock.LAYERS.getPossibleValues(), (statePredicateBuilder) -> {
                return (LootPoolEntryContainer.Builder<?>)(statePredicateBuilder == 8 ? LootItem.lootTableItem(ModBlocks.DUST_BLOCK.get()) : LootItem.lootTableItem(ModBlocks.DUST_PILE.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly((float)statePredicateBuilder.intValue()))).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DustLayerBlock.LAYERS, statePredicateBuilder))));
            }))));
        });

        this.add(ModBlocks.CRUMBLING_BONE.get(), block -> {
            return createSingleItemTableWithSilkTouch(block, ModItems.ANCIENT_DUST.get(), UniformGenerator.between(2f, 4f));
        });
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
