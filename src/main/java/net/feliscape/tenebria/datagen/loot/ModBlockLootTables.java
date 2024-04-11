package net.feliscape.tenebria.datagen.loot;

import net.feliscape.tenebria.block.ModBlocks;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
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

        dropSelf(ModBlocks.RIFTSTONE.get());

        dropSelf(ModBlocks.DUST.get());
        dropSelf(ModBlocks.DUST_BLOCK.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
