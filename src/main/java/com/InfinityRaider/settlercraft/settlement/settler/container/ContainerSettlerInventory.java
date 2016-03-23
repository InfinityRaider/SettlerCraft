package com.InfinityRaider.settlercraft.settlement.settler.container;

import com.InfinityRaider.settlercraft.api.v1.IInventorySettler;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ContainerSettlerInventory extends ContainerSettler {
    public static final int INVENTORY_SETTLER_X = 84;
    public static final int INVENTORY_SETTLER_Y = 8;

    public static final int INVENTORY_PLAYER_X = 84;
    public static final int INVENTORY_PLAYER_Y = 98;

    private final InventoryPlayer playerInventory;
    private final IInventorySettler settlerInventory;

    public ContainerSettlerInventory(EntityPlayer player) {
        super(player);
        this.playerInventory = getPlayer().inventory;
        this.settlerInventory = getSettler().getSettlerInventory();
        addSlotsToContainer();
    }

    private void addSlotsToContainer() {
        int xOffset = INVENTORY_SETTLER_X;
        int yOffset = INVENTORY_SETTLER_Y;
        //add settlers equipped item to the container
        this.addSlotToContainer(new SettlerInventorySlot(settlerInventory, 0, 8, 82));
        this.addSlotToContainer(new SettlerInventorySlot(settlerInventory, 0, 26, 82));
        //add settlers armor to the container
        for(int i = 0; i < 4; i++) {
            this.addSlotToContainer(new SettlerInventorySlot(settlerInventory, i + 2, 8, 8 + i * 18));
        }
        //add settler inventory to the container
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 9; j++) {
                //new Slot(inventory, slot index, x coordinate, y coordinate)
                this.addSlotToContainer(new SettlerInventorySlot(settlerInventory, j + i*9 + 6 + 9, xOffset + j*18, yOffset + i*18));
            }
        }
        //add settler hot bar to the container
        for(int i = 0; i < 9; i++) {
            //new Slot(inventory, slot index, x coordinate, y coordinate)
            this.addSlotToContainer(new SettlerInventorySlot(settlerInventory, i + 6, xOffset + i*18, 58 + yOffset));
        }

        //add player's armor to the container
        for(int i = 0; i < 4; i++) {
            this.addSlotToContainer(new SettlerInventorySlot(playerInventory, 3 - i + 36, 8, 102 + i * 18));
        }
        //add player's inventory to the container
        xOffset = INVENTORY_PLAYER_X;
        yOffset = INVENTORY_PLAYER_Y;
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 9; j++) {
                //new Slot(inventory, slot index, x coordinate, y coordinate)
                this.addSlotToContainer(new SettlerInventorySlot(playerInventory, j + i*9 + 9, xOffset + j*18, yOffset + i*18));
            }
        }
        //add player's hot bar to the container
        for(int i = 0; i < 9; i++) {
            //new Slot(inventory, slot index, x coordinate, y coordinate)
            this.addSlotToContainer(new SettlerInventorySlot(playerInventory, i, xOffset + i*18, 58 + yOffset));
        }
    }

    @Override
     public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        this.settlerInventory.closeInventory(player);
        this.playerInventory.closeInventory(player);
    }

    @Override
    public void onContainerClosed(EntityPlayer player, ISettler settler) {}

    @Override
    protected boolean stopInteracting() {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        net.minecraft.inventory.Slot slot = this.inventorySlots.get(index);
        if(slot == null) {
            return null;
        }
        ItemStack stack = slot.getStack();
        if(stack == null || stack.getItem() == null) {
            return null;
        }
        if(index < 6 || index >= 36 + 6) {
            if(this.mergeItemStack(stack, 6, 36 + 6, false)) {
                slot.putStack(null);
                return null;
            } else {
                return stack;
            }
        } else {
           if(this.mergeItemStack(stack, 36 + 6 + 4, 36 + 6 + 4 + 36, false)) {
                slot.putStack(null);
                return null;
            } else {
                return stack;
            }
        }
    }

    public static class SettlerInventorySlot extends Slot {
        public SettlerInventorySlot(IInventory inv, int index, int xPosition, int yPosition) {
            super(inv, index, xPosition, yPosition);
        }

        @Override
        public void onSlotChange(ItemStack oldStack, ItemStack newStack) {
            super.onSlotChange(oldStack, newStack);
        }

        @Override
        public void onSlotChanged() {
            super.onSlotChanged();
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            if(stack == null || stack.getItem() == null) {
                return true;
            }
            if(this.inventory instanceof InventoryPlayer && this.getSlotIndex()  >= 36) {
                return (stack.getItem() instanceof ItemArmor) && ((ItemArmor) stack.getItem()).armorType.ordinal() - 2 ==  3 - (this.getSlotIndex() - 36);
            }
            return this.inventory.isItemValidForSlot(this.getSlotIndex(), stack);
        }
    }
}
