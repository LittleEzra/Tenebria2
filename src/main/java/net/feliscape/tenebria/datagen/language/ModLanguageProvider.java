package net.feliscape.tenebria.datagen.language;

import net.feliscape.tenebria.Tenebria;
import net.feliscape.tenebria.advancements.ModAdvancement;
import net.feliscape.tenebria.block.ModBlocks;
import net.feliscape.tenebria.datagen.advancements.ModAdvancements;
import net.feliscape.tenebria.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

/*
  "item.mod_id.item_id": "Item",
  "block.mod_id.block_id": "Block",
  "creativetab.mod_id.tab_id": "Creative Tab",

  "advancements.mod_id.advancement.title": "Advancement Title",
  "advancements.mod_id.advancement.description": "Advancement Description",

  "death.attack.damage_source": "%1$s died from x",
  "death.attack.damage_source.player": "%1$s died from x whilst fighting %2$s"
 */

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output, String locale) {
        super(output, Tenebria.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        ModAdvancements.ENTRIES.forEach(this::addAdvancement);

        this.addItem(ModItems.DUST, "Dust");
        this.addItem(ModItems.ANCIENT_DUST, "Ancient Dust");

        this.addBlock(ModBlocks.DUST_PILE, "Dust Pile");
        this.addBlock(ModBlocks.DUST_BLOCK, "Dust Block");

        this.addBlock(ModBlocks.RIFTSTONE, "Riftstone");
        this.addBlock(ModBlocks.RIFTSTONE_SLAB, "Riftstone");
        this.addBlock(ModBlocks.RIFTSTONE_STAIRS, "Riftstone");
        this.addBlock(ModBlocks.RIFTSTONE_WALL, "Riftstone");

        this.addBlock(ModBlocks.POLISHED_RIFTSTONE, "Polished Riftstone");
        this.addBlock(ModBlocks.POLISHED_RIFTSTONE_SLAB, "Polished Riftstone Slab");
        this.addBlock(ModBlocks.POLISHED_RIFTSTONE_STAIRS, "Polished Riftstone Stairs");
        this.addBlock(ModBlocks.POLISHED_RIFTSTONE_BUTTON, "Polished Riftstone Button");
        this.addBlock(ModBlocks.POLISHED_RIFTSTONE_PRESSURE_PLATE, "Polished Riftstone Pressure Plate");
        this.addBlock(ModBlocks.POLISHED_RIFTSTONE_WALL, "Polished Riftstone Wall");

        this.addBlock(ModBlocks.POLISHED_RIFTSTONE_BRICKS, "Polished Riftstone Bricks");
        this.addBlock(ModBlocks.POLISHED_RIFTSTONE_BRICKS_SLAB, "Polished Riftstone Bricks Slab");
        this.addBlock(ModBlocks.POLISHED_RIFTSTONE_BRICKS_STAIRS, "Polished Riftstone Bricks Stairs");
        this.addBlock(ModBlocks.POLISHED_RIFTSTONE_BRICKS_WALL, "Polished Riftstone Bricks Wall");

        this.add("creativetab.tenebria.abyssal_building_tab", "Abyss Building Blocks");
        this.add("creativetab.tenebria.abyssal_nature_tab", "Abyss Nature");
        this.add("creativetab.tenebria.abyss_resources_tab", "Abyss Resources");
    }

    private void addAdvancement(ModAdvancement modAdvancement) {
        this.add(modAdvancement.titleKey(), modAdvancement.getTitle());
        this.add(modAdvancement.descriptionKey(), modAdvancement.getDescription());
    }


}
