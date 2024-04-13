package net.feliscape.tenebria.datagen;

import net.feliscape.tenebria.Tenebria;
import net.feliscape.tenebria.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Tenebria.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        //this.tag(ModTags.Blocks.NAME)
        //        .add(Tags.Blocks.STONE);

        this.tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(ModBlocks.DUST_PILE.get(),
                        ModBlocks.DUST_BLOCK.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(
                        ModBlocks.RIFTSTONE.get(),
                        ModBlocks.RIFTSTONE_SLAB.get(),
                        ModBlocks.RIFTSTONE_STAIRS.get(),
                        ModBlocks.RIFTSTONE_WALL.get(),

                        ModBlocks.POLISHED_RIFTSTONE.get(),
                        ModBlocks.POLISHED_RIFTSTONE_SLAB.get(),
                        ModBlocks.POLISHED_RIFTSTONE_STAIRS.get(),
                        ModBlocks.POLISHED_RIFTSTONE_BUTTON.get(),
                        ModBlocks.POLISHED_RIFTSTONE_PRESSURE_PLATE.get(),
                        ModBlocks.POLISHED_RIFTSTONE_WALL.get(),

                        ModBlocks.POLISHED_RIFTSTONE_BRICKS.get(),
                        ModBlocks.POLISHED_RIFTSTONE_BRICKS_SLAB.get(),
                        ModBlocks.POLISHED_RIFTSTONE_BRICKS_STAIRS.get(),
                        ModBlocks.POLISHED_RIFTSTONE_BRICKS_WALL.get()
                        );
        this.tag(BlockTags.WALLS)
                .add(
                        ModBlocks.RIFTSTONE_WALL.get(),
                        ModBlocks.POLISHED_RIFTSTONE_WALL.get(),
                        ModBlocks.POLISHED_RIFTSTONE_BRICKS_WALL.get()
                );
    }
}
