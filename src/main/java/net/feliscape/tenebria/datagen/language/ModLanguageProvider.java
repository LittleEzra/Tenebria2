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

        this.addBlock(ModBlocks.RIFTSTONE, "Riftstone");

        this.add("creativetab.tenebria.abyssal_building_tab", "Abyss Building Blocks");
        this.add("creativetab.tenebria.abyssal_nature_tab", "Abyss Nature");
    }

    private void addAdvancement(ModAdvancement modAdvancement) {
        this.add(modAdvancement.titleKey(), modAdvancement.getTitle());
        this.add(modAdvancement.descriptionKey(), modAdvancement.getDescription());
    }


}
