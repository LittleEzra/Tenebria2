package net.feliscape.tenebria.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class SoulContainerItem extends Item {
    private final int maxSouls;
    private final Supplier<Item> empty;

    public SoulContainerItem(Properties pProperties, int maxSouls, Supplier<Item> empty) {
        super(pProperties);
        this.maxSouls = maxSouls;
        this.empty = empty;
    }

    public int getMaxSouls() {
        return maxSouls;
    }

    public static ItemStack setSouls(ItemStack itemStack, int souls){
        itemStack.getOrCreateTag().putInt("souls", souls);
        return itemStack;
    }
    public static int getSouls(ItemStack itemStack){
        if (!itemStack.hasTag() || !itemStack.getTag().contains("souls")) return 0;
        return itemStack.getTag().getInt("souls");
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        if (pStack.hasTag()){
            CompoundTag compoundtag = pStack.getTag();
            int souls = compoundtag.getInt("souls");

            pTooltipComponents.add(Component.translatable("item.tenebria.captured_souls.tooltip", souls).withStyle(ChatFormatting.AQUA));
        }
    }

    public Item getEmpty() {
        return empty.get();
    }
}
