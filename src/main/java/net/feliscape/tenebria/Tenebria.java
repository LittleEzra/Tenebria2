package net.feliscape.tenebria;

import com.mojang.logging.LogUtils;
import net.feliscape.tenebria.block.ModBlocks;
import net.feliscape.tenebria.block.entity.ModBlockEntities;
import net.feliscape.tenebria.entity.ModEntityTypes;
import net.feliscape.tenebria.item.ModCreativeModeTabs;
import net.feliscape.tenebria.item.ModItems;
import net.feliscape.tenebria.item.custom.SoulContainerItem;
import net.feliscape.tenebria.networking.ModMessages;
import net.feliscape.tenebria.recipe.ModRecipes;
import net.feliscape.tenebria.screen.AlchemyTableScreen;
import net.feliscape.tenebria.screen.DistilleryScreen;
import net.feliscape.tenebria.screen.ModMenuTypes;
import net.feliscape.tenebria.sound.ModSounds;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import javax.annotation.Nullable;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Tenebria.MOD_ID)
public class Tenebria
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "tenebria";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public Tenebria()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModSounds.register(modEventBus);

        ModCreativeModeTabs.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        ModMenuTypes.register(modEventBus);

        ModEntityTypes.register(modEventBus);
        ModRecipes.register(modEventBus);

        ModBlockEntities.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        //ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC); // Add alongside the config class
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            ModMessages.register();

            ItemProperties.register(ModItems.CAPTURED_SOUL_BOTTLE.get(), asResource("bottle_souls"), (ClampedItemPropertyFunction)(itemStack, level, livingEntity, seed) -> {
                return 0.125f * Mth.ceil(SoulContainerItem.getSouls(itemStack) / 4f);
            });
            ItemProperties.register(ModItems.CAPTURED_SOUL_JAR.get(), asResource("jar_souls"), (ClampedItemPropertyFunction)(itemStack, level, livingEntity, seed) -> {
                return 0.125f * Mth.ceil(SoulContainerItem.getSouls(itemStack) / 4f);
            });
        });
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {

    }

    public static ResourceLocation asResource(String id) {
        return new ResourceLocation(MOD_ID, id);
    }

    public static void printDebug(String line){
        LOGGER.debug(line);
    }
    public static void printDebug(boolean value){
        printDebug(((Boolean)value).toString());
    }
    public static void printDebug(int value){
        printDebug(((Integer)value).toString());
    }
    public static void printDebug(float value){
        printDebug(((Float)value).toString());
    }
    public static void printDebug(double value){
        printDebug(((Double)value).toString());
    }

    public static void printServer(String line, @Nullable MinecraftServer server){
        if (server != null) {
            server.sendSystemMessage(Component.literal("[" + MOD_ID + "] " + line));
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            MenuScreens.register(ModMenuTypes.DISTILLERY_MENU.get(), DistilleryScreen::new);
            MenuScreens.register(ModMenuTypes.ALCHEMY_TABLE_MENU.get(), AlchemyTableScreen::new);
        }
    }
}

// The example config class commented out. Maybe add this later?
/*package com.example.examplemod;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = Darkwastes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue LOG_DIRT_BLOCK = BUILDER
            .comment("Whether to log the dirt block on common setup")
            .define("logDirtBlock", true);

    private static final ForgeConfigSpec.IntValue MAGIC_NUMBER = BUILDER
            .comment("A magic number")
            .defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);

    public static final ForgeConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER
            .comment("What you want the introduction message to be for the magic number")
            .define("magicNumberIntroduction", "The magic number is... ");

    // a list of strings that are treated as resource locations for items
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
            .comment("A list of items to log on common setup.")
            .defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), Config::validateItemName);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean logDirtBlock;
    public static int magicNumber;
    public static String magicNumberIntroduction;
    public static Set<Item> items;

    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        logDirtBlock = LOG_DIRT_BLOCK.get();
        magicNumber = MAGIC_NUMBER.get();
        magicNumberIntroduction = MAGIC_NUMBER_INTRODUCTION.get();

        // convert the list of strings into a set of items
        items = ITEM_STRINGS.get().stream()
                .map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName)))
                .collect(Collectors.toSet());
    }
}
*/
