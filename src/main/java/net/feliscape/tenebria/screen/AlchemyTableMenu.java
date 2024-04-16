package net.feliscape.tenebria.screen;

import net.feliscape.tenebria.block.ModBlocks;
import net.feliscape.tenebria.item.custom.SoulContainerItem;
import net.feliscape.tenebria.recipe.AlchemyRecipe;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerPattern;

import java.util.List;
import java.util.Optional;

public class AlchemyTableMenu extends AbstractContainerMenu {
    private static final int SLOT_REAGENT_0 = 0;
    private static final int SLOT_REAGENT_1 = 1;
    private static final int SLOT_REAGENT_2 = 2;
    private static final int SLOT_FUEL = 3;
    private static final int SLOT_RESULT = 0;

    private final ContainerLevelAccess access;
    private final Level level;
    private List<Holder<BannerPattern>> selectablePatterns = List.of();
    Runnable slotUpdateListener = () -> {
    };
    final Slot reagentSlot0;
    final Slot reagentSlot1;
    final Slot reagentSlot2;
    private final Slot fuelSlot;
    private final Slot resultSlot;
    long lastSoundTime;
    private final SimpleContainer inputContainer = new SimpleContainer(4) {
        /**
         * For block entities, ensures the chunk containing the block entity is saved to disk later - the game won't think
         * it hasn't changed and skip it.
         */
        public void setChanged() {
            super.setChanged();
            AlchemyTableMenu.this.slotsChanged(this);
            AlchemyTableMenu.this.slotUpdateListener.run();
        }
    };
    private final SimpleContainer outputContainer = new SimpleContainer(1) {
        /**
         * For block entities, ensures the chunk containing the block entity is saved to disk later - the game won't think
         * it hasn't changed and skip it.
         */
        public void setChanged() {
            super.setChanged();
            AlchemyTableMenu.this.slotUpdateListener.run();
        }
    };

    public AlchemyTableMenu(int pContainerId, Inventory pPlayerInventory, FriendlyByteBuf extraData) {
        this(pContainerId, pPlayerInventory, ContainerLevelAccess.NULL);
    }
    public AlchemyTableMenu(int pContainerId, Inventory pPlayerInventory) {
        this(pContainerId, pPlayerInventory, ContainerLevelAccess.NULL);
    }

    public AlchemyTableMenu(int pContainerId, Inventory pPlayerInventory, final ContainerLevelAccess pAccess) {
        super(ModMenuTypes.ALCHEMY_TABLE_MENU.get(), pContainerId);
        this.access = pAccess;
        this.level = pPlayerInventory.player.level();

        addPlayerInventory(pPlayerInventory);
        addPlayerHotbar(pPlayerInventory);

        reagentSlot0 = this.addSlot(new Slot(inputContainer, SLOT_REAGENT_0, 28, 17));
        reagentSlot1 = this.addSlot(new Slot(inputContainer, SLOT_REAGENT_1, 46, 17));
        reagentSlot2 = this.addSlot(new Slot(inputContainer, SLOT_REAGENT_2, 64, 17));
        fuelSlot = this.addSlot(new Slot(inputContainer, SLOT_FUEL, 46, 53){
            @Override
            public boolean mayPlace(ItemStack pStack) {
                return pStack.getItem() instanceof SoulContainerItem;
            }
        });
        resultSlot = this.addSlot(new Slot(outputContainer, SLOT_RESULT, 124, 35){
            public boolean mayPlace(ItemStack pStack) {
                return false;
            }

            public void onTake(Player pPlayer, ItemStack pStack) {
                if (!AlchemyTableMenu.this.hasRecipe() || AlchemyTableMenu.this.getCurrentRecipe().isEmpty()) return;

                ItemStack fuel = AlchemyTableMenu.this.fuelSlot.getItem();
                SoulContainerItem.setSouls(fuel, SoulContainerItem.getSouls(fuel) - AlchemyTableMenu.this.getCurrentRecipe().get().getSoulCost());
                if (SoulContainerItem.getSouls(fuel) <= 0){
                    AlchemyTableMenu.this.inputContainer.setItem(SLOT_FUEL, new ItemStack(((SoulContainerItem) fuel.getItem()).getEmpty()));
                }
                AlchemyTableMenu.this.reagentSlot0.remove(1);
                AlchemyTableMenu.this.reagentSlot1.remove(1);
                AlchemyTableMenu.this.reagentSlot2.remove(1);

                /*
                pAccess.execute((p_39952_, p_39953_) -> {
                    long l = p_39952_.getGameTime();
                    if (AlchemyTableMenu.this.lastSoundTime != l) {
                        p_39952_.playSound((Player)null, p_39953_, SoundEvents.UI_LOOM_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
                        AlchemyTableMenu.this.lastSoundTime = l;
                    }

                });*/
                super.onTake(pPlayer, pStack);
            }
        });
    }

    public void slotsChanged(Container pInventory) {
        ItemStack reagent0 = this.reagentSlot0.getItem();
        ItemStack reagent1 = this.reagentSlot1.getItem();
        ItemStack reagent2 = this.reagentSlot2.getItem();

        if (reagent0.isEmpty() && reagent1.isEmpty() && reagent2.isEmpty()) {
            this.resultSlot.set(ItemStack.EMPTY);
        } else {
            if (hasRecipe() && hasEnoughFuel()) {
                this.resultSlot.set(getCraftingResult());
            } else {
                this.resultSlot.set(ItemStack.EMPTY);
            }

            this.broadcastChanges();
        }
    }

    public boolean hasRecipe(){
        Optional<AlchemyRecipe> recipe = getCurrentRecipe();

        return recipe.isPresent();
    }
    public boolean hasEnoughFuel(){
        if (!hasRecipe()) return false;

        return SoulContainerItem.getSouls(inputContainer.getItem(SLOT_FUEL)) >= getCurrentRecipe().get().getSoulCost();
    }

    public Optional<AlchemyRecipe> getCurrentRecipe() {
        return this.level.getRecipeManager().getRecipeFor(AlchemyRecipe.Type.INSTANCE, inputContainer, level);
    }

    private ItemStack getCraftingResult() {
        Optional<AlchemyRecipe> recipe = getCurrentRecipe();

        if (recipe.isEmpty()){
            return ItemStack.EMPTY;
        }

        return recipe.get().getResultItem(null);
    }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 5;  // must be the number of slots you have!
    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + pIndex);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    public boolean stillValid(Player pPlayer) {
        return stillValid(this.access, pPlayer, ModBlocks.ALCHEMY_TABLE.get());
    }

    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.access.execute((p_39871_, p_39872_) -> {
            this.clearContainer(pPlayer, this.inputContainer);
        });
    }
}
