package com.InfinityRaider.settlercraft.api.v1;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface IInventorySettler extends IInventory {
    ISettler getSettler();

    ItemStack getEquippedItem();

    void setEquippedItem(ItemStack stack);

    ItemStack getArmorItemInSlot(int slot);

    void setArmorItemInSlot(ItemStack stack, int slot);

    NBTTagCompound writeToNBT();

    void readFromNBT(NBTTagCompound tag);
}
