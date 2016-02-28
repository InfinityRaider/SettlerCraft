package com.InfinityRaider.settlercraft.api.v1;

import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;

import java.util.List;

/**
 * This interface represents a building built within a settlement,
 *
 * it is used internally and should not be implemented,
 * it can be used to interact with a building in a settlement
 */
public interface ISettlementBuilding {
    /**
     * @return the building built here
     */
    IBuilding building();

    /**
     * Checks if a settler can do work here, work can be:
     *   - Building the building if the structure is incomplete
     *   - Performing tasks scheduled by the IBuilding
     *   - Learning a new profession or increasing knowledge if the IBuilding is an academy
     *   - ...
     *
     * @param settler the settler to be assigned a task
     * @return if the settler can do a task here
     */
    boolean canDoWorkHere(ISettler settler);

    /**
     * @return a list of settlers living in this building (may be empty, but will never be null)
     */
    List<? extends ISettler> inhabitants();

    /**
     * @return the settlement where this is built
     */
    ISettlement settlement();

    /**
     * Checks if a settler can make this his home
     * @param settler the settler to be assigned this building as home
     * @return if the settler can live here
     */
    boolean canLiveHere(ISettler settler);

    /**
     * @return if this building is fully constructed
     */
    boolean isComplete();

    /**
     * @return the items currently stored in this building
     */
    IInventory inventory();

    /**
     * This always returns the lowest coordinates
     * @return The absolute BlockPosition of the initial block in the world
     */
    BlockPos position();

    /**
     * This is the position which is given to settlers as their home position when they live in this building,
     * It is defined in the schematic json
     * @return the absolute BlockPosition for the home position
     */
    BlockPos homePosition();

    /**
     * @return the x size of this building
     */
    int sizeX();

    /**
     * @return the y size of this building
     */
    int sizeY();

    /**
     * @return the z size of this building
     */
    int sizeZ();

    /**
     * @return the bounding box encompassing this building
     */
    IBoundingBox getBoundingBox();

    /**
     * @return the rotation for this building
     */
    int getRotation();

    /**
     * Checks if a set of coordinates intersects with this building
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param z the z-coordinate
     * @return if the coordinates intersect with the building
     */
    boolean isInsideBuilding(int x, int y, int z);

    /**
     * The actual structure of the building in a settlement might be rotated and offset, this method transforms a BlockPos
     * relative to the building to an absolute BlockPos (representing the actual position in the world)
     *
     * @param pos the position, relative to the origin of the building to be transformed
     * @return the absolute position
     */
    BlockPos getActualPosition(BlockPos pos);

    NBTTagCompound writeToNBT();

    void readFromNBT(NBTTagCompound tag);
}
