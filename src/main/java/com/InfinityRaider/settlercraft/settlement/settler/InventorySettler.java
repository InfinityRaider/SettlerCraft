package com.InfinityRaider.settlercraft.settlement.settler;

import com.InfinityRaider.settlercraft.api.v1.IInventorySettler;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.reference.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventorySettler implements IInventorySettler {
    private ItemStack[] mainInventory;
    private ItemStack[] armorInventory;
    private ItemStack active;
    private ItemStack offhand;

    private EntitySettler settler;

    protected InventorySettler(EntitySettler settler) {
        this.settler = settler;
        this.mainInventory = new ItemStack[36];
        this.armorInventory = new ItemStack[4];
    }

    @Override
    public ISettler getSettler() {
        return settler;
    }

    @Override
    public ItemStack getEquippedItem(EnumHand hand) {
        switch(hand) {
            case MAIN_HAND: return active;
            case OFF_HAND: return offhand;
            default: return null;
        }
    }

    @Override
    public void setEquippedItem(EnumHand hand, ItemStack stack) {
        switch(hand) {
            case MAIN_HAND: active = stack; break;
            case OFF_HAND: offhand = stack; break;
        }
    }

    @Override
    public ItemStack getEquipmentInSlot(EntityEquipmentSlot slot) {
        switch(slot) {
            case MAINHAND: return active;
            case OFFHAND: return offhand;
            case FEET: return armorInventory[3];
            case LEGS: return armorInventory[2];
            case CHEST: return armorInventory[1];
            case HEAD: return armorInventory[0];
            default: return null;
        }
    }

    @Override
    public void setEquipmentInSlot(EntityEquipmentSlot slot, ItemStack stack) {
        switch(slot) {
            case MAINHAND: active = stack; break;
            case OFFHAND: offhand = stack; break;
            case FEET:
                if(isItemValidForSlot(2, stack)) {
                    armorInventory[3] = stack;
                }
                break;
            case LEGS:
                if(isItemValidForSlot(3, stack)) {
                    armorInventory[2] = stack;
                }
                break;
            case CHEST:
                if(isItemValidForSlot(4, stack)) {
                    armorInventory[1] = stack;
                }
                break;
            case HEAD:
                if(isItemValidForSlot(5, stack)) {
                    armorInventory[0] = stack;
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
        if(active != null) {
            list.add(active);
        }
        if(offhand != null) {
            list.add(offhand);
        }
        for(ItemStack stack : armorInventory) {
            if(stack != null) {
                list.add(stack);
            }
        }
        return list;
    }

    @Override
    public int getSlotForStack(ItemStack stack) {
        int slot = -1;
        if(stack == null || stack.getItem() == null) {
            return slot;
        }
        if(isSameItem(stack, active)) {
            slot = 0;
        } else if(isSameItem(stack, offhand)) {
            slot = 1;
        } else {
            for (int i = 0; i < mainInventory.length; i++) {
                ItemStack inSlot = this.getStackInSlot(i + 1 + armorInventory.length);
                if(isSameItem(stack, inSlot)) {
                    slot = i + 2 + armorInventory.length;
                    break;
                }
            }
        }
        return slot;
    }

    @Override
    public boolean hasStack(ItemStack stack) {
        return stack == null || stack.getItem() == null || getSlotForStack(stack) >= 0;
    }

    @Override
    public void consumeStack(ItemStack stack) {
        if(stack != null) {
            int index = getSlotForStack(stack);
            this.decrStackSize(index, stack.stackSize);
        }
    }

    @Override
    public ItemStack[] toArray() {
        ItemStack[] inv = new ItemStack[2 + armorInventory.length + mainInventory.length];
        inv[0] = active;
        inv[1] = offhand;
        System.arraycopy(armorInventory, 0, inv, 2, armorInventory.length);
        System.arraycopy(mainInventory, 0, inv, 2 + armorInventory.length, mainInventory.length);
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
        return 2 + armorInventory.length + mainInventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        if(index < 0) {
            return null;
        }
        if(index == 0) {
            return active;
        }
        if(index == 1) {
            return offhand;
        }
        index = index - 2;
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
        } else if(index == 1) {
            offhand = stack;
        } else {
            index = index - 2;
            if (index < armorInventory.length) {
                armorInventory[index] = stack;
            } else {
                index = index - armorInventory.length;
                if (index < mainInventory.length) {
                    mainInventory[index] = stack;
                } else {
                    return;
                }
            }
        }
        if(isSameItem(stack, getSettler().getMissingResource())) {
            getSettler().setMissingResource(null);
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
        if(index == 0 || index == 1 || index >= 2 + armorInventory.length) {
            return true;
        }
        if(stack.getItem() instanceof ItemArmor) {
            ItemArmor item = (ItemArmor) stack.getItem();
            return item.armorType.ordinal() - 2 == 3 - (index - 2);
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
        this.offhand = null;
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
    public ITextComponent getDisplayName() {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName());
    }

    private boolean isSameItem(ItemStack a, ItemStack b) {
        return ItemStack.areItemsEqual(a, b) && ItemStack.areItemStackTagsEqual(a, b);
    }
}
