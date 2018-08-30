package com.infinityraider.settlercraft.settlement.settler.container;

import com.infinityraider.settlercraft.api.v1.IInventorySettler;
import com.infinityraider.settlercraft.api.v1.ISettler;
import com.infinityraider.settlercraft.registry.IconRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerSettlerInventory extends ContainerSettler {
    public static final int INVENTORY_SETTLER_X = 84;
    public static final int INVENTORY_SETTLER_Y = 8;

    public static final int INVENTORY_PLAYER_X = 84;
    public static final int INVENTORY_PLAYER_Y = 98;

    private final InventoryPlayer playerInventory;
    private final IInventorySettler settlerInventory;

    public ContainerSettlerInventory(EntityPlayer player, ISettler settler) {
        super(player, settler);
        this.playerInventory = getPlayer().inventory;
        this.settlerInventory = getSettler().getSettlerInventory();
        addSlotsToContainer();
    }

    private void addSlotsToContainer() {
        //SETTLER INVENTORY
        int xOffset = INVENTORY_SETTLER_X;
        int yOffset = INVENTORY_SETTLER_Y;

        //add settler hot bar to the container
        for(int i = 0; i < 9; i++) {
            //new Slot(inventory, slot index, x coordinate, y coordinate)
            this.addSlotToContainer(new SettlerInventorySlot(settlerInventory, i, xOffset + i*18, 58 + yOffset));
        }

        //add settler main inventory to the container
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 9; j++) {
                //new Slot(inventory, slot index, x coordinate, y coordinate)
                this.addSlotToContainer(new SettlerInventorySlot(settlerInventory, j + 9*(i + 1), xOffset + j*18, yOffset + i*18));
            }
        }

        //add settlers armor to the container
        for(int i = 0; i < 4; i++) {
            this.addSlotToContainer(new SettlerInventorySlot(settlerInventory, 3 - i + 36, 8, 8 + i * 18, EntityEquipmentSlot.values()[(3-i)+2]));
        }

        //add settlers off hand item to the container
        this.addSlotToContainer(new SettlerInventorySlot(settlerInventory, 40, 26, 82, EntityEquipmentSlot.OFFHAND));


        //PLAYER INVENTORY
        xOffset = INVENTORY_PLAYER_X;
        yOffset = INVENTORY_PLAYER_Y;

        //add player's hot bar to the container
        for(int i = 0; i < 9; i++) {
            //new Slot(inventory, slot index, x coordinate, y coordinate)
            this.addSlotToContainer(new SettlerInventorySlot(playerInventory, i, xOffset + i*18, 58 + yOffset));
        }

        //add player's inventory to the container
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 9; j++) {
                //new Slot(inventory, slot index, x coordinate, y coordinate)
                this.addSlotToContainer(new SettlerInventorySlot(playerInventory, j + 9*(i + 1), xOffset + j*18, yOffset + i*18));
            }
        }

        //add player's armor to the container
        for(int i = 0; i < 4; i++) {
            this.addSlotToContainer(new SettlerInventorySlot(playerInventory, 3 - i + 36, 8, 102 + i * 18, EntityEquipmentSlot.values()[(3-i)+2]));
        }

        //add player's off hand to the container
        this.addSlotToContainer(new SettlerInventorySlot(playerInventory, 40, 62, 82, EntityEquipmentSlot.OFFHAND));
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
        private final EntityEquipmentSlot slotType;

        public SettlerInventorySlot(IInventory inv, int index, int xPosition, int yPosition) {
            this(inv, index, xPosition, yPosition, null);
        }

        public SettlerInventorySlot(IInventory inv, int index, int xPosition, int yPosition, EntityEquipmentSlot slotType) {
            super(inv, index, xPosition, yPosition);
            this.slotType = slotType;
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
            if(!(this.inventory instanceof IInventorySettler) && this.getSlotIndex()  >= 36 && this.getSlotIndex() < 40) {
                return (stack.getItem() instanceof ItemArmor) && ((ItemArmor) stack.getItem()).armorType.ordinal() - 2 ==  (this.getSlotIndex() - 36);
            }
            return this.inventory.isItemValidForSlot(this.getSlotIndex(), stack);
        }

        @Override
        @SideOnly(Side.CLIENT)
        public String getSlotTexture() {
            if(slotType == null) {
                return null;
            }
            switch(this.slotType) {
                case MAINHAND: return IconRegistry.getInstance().icon_mainHandBackground.getIconName();
                case OFFHAND: return "minecraft:items/empty_armor_slot_shield";
                case FEET: return "minecraft:items/empty_armor_slot_boots";
                case LEGS: return "minecraft:items/empty_armor_slot_leggings";
                case CHEST: return "minecraft:items/empty_armor_slot_chestplate";
                case HEAD: return "minecraft:items/empty_armor_slot_helmet";
                default: return null;
            }
        }
    }
}
