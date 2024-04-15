package net.feliscape.tenebria.block.entity;

import com.google.common.collect.ImmutableMap;
import net.feliscape.tenebria.Tenebria;
import net.feliscape.tenebria.item.ModItems;
import net.feliscape.tenebria.item.custom.CapturedSoulsItem;
import net.feliscape.tenebria.screen.DistilleryMenu;
import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.*;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Map;

public class DistilleryBlockEntity extends BlockEntity implements MenuProvider, StackedContentsCompatible {
    int souls;

    private static Map<Item, Integer> soulAmounts = ImmutableMap.<Item, Integer>builder()
            .put(ModItems.ANCIENT_DUST.get(), 2)
            .put(Items.SOUL_SAND, 1)
            .put(Items.SOUL_SOIL, 1)
            .put(Items.ANCIENT_DEBRIS, 6)
            .build();

    protected static final int SLOT_INPUT = 0;
    protected static final int SLOT_FUEL = 1;
    protected static final int SLOT_BOTTLE = 2;
    protected static final int SLOT_COUNT = 3;

    public static final int DATA_LIT_TIME = 0;
    private static final int[] SLOTS_FOR_UP = new int[]{0};
    private static final int[] SLOTS_FOR_DOWN = new int[]{2, 1};
    private static final int[] SLOTS_FOR_SIDES = new int[]{1};
    public static final int DATA_LIT_DURATION = 1;
    public static final int DATA_COOKING_PROGRESS = 2;
    public static final int DATA_COOKING_TOTAL_TIME = 3;
    public static final int NUM_DATA_VALUES = 4;
    public static final int BURN_TIME_STANDARD = 200;
    public static final int BURN_COOL_SPEED = 2;

    private final ItemStackHandler itemHandler = new ItemStackHandler(SLOT_COUNT){
        @Override
        protected int getStackLimit(int slot, @NotNull ItemStack stack) {
            return slot == SLOT_BOTTLE ? 1 : super.getStackLimit(slot, stack);
        }
    };
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    int litTime;
    int litDuration;
    int cookingProgress;
    int cookingTotalTime;
    protected final ContainerData data;

