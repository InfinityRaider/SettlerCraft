package com.InfinityRaider.settlercraft.settlement.settler;

import com.InfinityRaider.settlercraft.api.v1.IInventorySettler;
import com.InfinityRaider.settlercraft.reference.Names;
import net.minecraft.block.state.IBlockState;
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

    private List<IListener> listeners;

    protected InventorySettler(EntitySettler settler) {
        this.settler = settler;
        if(!settler.worldObj.isRemote) {
            this.listeners = new ArrayList<>();
        }
        this.mainInventory = new ItemStack[36];
        this.armorInventory = new ItemStack[4];
    }

    @Override
    public EntitySettler getSettler() {
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
    public ItemStack[] getMainInventory() {
        return mainInventory;
    }

    @Override
    public ItemStack[] getArmorInventory() {
        return armorInventory;
    }

    @Override
    public ItemStack addStackToInventory(ItemStack stack) {
        if(stack == null || stack.getItem() == null || stack.stackSize == 0) {
            return stack;
        }
        ItemStack remaining = stack.copy();
        //try to merge with existing stacks
        for(int i = 0; i < this.getSizeInventory() && remaining.stackSize > 0; i++) {
            if(!isItemValidForSlot(i, remaining)) {
                continue;
            }
            ItemStack stackInSlot = getStackInSlot(i);
            if(isSameItem(remaining, stackInSlot)) {
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
        int slot = -1;
        if (stack == null || stack.getItem() == null) {
            return -1;
        }
        if (isSameItem(stack, active)) {
            n = n - 1;
            if (n <= 0) {
                return 0;
            }
        }
        if (isSameItem(stack, offhand)) {
            n = n - 1;
            if (n <= 0) {
                return 1;
            }
        }
        for (int i = 0; i < mainInventory.length; i++) {
            ItemStack inSlot = this.getStackInSlot(i + 2 + armorInventory.length);
            if (isSameItem(stack, inSlot)) {
                n = n - 1;
                if (n <= 0) {
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
        ItemStack[] inv = new ItemStack[2 + armorInventory.length + mainInventory.length];
        inv[0] = active;
        inv[1] = offhand;
        System.arraycopy(armorInventory, 0, inv, 2, armorInventory.length);
        System.arraycopy(mainInventory, 0, inv, 2 + armorInventory.length, mainInventory.length);
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
    public ItemStack getCurrentItem() {
        return getStackInSlot(0);
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

    public void decrementAnimations() {
        for (int i = 0; i < getSizeInventory(); i++) {
            ItemStack stack = getStackInSlot(i);
            if (stack != null) {
                stack.updateAnimation(getSettler().getWorld(), getSettler(), i, i == 0);
            }
        }
    }

    public float getStrVsBlock(IBlockState state) {
        float f = 1.0F;
        if (active != null) {
            f *= active.getStrVsBlock(state);
        }
        return f;
    }

    public boolean canHarvestBlock(IBlockState state) {
        if (state.getMaterial().isToolNotRequired()) {
            return true;
        } else {
            return active != null && active.canHarvestBlock(state);
        }
    }

    public void damageArmor(float damage) {
        damage = damage / 4.0F;
        if (damage < 1.0F) {
            damage = 1.0F;
        }
        for (int i = 0; i < this.armorInventory.length; ++i) {
            if (this.armorInventory[i] != null && this.armorInventory[i].getItem() instanceof ItemArmor) {
                this.armorInventory[i].damageItem((int)damage, this.getSettler());
                if (this.armorInventory[i].stackSize == 0) {
                    this.armorInventory[i] = null;
                }
            }
        }
    }
}
