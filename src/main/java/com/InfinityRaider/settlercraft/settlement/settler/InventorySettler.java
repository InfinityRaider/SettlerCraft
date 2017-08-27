package com.InfinityRaider.settlercraft.settlement.settler;

import com.InfinityRaider.settlercraft.api.v1.IInventorySettler;
import com.infinityraider.infinitylib.utility.inventory.IInventorySerializableItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventorySettler extends InventoryPlayer implements IInventorySettler, IInventorySerializableItemHandler {
    private EntitySettler settler;

    private List<IListener> listeners;

    protected InventorySettler(EntitySettler settler) {
        super(settler.getFakePlayerImplementation());
        this.settler = settler;
        if(!settler.worldObj.isRemote) {
            this.listeners = new ArrayList<>();
        }
    }

    @Override
    public EntitySettler getSettler() {
        return settler;
    }

    public ItemStack getMainHandStack() {
        return this.mainInventory[0];
    }

    public ItemStack getOffHandStack() {
        return this.offHandInventory[0];
    }

    @Override
    public ItemStack getEquippedItem(EnumHand hand) {
        switch(hand) {
            case MAIN_HAND: return this.getMainHandStack();
            case OFF_HAND: return this.getOffHandStack();
            default: return null;
        }
    }

    @Override
    public void setEquippedItem(EnumHand hand, ItemStack stack) {
        switch(hand) {
            case MAIN_HAND: this.mainInventory[0] = stack; break;
            case OFF_HAND: this.offHandInventory[0] = stack; break;
        }
    }

    @Override
    public ItemStack getEquipmentInSlot(EntityEquipmentSlot slot) {
        switch(slot) {
            case MAINHAND: return this.getMainHandStack();
            case OFFHAND: return this.getOffHandStack();
            case FEET: return armorInventory[0];
            case LEGS: return armorInventory[1];
            case CHEST: return armorInventory[2];
            case HEAD: return armorInventory[3];
            default: return null;
        }
    }

    @Override
    public void setEquipmentInSlot(EntityEquipmentSlot slot, ItemStack stack) {
        switch(slot) {
            case MAINHAND: this.mainInventory[0] = stack; break;
            case OFFHAND: this.offHandInventory[0] = stack; break;
            case FEET:
                if(isItemValidForSlot(2, stack)) {
                    armorInventory[0] = stack;
                }
                break;
            case LEGS:
                if(isItemValidForSlot(3, stack)) {
                    armorInventory[1] = stack;
                }
                break;
            case CHEST:
                if(isItemValidForSlot(4, stack)) {
                    armorInventory[2] = stack;
                }
                break;
            case HEAD:
                if(isItemValidForSlot(5, stack)) {
                    armorInventory[3] = stack;
                }
                break;
        }
    }

    @Override
    public Map<EntityEquipmentSlot, ItemStack> getEquipmentMap() {
        Map<EntityEquipmentSlot, ItemStack> map = new HashMap<>();
        for(EntityEquipmentSlot slot: EntityEquipmentSlot.values()) {
            map.put(slot, getEquipmentInSlot(slot));
        }
        return map;
    }

    @Override
    public List<ItemStack> getEquipmentList() {
        List<ItemStack> list = new ArrayList<>();
        if(this.getMainHandStack() != null) {
            list.add(this.getMainHandStack());
        }
        if(this.getOffHandStack() != null) {
            list.add(this.getOffHandStack());
        }
        for(ItemStack stack : armorInventory) {
            if(stack != null) {
                list.add(stack);
            }
        }
        return list;
    }

    @Override
    public ItemStack[] getMainInventory() {
        return mainInventory;
    }

    @Override
    public ItemStack[] getArmorInventory() {
        return armorInventory;
    }

    @Override
    public ItemStack addStackToInventory(ItemStack stack) {
        if(stack == null || stack.stackSize == 0) {
            return stack;
        }
        ItemStack remaining = stack.copy();
        //try to merge with existing stacks
        for(int i = 0; i < this.getSizeInventory() && remaining.stackSize > 0; i++) {
            if(!isItemValidForSlot(i, remaining)) {
                continue;
            }
            ItemStack stackInSlot = getStackInSlot(i);
            if(stackInSlot != null && isSameItem(remaining, stackInSlot)) {
                int room = Math.max(0, stackInSlot.getMaxStackSize() - stackInSlot.stackSize);
                int amount = Math.min(room, remaining.stackSize);
                if(amount > 0) {
                    remaining.stackSize = remaining.stackSize - amount;
                    stackInSlot.stackSize = stackInSlot.stackSize + amount;
                }
            }
        }
        //put in first free slot
        if(remaining.stackSize <= 0) {
            remaining = null;
        } else {
            for (int i = 0; i < this.getSizeInventory() && remaining != null; i++) {
                if (!isItemValidForSlot(i, remaining)) {
                    continue;
                }
                if (getStackInSlot(i) == null) {
                    this.setInventorySlotContents(i, remaining.copy());
                    remaining = null;
                }
            }
        }
        return remaining;
    }

    @Override
    public int getSlotForStack(ItemStack stack) {
        return getSlotForStack(stack, 1);
    }

    @Override
    public int getSlotForStack(ItemStack stack, int n) {
        if (stack == null) {
            return -1;
        }
        for (int i = 0; i < mainInventory.length; i++) {
            ItemStack inSlot = this.getStackInSlot(i);
            if (isSameItem(stack, inSlot)) {
                n = n - 1;
                if (n <= 0) {
                    return i + 2 + armorInventory.length;
                }
            }
        }
        if (isSameItem(stack, this.getOffHandStack())) {
            n = n - 1;
            if (n <= 0) {
                return mainInventory.length + armorInventory.length;
            }
        }
        return - 1;
    }

    @Override
    public boolean hasStack(ItemStack stack) {
        return stack == null || getSlotForStack(stack) >= 0;
    }

    @Override
    public void consumeStack(ItemStack stack) {
        if(stack != null) {
            int index = getSlotForStack(stack);
            this.decrStackSize(index, stack.stackSize);
        }
    }

    @Override
    public boolean switchStacksInSlots(int slot1, int slot2) {
        ItemStack stack1 = this.getStackInSlot(slot1);
        ItemStack stack2 = this.getStackInSlot(slot2);
        if(this.isItemValidForSlot(slot1, stack2) && this.isItemValidForSlot(slot2, stack1)) {
            this.setInventorySlotContents(slot1, stack2);
            this.setInventorySlotContents(slot2, stack1);
            return true;
        }
        return false;
    }

    @Override
    public ItemStack[] toArray() {
        ItemStack[] inv = new ItemStack[this.getSizeInventory()];
        System.arraycopy(mainInventory, 0, inv, 0, mainInventory.length);
        System.arraycopy(armorInventory, 0, inv, mainInventory.length, armorInventory.length);
        System.arraycopy(offHandInventory, 0, inv, mainInventory.length + mainInventory.length, offHandInventory.length);
        return inv;
    }

    @Override
    public void registerInventoryListener(IListener listener) {
        if(!getSettler().getWorld().isRemote) {
            this.listeners.add(listener);
        }
    }

    @Override
    public NBTTagCompound writeToNBT() {
        return this.writeInventoryToNBT(new NBTTagCompound());
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        this.readInventoryFromNBT(tag);
    }

    @Override
    public ItemStack getCurrentItem() {
        return getStackInSlot(0);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = getStackInSlot(index);
        if (stack == null) {
            return null;
        }
        ItemStack result = stack.copy();
        if(count >= stack.stackSize) {
            setInventorySlotContents(index, null);
        } else {
            stack.stackSize = stack.stackSize - count;
            result.stackSize = count;
            setInventorySlotContents(index, stack);
        }
        return result;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack stack = getStackInSlot(index);
        if(stack == null) {
            return null;
        }
        setInventorySlotContents(index, null);
        return stack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if(index < 0) {
            return;
        }
        super.setInventorySlotContents(index, stack);
        notifyListenersOfSlotChange(index, stack);
    }

    private void notifyListenersOfSlotChange(int index, ItemStack stack) {
        if(!getSettler().getWorld().isRemote) {
            for(IListener listener : listeners) {
                listener.onInventorySlotChange(getSettler(), index, stack);
            }
        }
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return settler.settlement().isMayor(player);
    }

    @Override
    public void openInventory(EntityPlayer player) {}

    @Override
    public void closeInventory(EntityPlayer player) {}

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if(stack == null) {
            return true;
        }
        if(index < this.mainInventory.length) {
            return true;
        }
        index = index - mainInventory.length;
        if(index < this.armorInventory.length) {
            if (stack.getItem() instanceof ItemArmor) {
                ItemArmor item = (ItemArmor) stack.getItem();
                return item.armorType.ordinal() - 2 == index;
            }
            return false;
        }
        index = index - armorInventory.length;
        return index < offHandInventory.length;
    }

    @Override
    public String getName() {
        return "settlercraft.inventorySettler";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName());
    }

    private boolean isSameItem(ItemStack a, ItemStack b) {
        return ItemStack.areItemsEqual(a, b) && ItemStack.areItemStackTagsEqual(a, b);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void changeCurrentItem(int direction) {}

    @Override
    public void decrementAnimations() {
        for (int i = 0; i < getSizeInventory(); i++) {
            ItemStack stack = getStackInSlot(i);
            if (stack != null) {
                stack.updateAnimation(getSettler().getWorld(), getSettler(), i, i == 0);
            }
        }
    }
}
