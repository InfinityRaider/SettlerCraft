package com.InfinityRaider.settlercraft.api.v1;

import net.minecraft.item.ItemStack;

public interface IInventorySettler extends IInventorySerializable {
    ISettler getSettler();

    ItemStack getEquippedItem();

    void setEquippedItem(ItemStack stack);

    ItemStack getArmorItemInSlot(int slot);

    void setArmorItemInSlot(ItemStack stack, int slot);

    int getSlotForStack(ItemStack stack);

    ItemStack[] toArray();
}
