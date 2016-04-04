package com.InfinityRaider.settlercraft.api.v1;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

import java.util.List;
import java.util.Map;

/**
 * Interface for the settler's inventory, this is used to interact with the settler's inventory and should not be implemented by you
 * A settler's inventory consists of the following slots:
 *  - 1 main hand slot (id: 0)
 *  - 1 off hand slot (id: 1)
 *  - 4 armor slots (ids: 2 - 5)
 *  - 36 inventory slots (ids: 6 - 41)
 */
public interface IInventorySettler extends IInventorySerializable {
    /**
     * @return The settler which inventory this is
     */
    ISettler getSettler();

    /**
     * Gets the item equipped in the hand of the settler
     * @param hand the hand to retrieve the item from
     * @return item stack held in the hand
     */
    ItemStack getEquippedItem(EnumHand hand);

    /**
     * Sets the item equipped in the hand of the settler
     * @param hand the hand to equip the item in
     * @param stack item stack to be equipped
     */
    void setEquippedItem(EnumHand hand, ItemStack stack);

    /**
     * Gets an item equipped in an equipment slot
     * @param slot equipment slot to retrieve an item from
     * @return item stack equipped in the slot
     */
    ItemStack getEquipmentInSlot(EntityEquipmentSlot slot);

    /**
     * Sets the item equipped in an equipment slot
     * @param slot equipment slot to equip item in
     * @param stack stack to be equipped in the slot
     */
    void setEquipmentInSlot(EntityEquipmentSlot slot, ItemStack stack);

    /**
     * @return a map for the settler's current equipment
     */
    Map<EntityEquipmentSlot, ItemStack> getEquipmentMap();

    /**
     * Gets a list for the settler's current equipment, ordered by slot id
     * @return a list holding the settler's equipment
     */
    List<ItemStack> getEquipmentList();

    /**
     * Finds the first slot id which contains an item equal to the one passed in the stack, stacksize is ignored
     * @param stack stack to find
     * @return index for the stack, -1 if the settler does not have such an item in its inventory
     */
    int getSlotForStack(ItemStack stack);

    /**
     * Finds the n' th slot id which contains an item equal to the one passed in the stack, stacksize is ignored
     * @param stack stack to find
     * @param n amount of slots - 1 to skip when looking for the item
     * @return index for the stack, -1 if the settler does not have such an item in its inventory
     */
    int getSlotForStack(ItemStack stack, int n);

    /**
     * Checks if a settler has an item equal to the one passed in the stack, stacksize is ignored
     * @param stack stack to find
     * @return if the settler has such an item in its inventory
     */
    boolean hasStack(ItemStack stack);

    /**
     * Consumes an amount of items equal to the one passed in the stack.
     * The stack's stacksize is the amount of items consumed.
     * @param stack stack to be consumed
     */
    void consumeStack(ItemStack stack);

    /**
     * Converts the inventory to an array, where the index in the array corresponds to the index in the inventory
     * @return array representation of the inventory
     */
    ItemStack[] toArray();
}
