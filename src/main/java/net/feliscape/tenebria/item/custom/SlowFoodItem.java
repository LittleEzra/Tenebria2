package net.feliscape.tenebria.item.custom;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SlowFoodItem extends Item {
    public SlowFoodItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 48;
    }
}
