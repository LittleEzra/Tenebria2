package net.feliscape.tenebria.block;

import net.feliscape.tenebria.Tenebria;
import net.feliscape.tenebria.block.custom.AlchemyTableBlock;
import net.feliscape.tenebria.block.custom.DistilleryBlock;
import net.feliscape.tenebria.block.custom.DustLayerBlock;
import net.feliscape.tenebria.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Tenebria.MOD_ID);


    public static final RegistryObject<Block> DUST_BLOCK = registerBlock("dust_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.SNOW_BLOCK).mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final RegistryObject<Block> DUST_PILE = registerBlock("dust_pile",
            () -> new DustLayerBlock(BlockBehaviour.Properties.copy(Blocks.SNOW).mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final RegistryObject<Block> SOUL_STEEL_BLOCK = registerBlock("soul_steel_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).mapColor(MapColor.COLOR_LIGHT_BLUE)));

    public static final RegistryObject<Block> DISTILLERY = registerBlock("distillery",
            () -> new DistilleryBlock(BlockBehaviour.Properties.copy(Blocks.FURNACE).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<Block> ALCHEMY_TABLE = registerBlock("alchemy_table",
            () -> new AlchemyTableBlock(BlockBehaviour.Properties.copy(Blocks.ENCHANTING_TABLE).noOcclusion()));

    public static final RegistryObject<Block> CRUMBLING_BONE = registerBlock("crumbling_bone",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.BONE_BLOCK).mapColor(MapColor.COLOR_BROWN)
                    .strength(1.25f)));

    //region Riftstone
    public static final RegistryObject<Block> RIFTSTONE = registerBlock("riftstone",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE).mapColor(MapColor.COLOR_GRAY)));
    public static final RegistryObject<Block> RIFTSTONE_STAIRS = registerBlock("riftstone_stairs",
            () -> new StairBlock(() -> ModBlocks.RIFTSTONE.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(ModBlocks.RIFTSTONE.get())));
    public static final RegistryObject<Block> RIFTSTONE_SLAB = registerBlock("riftstone_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(ModBlocks.RIFTSTONE.get())));

    public static final RegistryObject<Block> RIFTSTONE_WALL = registerBlock("riftstone_wall",
            () -> new WallBlock(BlockBehaviour.Properties.copy(ModBlocks.RIFTSTONE.get())));
    //endregion
    //region Polished Riftstone
    public static final RegistryObject<Block> POLISHED_RIFTSTONE = registerBlock("polished_riftstone",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE).mapColor(MapColor.COLOR_GRAY)));
    public static final RegistryObject<Block> POLISHED_RIFTSTONE_STAIRS = registerBlock("polished_riftstone_stairs",
            () -> new StairBlock(() -> ModBlocks.POLISHED_RIFTSTONE.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(ModBlocks.POLISHED_RIFTSTONE.get())));
    public static final RegistryObject<Block> POLISHED_RIFTSTONE_SLAB = registerBlock("polished_riftstone_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(ModBlocks.POLISHED_RIFTSTONE.get())));

    public static final RegistryObject<Block> POLISHED_RIFTSTONE_BUTTON = registerBlock("polished_riftstone_button",
            () -> new ButtonBlock(BlockBehaviour.Properties.of().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY),
                    BlockSetType.STONE, 20, false));
    public static final RegistryObject<Block> POLISHED_RIFTSTONE_PRESSURE_PLATE = registerBlock("polished_riftstone_pressure_plate",
            () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS,
                    BlockBehaviour.Properties.copy(ModBlocks.POLISHED_RIFTSTONE.get()).noCollission().noOcclusion(), BlockSetType.STONE));
    public static final RegistryObject<Block> POLISHED_RIFTSTONE_WALL = registerBlock("polished_riftstone_wall",
            () -> new WallBlock(BlockBehaviour.Properties.copy(ModBlocks.POLISHED_RIFTSTONE.get())));
    //endregion

    //region Polished Riftstone Bricks
    public static final RegistryObject<Block> POLISHED_RIFTSTONE_BRICKS = registerBlock("polished_riftstone_bricks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE).mapColor(MapColor.COLOR_GRAY)));
    public static final RegistryObject<Block> POLISHED_RIFTSTONE_BRICKS_STAIRS = registerBlock("polished_riftstone_bricks_stairs",
            () -> new StairBlock(() -> ModBlocks.POLISHED_RIFTSTONE_BRICKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(ModBlocks.POLISHED_RIFTSTONE_BRICKS.get())));
    public static final RegistryObject<Block> POLISHED_RIFTSTONE_BRICKS_SLAB = registerBlock("polished_riftstone_bricks_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(ModBlocks.POLISHED_RIFTSTONE_BRICKS.get())));

    public static final RegistryObject<Block> POLISHED_RIFTSTONE_BRICKS_WALL = registerBlock("polished_riftstone_bricks_wall",
            () -> new WallBlock(BlockBehaviour.Properties.copy(ModBlocks.POLISHED_RIFTSTONE_BRICKS.get())));
    //endregion

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block)
    {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    //region HELPER FUNCTIONS
    private static LeavesBlock leaves(SoundType p_152615_) {
        return new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).strength(0.2F).randomTicks().sound(p_152615_).noOcclusion().isValidSpawn(ModBlocks::ocelotOrParrot).isSuffocating(ModBlocks::never).isViewBlocking(ModBlocks::never));
    }

    private static Boolean never(BlockState p_50779_, BlockGetter p_50780_, BlockPos p_50781_, EntityType<?> p_50782_) {
        return (boolean)false;
    }
    private static Boolean never(BlockState p_50806_, BlockGetter p_50807_, BlockPos p_50808_) {
        return false;
    }

    private static Boolean always(BlockState p_50810_, BlockGetter p_50811_, BlockPos p_50812_, EntityType<?> p_50813_) {
        return (boolean)true;
    }
    private static boolean always(BlockState p_50775_, BlockGetter p_50776_, BlockPos p_50777_) {
        return true;
    }

    private static Boolean ocelotOrParrot(BlockState p_50822_, BlockGetter p_50823_, BlockPos p_50824_, EntityType<?> p_50825_) {
        return (boolean)(p_50825_ == EntityType.OCELOT || p_50825_ == EntityType.PARROT);
    }

    private static Block simpleFlammableBlock(BlockBehaviour.Properties properties, int flammability, int firespread){
        return new Block(properties){
            @Override
            public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                return true;
            }

            @Override
            public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                return flammability;
            }

            @Override
            public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                return firespread;
            }
        };
    }
    private static SlabBlock flammableSlabBlock(BlockBehaviour.Properties properties, int flammability, int firespread){
        return new SlabBlock(properties){
            @Override
            public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                return true;
            }

            @Override
            public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                return flammability;
            }

            @Override
            public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                return firespread;
            }
        };
    }
    private static StairBlock flammableStairBlock(Block base, BlockBehaviour.Properties properties, int flammability, int firespread){
        return new StairBlock(base::defaultBlockState, properties){
            @Override
            public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                return true;
            }

            @Override
            public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                return flammability;
            }

            @Override
            public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                return firespread;
            }
        };
    }
    private static FenceBlock flammableFenceBlock(BlockBehaviour.Properties properties, int flammability, int firespread){
        return new FenceBlock(properties){
            @Override
            public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                return true;
            }

            @Override
            public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                return flammability;
            }

            @Override
            public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                return firespread;
            }
        };
    }
    private static FenceGateBlock flammableFenceGateBlock(BlockBehaviour.Properties properties, int flammability, int firespread){
        return new FenceGateBlock(properties, SoundEvents.FENCE_GATE_CLOSE, SoundEvents.FENCE_GATE_OPEN){
            @Override
            public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                return true;
            }

            @Override
            public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                return flammability;
            }

            @Override
            public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                return firespread;
            }
        };
    }
    private static FenceGateBlock flammableFenceGateBlock(BlockBehaviour.Properties properties, WoodType woodType, int flammability, int firespread){
        return new FenceGateBlock(properties, woodType){
            @Override
            public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                return true;
            }

            @Override
            public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                return flammability;
            }

            @Override
            public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                return firespread;
            }
        };
    }
    //endregion

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block)
    {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) { BLOCKS.register(eventBus); }
}
