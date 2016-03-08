package com.InfinityRaider.settlercraft.settlement.settler.container;

import com.InfinityRaider.settlercraft.api.v1.IInventorySettler;
import com.InfinityRaider.settlercraft.settlement.SettlementHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
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
        this.addSlotToContainer(new Slot(settlerInventory, 0, 8, 82));
        //add settlers armor to the container
        for(int i = 0; i < 4; i++) {
            this.addSlotToContainer(new Slot(settlerInventory, i + 1, 8, 8 + i * 18));
        }
        //add settler inventory to the container
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 9; j++) {
                //new Slot(inventory, slot index, x coordinate, y coordinate)
                this.addSlotToContainer(new Slot(settlerInventory, j + i*9 + 5 + 9, xOffset + j*18, yOffset + i*18));
            }
        }
        //add settler hot bar to the container
        for(int i = 0; i < 9; i++) {
            //new Slot(inventory, slot index, x coordinate, y coordinate)
            this.addSlotToContainer(new Slot(settlerInventory, i + 5, xOffset + i*18, 58 + yOffset));
        }

        //add player's armor to the container
        for(int i = 0; i < 4; i++) {
            this.addSlotToContainer(new Slot(playerInventory, 3 - i + 36, 8, 102 + i * 18));
        }
        //add player's inventory to the container
        xOffset = INVENTORY_PLAYER_X;
        yOffset = INVENTORY_PLAYER_Y;
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 9; j++) {
                //new Slot(inventory, slot index, x coordinate, y coordinate)
                this.addSlotToContainer(new Slot(playerInventory, j + i*9 + 9, xOffset + j*18, yOffset + i*18));
            }
        }
        //add player's hot bar to the container
        for(int i = 0; i < 9; i++) {
            //new Slot(inventory, slot index, x coordinate, y coordinate)
            this.addSlotToContainer(new Slot(playerInventory, i, xOffset + i*18, 58 + yOffset));
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        playerInventory.markDirty();
        settlerInventory.markDirty();
        super.onContainerClosed(player);
        SettlementHandler.getInstance().stopInteractingWithSettler(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        Slot slot = this.inventorySlots.get(index);
        if(slot == null) {
            return null;
        }
        ItemStack stack = slot.getStack();
        if(stack == null || stack.getItem() == null) {
            return null;
        }
        if(index < 5 || index >= 36 + 5) {
            if(this.mergeItemStack(stack, 5, 36 + 5, false)) {
                slot.putStack(null);
                return null;
            } else {
                return stack;
            }
        } else {
           if(this.mergeItemStack(stack, 36 + 5 + 4, 36 + 5 + 4 + 36, false)) {
                slot.putStack(null);
                return null;
            } else {
                return stack;
            }
        }
    }
}
