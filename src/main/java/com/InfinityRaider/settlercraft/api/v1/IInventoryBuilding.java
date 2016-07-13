package com.InfinityRaider.settlercraft.api.v1;

import net.minecraft.inventory.IInventory;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;

/**
 * This interface is the bridge between a building and all the inventories inside,
 * when the building is completed, it will scan its bounding box for any TileEntities which implement IInventory.
 * This class is used by settlers to access those inventories
 */
public interface IInventoryBuilding extends IInventorySerializable {
    /**
     * Gets the BlockPos where the Block and TileEntity of this inventory are in the world
     * @param inventory the inventory
     * @return the position of that inventory, or null if this inventory is not inside the building
     */
    BlockPos getPositionForInventory(IInventory inventory);

    /**
     * Gets the IInventory at a position
     * @param pos the position
     * @return the inventory at that position, or null if there is no inventory at that position
     */
    IInventory getInventoryForPosition(BlockPos pos);

    /**
     * Converts the global slot index of this inventory to the corresponding inventory and the slot inside that inventory
     * @param slot global slot index
     * @return a Tuple containing the inventory and the slot index for that inventory corresponding tot he global slot index, or null if the argument is larger than the inventory size
     */
    Tuple<IInventory, Integer> getInventoryAndSlotForGlobalSlot(int slot);

    /**
     * @return The building of which this is the inventory
     */
    ISettlementBuilding getBuilding();
}
