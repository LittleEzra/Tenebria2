package net.feliscape.tenebria.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CapturedSoulsItem extends Item {
    public static final int MAX_SOULS = 16;

    public CapturedSoulsItem(Properties pProperties) {
        super(pProperties);
    }

    public static void fillBottleWithSouls(ItemStack itemStack, int souls){
        itemStack.getOrCreateTag().putInt("souls", Math.min(souls, MAX_SOULS));
    }
    public static int getSouls(ItemStack itemStack){
        if (!itemStack.hasTag()) return 0;
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
}
