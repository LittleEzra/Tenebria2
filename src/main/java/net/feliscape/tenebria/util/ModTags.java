package net.feliscape.tenebria.util;

import net.feliscape.tenebria.Tenebria;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks{


        private static TagKey<Block> create(String path){
            return BlockTags.create(new ResourceLocation(Tenebria.MOD_ID, path));
        }
    }

    public static class Items{


        private static TagKey<Item> create(String path){
            return ItemTags.create(new ResourceLocation(Tenebria.MOD_ID, path));
        }
    }
}
