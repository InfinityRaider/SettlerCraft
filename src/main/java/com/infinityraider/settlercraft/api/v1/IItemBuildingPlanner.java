package com.infinityraider.settlercraft.api.v1;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * This interface is implemented in the building planner Item,
 * it is meant for you to interact with and not to implement yourself.
 * You can get the IItemBuildingPlanner from the SettlerCraftItemRegistry.
 */
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
    ISettlement getSettlement(World world, ItemStack stack);

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
     * Increments the rotation of the planned building by 90�
     *
     * @param stack the stack holding the planner
     * @return this, allows for chaining method calls
     */
    IItemBuildingPlanner rotate(ItemStack stack);

    /**
     * Gets the rotation of the planned building, the return will be a number between 0 and 4:
     * 0 -> 0�
     * 1 -> 90�
     * 2 -> 180�
     * 3 -> 270�
     *
     * @param stack the stack holding the planner
     * @return this, allows for chaining method calls
     */
    int getRotation(ItemStack stack);

    /**
     * Sets the rotation of the planned building, argument should be a number between 0 and 4:
     * 0 -> 0�
     * 1 -> 90�
     * 2 -> 180�
     * 3 -> 270�
     *
     * @param stack the stack holding the planner
     * @param rotation the rotation
     * @return this, allows for chaining method calls
     */
    IItemBuildingPlanner setRotation(ItemStack stack, int rotation);

    /**
     * Checks if the bounding box for a new building is valid for a settlement
     *
     * @param stack the stack holding the planner
     * @param player the player using the planner
     * @param settlement the settlement being planned for
     * @param building the building trying to be built in the bounding box
     * @param buildingBox the bounding box for the new building
     * @return true if the building can be built here, false if not
     */
    boolean isValidBoundingBoxForBuilding(World world, ItemStack stack, EntityPlayer player, ISettlement settlement, IBuilding building, IBoundingBox buildingBox);
}