    public DistilleryBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.DISTILLERY_BE.get(), pPos, pBlockState);
        data = new ContainerData() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> DistilleryBlockEntity.this.litTime;
                    case 1 -> DistilleryBlockEntity.this.litDuration;
                    case 2 -> DistilleryBlockEntity.this.cookingProgress;
                    case 3 -> DistilleryBlockEntity.this.cookingTotalTime;
                    case 4 -> DistilleryBlockEntity.this.souls;
                    default ->  0;
                };
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0 -> DistilleryBlockEntity.this.litTime = value;
                    case 1 -> DistilleryBlockEntity.this.litDuration = value;
                    case 2 -> DistilleryBlockEntity.this.cookingProgress = value;
                    case 3 -> DistilleryBlockEntity.this.cookingTotalTime = value;
                    case 4 -> DistilleryBlockEntity.this.souls = value;
                }
            }

            public int getCount() {
                return 5;
            }
        };
    }

    private boolean isLit() {
        return this.litTime > 0;
    }

    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("Inventory"));
        if (itemHandler.getSlots() != SLOT_COUNT) itemHandler.setSize(SLOT_COUNT);
        this.litTime = pTag.getInt("BurnTime");
        this.cookingProgress = pTag.getInt("CookTime");
        this.cookingTotalTime = pTag.getInt("CookTimeTotal");
        this.litDuration = this.getBurnDuration(this.itemHandler.getStackInSlot(SLOT_FUEL));
        this.souls = pTag.getInt("Souls");
    }

    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("BurnTime", this.litTime);
        pTag.putInt("CookTime", this.cookingProgress);
        pTag.putInt("CookTimeTotal", this.cookingTotalTime);
        pTag.putInt("Souls", this.souls);
        pTag.put("Inventory", itemHandler.serializeNBT());
    }

    public void drops(){
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++){
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, DistilleryBlockEntity pBlockEntity) {
        boolean wasLit = pBlockEntity.isLit();
        boolean changed = false;
        if (pBlockEntity.isLit()) {
            --pBlockEntity.litTime;
        }

        ItemStack fuelItem = pBlockEntity.itemHandler.getStackInSlot(SLOT_FUEL);
        boolean ingredientNotEmpty = !pBlockEntity.itemHandler.getStackInSlot(SLOT_INPUT).isEmpty();
        boolean fuelItemNotEmpty = !fuelItem.isEmpty();
        if (pBlockEntity.souls > 0){
            ItemStack bottleItem = pBlockEntity.itemHandler.getStackInSlot(SLOT_BOTTLE);
            if (bottleItem.is(Items.GLASS_BOTTLE)){
                pBlockEntity.souls--;
                pBlockEntity.itemHandler.setStackInSlot(SLOT_BOTTLE, new ItemStack(ModItems.CAPTURED_SOULS.get()));

                CapturedSoulsItem.fillBottleWithSouls(pBlockEntity.itemHandler.getStackInSlot(SLOT_BOTTLE), 1);
            } else if (bottleItem.is(ModItems.CAPTURED_SOULS.get()) && CapturedSoulsItem.getSouls(bottleItem) < CapturedSoulsItem.MAX_SOULS){
                pBlockEntity.souls--;

                CapturedSoulsItem.fillBottleWithSouls(bottleItem, CapturedSoulsItem.getSouls(bottleItem) + 1);
            }
        }
        if (pBlockEntity.itemHandler.getStackInSlot(SLOT_INPUT).is(ModItems.CAPTURED_SOULS.get())){ // Empty Captured Souls
            ItemStack inputStack = pBlockEntity.itemHandler.getStackInSlot(SLOT_INPUT);
            if (CapturedSoulsItem.getSouls(inputStack) > 0){
                CapturedSoulsItem.fillBottleWithSouls(inputStack, CapturedSoulsItem.getSouls(inputStack) - 1);
                pBlockEntity.souls++;
                if (CapturedSoulsItem.getSouls(inputStack) == 0){
                    pBlockEntity.itemHandler.setStackInSlot(SLOT_INPUT, new ItemStack(Items.GLASS_BOTTLE));
                }
            } else{
                pBlockEntity.itemHandler.setStackInSlot(SLOT_INPUT, new ItemStack(Items.GLASS_BOTTLE));
            }
            changed = true;
        }

        if (pBlockEntity.isLit() || fuelItemNotEmpty && ingredientNotEmpty) {
            if (!pBlockEntity.isLit() && pBlockEntity.canDistill()) {
                pBlockEntity.litTime = pBlockEntity.getBurnDuration(fuelItem);
                pBlockEntity.litDuration = pBlockEntity.litTime;
                if (pBlockEntity.isLit()) {
                    changed = true;
                    if (fuelItem.hasCraftingRemainingItem())
                        pBlockEntity.itemHandler.setStackInSlot(1, fuelItem.getCraftingRemainingItem());
                    else
                    if (fuelItemNotEmpty) {
                        fuelItem.shrink(1);
                        if (fuelItem.isEmpty()) {
                            pBlockEntity.itemHandler.setStackInSlot(1, fuelItem.getCraftingRemainingItem());
                        }
                    }
                }
            }

            if (pBlockEntity.isLit() && pBlockEntity.canDistill()) {
                ++pBlockEntity.cookingProgress;
                changed = true;
                Tenebria.printDebug(pBlockEntity.cookingProgress);
                if (pBlockEntity.cookingProgress >= pBlockEntity.cookingTotalTime) {
                    pBlockEntity.cookingProgress = 0;
                    pBlockEntity.cookingTotalTime = getTotalCookTime();
                    pBlockEntity.distill();
                }
            } else {
                pBlockEntity.cookingProgress = 0;
            }
        } else if (!pBlockEntity.isLit() && pBlockEntity.cookingProgress > 0) {
            pBlockEntity.cookingProgress = Mth.clamp(pBlockEntity.cookingProgress - 2, 0, pBlockEntity.cookingTotalTime);
        }

        if (wasLit != pBlockEntity.isLit()) {
            changed = true;
            pState = pState.setValue(AbstractFurnaceBlock.LIT, Boolean.valueOf(pBlockEntity.isLit()));
            pLevel.setBlock(pPos, pState, 3);
        }

        if (changed) {
            setChanged(pLevel, pPos, pState);
        }

    }

    private boolean canDistill(){
        return hasSouls(this.itemHandler.getStackInSlot(SLOT_INPUT));
    }

    private boolean distill() {
        if (canDistill()){
            this.itemHandler.extractItem(SLOT_INPUT, 1, false);
            souls += getSoulAmount(this.itemHandler.getStackInSlot(SLOT_INPUT));
            return true;
        } else{
            return false;
        }
    }

    private static int getSoulAmount(ItemStack itemStack) {
        if (hasSouls(itemStack)) {
            return soulAmounts.get(itemStack.getItem());
        } else{
            Tenebria.printDebug("Trying to get soul amount of item with unassigned soul amount");
            return 0;
        }
    }
    public static boolean hasSouls(ItemStack itemStack) {
        return soulAmounts.containsKey(itemStack.getItem());
    }

    public int getSouls(){
        return souls;
    }

    protected int getBurnDuration(ItemStack pFuel) {
        if (pFuel.isEmpty()) {
            return 0;
        } else {
            Item item = pFuel.getItem();
            return net.minecraftforge.common.ForgeHooks.getBurnTime(pFuel, RecipeType.SMELTING);
        }
    }

    private static int getTotalCookTime() {
        return 200;
    }

    public static boolean isFuel(ItemStack pStack) {
        return net.minecraftforge.common.ForgeHooks.getBurnTime(pStack, null) > 0;
    }

    public int[] getSlotsForFace(Direction pSide) {
        if (pSide == Direction.DOWN) {
            return SLOTS_FOR_DOWN;
        } else {
            return pSide == Direction.UP ? SLOTS_FOR_UP : SLOTS_FOR_SIDES;
        }
    }

    /**
     * Returns {@code true} if automation can insert the given item in the given slot from the given side.
     */
    public boolean canPlaceItemThroughFace(int pIndex, ItemStack pItemStack, @Nullable Direction pDirection) {
        return this.canPlaceItem(pIndex, pItemStack);
    }

    /**
     * Returns {@code true} if automation can extract the given item in the given slot from the given side.
     */
    public boolean canTakeItemThroughFace(int pIndex, ItemStack pStack, Direction pDirection) {
        if (pDirection == Direction.DOWN && pIndex == 1) {
            return pStack.is(Items.WATER_BUCKET) || pStack.is(Items.BUCKET);
        } else {
            return true;
        }
    }
    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setItem(int pIndex, ItemStack pStack) {
        ItemStack itemstack = this.itemHandler.getStackInSlot(pIndex);
        boolean flag = !pStack.isEmpty() && ItemStack.isSameItemSameTags(itemstack, pStack);
        this.itemHandler.setStackInSlot(pIndex, pStack);
        if (pIndex == SLOT_BOTTLE && pStack.getCount() > 1) {
            pStack.setCount(1);
        }

        if (pIndex == 0 && !flag) {
            this.cookingTotalTime = getTotalCookTime();
            this.cookingProgress = 0;
            this.setChanged();
        }

    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    public boolean stillValid(Player pPlayer) {
        return Container.stillValidBlockEntity(this, pPlayer);
    }

    /**
     * Returns {@code true} if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     * For guis use Slot.isItemValid
     */
    public boolean canPlaceItem(int pIndex, ItemStack pStack) {
        if (pIndex != 1) {
            return true;
        } else {
            ItemStack itemstack = this.itemHandler.getStackInSlot(SLOT_FUEL);
            return net.minecraftforge.common.ForgeHooks.getBurnTime(pStack, RecipeType.SMELTING) > 0 || pStack.is(Items.BUCKET) && !itemstack.is(Items.BUCKET);
        }
    }

    private static void createExperience(ServerLevel pLevel, Vec3 pPopVec, int pRecipeIndex, float pExperience) {
        int i = Mth.floor((float)pRecipeIndex * pExperience);
        float f = Mth.frac((float)pRecipeIndex * pExperience);
        if (f != 0.0F && Math.random() < (double)f) {
            ++i;
        }

        ExperienceOrb.award(pLevel, pPopVec, i);
    }

    public void fillStackedContents(StackedContents pHelper) {
        for (int i = 0; i < this.itemHandler.getSlots(); i++){
            pHelper.accountStack(itemHandler.getStackInSlot(i));
        }
    }

    @Override
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
        if (capability == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.tenebria.distillery");
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new DistilleryMenu(pContainerId, pPlayerInventory, this, this.data);
    }
}
