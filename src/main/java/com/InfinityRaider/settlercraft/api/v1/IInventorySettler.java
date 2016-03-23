package com.InfinityRaider.settlercraft.api.v1;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

import java.util.List;
import java.util.Map;

public interface IInventorySettler extends IInventorySerializable {
    ISettler getSettler();

    ItemStack getEquippedItem(EnumHand hand);

    void setEquippedItem(EnumHand hand, ItemStack stack);

    ItemStack getEquipmentInSlot(EntityEquipmentSlot slot);

    void setEquipmentInSlot(EntityEquipmentSlot slot, ItemStack stack);

    Map<EntityEquipmentSlot, ItemStack> getEquipmentMap();

    List<ItemStack> getEquipmentList();

    int getSlotForStack(ItemStack stack);

    ItemStack[] toArray();
}
