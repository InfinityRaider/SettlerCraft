package com.InfinityRaider.settlercraft.settlement.settler;

import com.InfinityRaider.settlercraft.api.v1.IInventorySettler;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.reference.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class InventorySettler implements IInventorySettler {
    private ItemStack[] mainInventory = new ItemStack[36];
    private ItemStack[] armorInventory = new ItemStack[4];
    private ItemStack active;

    private EntitySettler settler;

    protected InventorySettler(EntitySettler settler) {
        this.settler = settler;
    }

    @Override
    public ISettler getSettler() {
        return settler;
    }

    @Override
    public ItemStack getEquippedItem() {
        return active;
    }

    @Override
    public void setEquippedItem(ItemStack stack) {
        this.active = stack;
    }

    @Override
    public ItemStack getArmorItemInSlot(int slot) {
        return armorInventory[slot % armorInventory.length];
    }

    @Override
    public void setArmorItemInSlot(ItemStack stack, int slot) {
        armorInventory[slot % armorInventory.length] = stack;
    }

    @Override
    public int getSlotForStack(ItemStack stack) {
        int slot = -1;
        if(stack == null || stack.getItem() == null) {
            return slot;
        }
        if(isSameItem(stack, active)) {
            slot = 0;
        } else {
            for (int i = 0; i < mainInventory.length; i++) {
                ItemStack inSlot = this.getStackInSlot(i + 1 + armorInventory.length);
                if(isSameItem(stack, inSlot)) {
                    slot = i + 1 + armorInventory.length;
                    break;
                }
            }
        }
        return slot;
    }

    @Override
    public ItemStack[] toArray() {
        ItemStack[] inv = new ItemStack[1 + armorInventory.length + mainInventory.length];
        inv[0] = active;
        System.arraycopy(armorInventory, 0, inv, 1, armorInventory.length);
        System.arraycopy(mainInventory, 0, inv, 1 + armorInventory.length, mainInventory.length);
        return inv;
    }

    @Override
    public NBTTagCompound writeToNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        for(int i = 0; i < this.getSizeInventory(); i++) {
            ItemStack stack = this.getStackInSlot(i);
            if(stack != null) {
                NBTTagCompound subTag = new NBTTagCompound();
                subTag.setInteger(Names.NBT.SLOT, i);
                subTag.setTag(Names.NBT.ITEM, stack.writeToNBT(new NBTTagCompound()));
                list.appendTag(subTag);
            }
        }
        tag.setTag(Names.NBT.INVENTORY, list);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        this.clear();
        if(tag.hasKey(Names.NBT.INVENTORY)) {
            NBTTagList list = tag.getTagList(Names.NBT.INVENTORY, 10);
            for(int i = 0;i < list.tagCount(); i++) {
                NBTTagCompound subTag = list.getCompoundTagAt(i);
                this.setInventorySlotContents(subTag.getInteger(Names.NBT.SLOT), ItemStack.loadItemStackFromNBT(subTag.getCompoundTag(Names.NBT.ITEM)));
            }
        }
    }

    @Override
    public int getSizeInventory() {
        return 1 + armorInventory.length + mainInventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        if(index < 0) {
            return null;
        }
        if(index == 0) {
            return active;
        }
        index = index - 1;
        if(index < armorInventory.length) {
            return armorInventory[index];
        }
        index = index - armorInventory.length;
        if(index < mainInventory.length) {
            return mainInventory[index];
        }
        return null;
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
        if(index == 0) {
            active = stack;
            return;
        }
        index = index - 1;
        if(index < armorInventory.length) {
            armorInventory[index] = stack;
            return;
        }
        index = index - armorInventory.length;
        if(index < mainInventory.length) {
            mainInventory[index] = stack;
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {}

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
        if(index == 0 || index >= 1 + armorInventory.length) {
            return true;
        }
        if(stack.getItem() instanceof ItemArmor) {
            ItemArmor item = (ItemArmor) stack.getItem();
            return item.armorType == index - 1;
        }
        return false;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) { }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        this.active = null;
        for(int i = 0; i < mainInventory.length; i++) {
            mainInventory[i] = null;
        }
        for(int i = 0; i < armorInventory.length; i++) {
            armorInventory[i] = null;
        }
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
    public IChatComponent getDisplayName() {
        return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName());
    }

    private boolean isSameItem(ItemStack a, ItemStack b) {
        return ItemStack.areItemsEqual(a, b) && ItemStack.areItemStackTagsEqual(a, b);
    }
}
