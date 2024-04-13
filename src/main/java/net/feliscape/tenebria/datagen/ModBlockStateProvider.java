package net.feliscape.tenebria.datagen;

import net.feliscape.tenebria.Tenebria;
import net.feliscape.tenebria.block.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Tenebria.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.DUST_BLOCK);

        blockWithItem(ModBlocks.RIFTSTONE);
        blockWithItem(ModBlocks.POLISHED_RIFTSTONE);
        blockWithItem(ModBlocks.POLISHED_RIFTSTONE_BRICKS);

        stairsBlock(((StairBlock) ModBlocks.RIFTSTONE_STAIRS.get()), blockTexture(ModBlocks.RIFTSTONE.get()));
        slabBlock(((SlabBlock) ModBlocks.RIFTSTONE_SLAB.get()), blockTexture(ModBlocks.RIFTSTONE.get()), blockTexture(ModBlocks.RIFTSTONE.get()));
        wallBlock(((WallBlock) ModBlocks.RIFTSTONE_WALL.get()), blockTexture(ModBlocks.RIFTSTONE.get()));

        stairsBlock(((StairBlock) ModBlocks.POLISHED_RIFTSTONE_STAIRS.get()), blockTexture(ModBlocks.POLISHED_RIFTSTONE.get()));
        slabBlock(((SlabBlock) ModBlocks.POLISHED_RIFTSTONE_SLAB.get()), blockTexture(ModBlocks.POLISHED_RIFTSTONE.get()), blockTexture(ModBlocks.POLISHED_RIFTSTONE.get()));
        buttonBlock(((ButtonBlock) ModBlocks.POLISHED_RIFTSTONE_BUTTON.get()), blockTexture(ModBlocks.POLISHED_RIFTSTONE.get()));
        pressurePlateBlock(((PressurePlateBlock) ModBlocks.POLISHED_RIFTSTONE_PRESSURE_PLATE.get()), blockTexture(ModBlocks.POLISHED_RIFTSTONE.get()));
        wallBlock(((WallBlock) ModBlocks.POLISHED_RIFTSTONE_WALL.get()), blockTexture(ModBlocks.POLISHED_RIFTSTONE.get()));

        stairsBlock(((StairBlock) ModBlocks.POLISHED_RIFTSTONE_BRICKS_STAIRS.get()), blockTexture(ModBlocks.POLISHED_RIFTSTONE_BRICKS.get()));
        slabBlock(((SlabBlock) ModBlocks.POLISHED_RIFTSTONE_BRICKS_SLAB.get()), blockTexture(ModBlocks.POLISHED_RIFTSTONE_BRICKS.get()), blockTexture(ModBlocks.POLISHED_RIFTSTONE_BRICKS.get()));
        wallBlock(((WallBlock) ModBlocks.POLISHED_RIFTSTONE_BRICKS_WALL.get()), blockTexture(ModBlocks.POLISHED_RIFTSTONE_BRICKS.get()));
    }

    public void hangingSignBlock(Block signBlock, Block wallSignBlock, ResourceLocation texture) {
        ModelFile sign = models().sign(name(signBlock), texture);
        hangingSignBlock(signBlock, wallSignBlock, sign);
    }

    public void hangingSignBlock(Block signBlock, Block wallSignBlock, ModelFile sign) {
        simpleBlock(signBlock, sign);
        simpleBlock(wallSignBlock, sign);
    }

    private void blockItem(RegistryObject<Block> blockRegistryObject){
        simpleBlockItem(blockRegistryObject.get(), new ModelFile.UncheckedModelFile(Tenebria.MOD_ID +
                ":block/" + ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath()));
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject){
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
    private void blockWithItemAndRenderType(RegistryObject<Block> blockRegistryObject, String renderType){
        simpleBlockWithItem(blockRegistryObject.get(), models().cubeAll(name(blockRegistryObject.get()), blockTexture(blockRegistryObject.get())).renderType(renderType));
    }

    public void logBlockWithItem(RotatedPillarBlock block) {
        axisBlockWithItem(block, blockTexture(block), extend(blockTexture(block), "_top"));
    }

    public void axisBlockWithItem(RotatedPillarBlock block) {
        axisBlockWithItem(block, extend(blockTexture(block), "_side"),
                extend(blockTexture(block), "_end"));
    }

    public void axisBlockWithItem(RotatedPillarBlock block, ResourceLocation side, ResourceLocation end) {
        axisBlockWithItem(block,
                models().cubeColumn(name(block), side, end),
                models().cubeColumnHorizontal(name(block) + "_horizontal", side, end));
    }

    public void axisBlockWithItem(RotatedPillarBlock block, ModelFile vertical, ModelFile horizontal) {
        getVariantBuilder(block)
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y)
                .modelForState().modelFile(vertical).addModel()
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Z)
                .modelForState().modelFile(horizontal).rotationX(90).addModel()
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.X)
                .modelForState().modelFile(horizontal).rotationX(90).rotationY(90).addModel();
        simpleBlockItem(block, vertical);
    }

    public void crossBlockWithRenderType(Block block, String renderType) {
        getVariantBuilder(block).partialState().setModels(new ConfiguredModel(models().cross(name(block), blockTexture(block)).renderType(renderType)));
    }

    public void horizontalBlockWithItem(RegistryObject<Block> block, ModelFile model){
        horizontalBlock(block.get(), model);
        simpleBlockItem(block.get(), model);
    }
    public void cubeBottomTop(Block block){
        simpleBlockWithItem(block, models().cubeBottomTop(name(block),
                extend(blockTexture(block), "_side"),
                extend(blockTexture(block), "_bottom"),
                extend(blockTexture(block), "_top")));
    }

    private ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }
    private String name(Block block) {
        return key(block).getPath();
    }
    private ResourceLocation extend(ResourceLocation rl, String suffix) {
        return new ResourceLocation(rl.getNamespace(), rl.getPath() + suffix);
    }

}
