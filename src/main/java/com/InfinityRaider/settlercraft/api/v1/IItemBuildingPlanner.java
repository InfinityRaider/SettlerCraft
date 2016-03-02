package com.InfinityRaider.settlercraft.api.v1;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IItemBuildingPlanner {
    /**
     * The implementation of this method is simply 'return this;'
     * But this is, in my opinion, much cleaner than an instanceof check and then a typecast
     *
     * @return the Item instance
     */
    Item getItem();

    /**
     * Gets the settlement this stack is for, this stack can only be used by the mayor of this settlement, inside this settlement
     * This may return null, but if everything is well, it should never return null.
     *
     * @param stack the stack holding the planner
     * @return the settlement
     */
    ISettlement getSettlement(ItemStack stack);

    /**
     * Sets the settlement this stack is for, this stack can only be used by the mayor of this settlement, inside this settlement
     *
     * @param stack the stack holding the planner
     * @param settlement the settlement
     * @return this, allows for chaining method calls
     */
    IItemBuildingPlanner setSettlement(ItemStack stack, ISettlement settlement);

    /**
     * Gets the building this stack is currently set to, can return null if no building is currently assigned
     *
     * @param stack the stack holding the planner
     * @return the building
     */
    IBuilding getBuilding(ItemStack stack);

    /**
     * Sets the building which the player can plan a build for using this stack
     *
     * @param stack the stack holding the planner
     * @param building the building
     * @return this, allows for chaining method calls
     */
    IItemBuildingPlanner setBuilding(ItemStack stack, IBuilding building);

    /**
     * Increments the rotation of the planned building by 90°
     *
     * @param stack the stack holding the planner
     * @return this, allows for chaining method calls
     */
    IItemBuildingPlanner rotate(ItemStack stack);

    /**
     * Gets the rotation of the planned building, the return will be a number between 0 and 4:
     * 0 -> 0°
     * 1 -> 90°
     * 2 -> 180°
     * 3 -> 270°
     *
     * @param stack the stack holding the planner
     * @return this, allows for chaining method calls
     */
    int getRotation(ItemStack stack);

    /**
     * Sets the rotation of the planned building, argument should be a number between 0 and 4:
     * 0 -> 0°
     * 1 -> 90°
     * 2 -> 180°
     * 3 -> 270°
     *
     * @param stack the stack holding the planner
     * @param rotation the rotation
     * @return this, allows for chaining method calls
     */
    IItemBuildingPlanner setRotation(ItemStack stack, int rotation);
}
