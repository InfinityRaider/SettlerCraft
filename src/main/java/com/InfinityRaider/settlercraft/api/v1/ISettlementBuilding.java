package com.InfinityRaider.settlercraft.api.v1;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * This interface represents a building built within a settlement,
 *
 * it is used internally and should not be implemented,
 * it can be used to interact with a building in a settlement
 */
public interface ISettlementBuilding {

    /**
     * This method returns the id of this building in its settlement. An id is final and will not change within a settlement.
     * @return this building's id in the settlement
     */
    int id();

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
     * Sets this building to the settler's workplace, only works on the server thread and is synced to the client automatically
     * @param settler the settler
     */
    void addWorker(ISettler settler);

    /**
     * Checks if a settler can make this his home
     * @param settler the settler to be assigned this building as home
     * @return if the settler can live here
     */
    boolean canLiveHere(ISettler settler);

    /**
     * Sets this building to the settler's home, only works on the server thread and is synced to the client automatically
     * @param settler the settler
     */
    void addInhabitant(ISettler settler);

    /**
     * If the settler can do work for this building ( true returned from canDoWorkHere(settler) ),
     * this method will be called to get a list of task for the settler to fulfill.
     * For incomplete buildings this call will assign the settler to build the building,
     * for completed buildings the call will be forwarded to the IBuilding object.
     *
     * @param settler the settler needing work
     * @return a List holding tasks for the settler, can be empty but should never be null
     */
    ITask getTaskForSettler(ISettler settler);

    /**
     * Gets all settlers living in this building, note that this list may be incomplete if the settlement is not chunk loaded
     * If you need the number of settlers living in this building, use inhabitantCount() instead.
     * @return a list of settlers living in this building (may be empty, but will never be null)
     */
    List<? extends ISettler> inhabitants();

    /**
     * Gets all settlers working in this building, note that this list may be incomplete if the settlement is not chunk loaded
     * If you need the number of settlers working in this building, use inhabitantCount() instead.
     * @return a list of settlers working in this building (may be empty, but will never be null)
     */
    List<? extends ISettler> workers();

    /**
     * @return the number of workers
     */
    int workerCount();

    /**
     * @return the number of inhabitants
     */
    int inhabitantCount();

    /**
     * Checks if this settler lives in this building
     * @param settler the settler
     * @return true if the settler lives here, false if not
     */
    boolean doesSettlerLiveHere(ISettler settler);

    /**
     * Checks if this settler works in this building
     * @param settler the settler
     * @return true if the settler works here, false if not
     */
    boolean doesSettlerWorkHere(ISettler settler);

    /**
     * @return the settlement where this is built, this can be null if the chunk with this building is loaded, but the settlement's home chunk isn't.
     */
    ISettlement settlement();

    /**
     * @return if this building is fully constructed
     */
    boolean isComplete();

    /**
     * @return the items currently stored in this building
     */
    IInventoryBuilding inventory();

    /**
     * @return The world object this building is in
     */
    World getWorld();

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
    boolean isInsideBuilding(double x, double y, double z);

    /**
     * Gets a list of all block positions for beds inside this building.
     * @return list of positions
     */
    List<BlockPos> getBeds();

    /**
     * The actual structure of the building in a settlement might be rotated and offset, this method transforms a BlockPos
     * relative to the building to an absolute BlockPos (representing the actual position in the world)
     * For instance, if the schematic of the building has a certain block at coordinates (5, 1, 3) in the schematic and you wish to get the position of this chest,
     * call this method with a BlockPos argument with coordinates of (5, 1, 3) to get the BlockPos of that block in the world.
     *
     * @param pos the position, relative to the origin of the building to be transformed
     * @return the absolute position
     */
    BlockPos getActualPosition(BlockPos pos);

    /**
     * Called when the building is completed, used to scan the building for beds and inventories
     */
    void onBuildingCompleted();

    /**
     * Called right before a block inside this building's bounding box is broken
     * @param player the player breaking the block (can be null)
     * @param pos the position of the broken block
     * @param state the state of the broken bock
     */
    void onBlockBroken(EntityPlayer player, BlockPos pos, IBlockState state);

    /**
     * Called right before a block inside this building's bounding box is placed
     * @param player the player placing the block (can be null)
     * @param pos the position of the placed block
     * @param state the state of the placed bock
     */
    void onBlockPlaced(EntityPlayer player, BlockPos pos, IBlockState state);

    /**
     * Call this on the server to mark this settlement dirty,
     * when marked dirty, the settlement for this building will be saved to the disk
     * This is equivalent to calling markDirty() on the settlement for this building
     */
    void markDirty();

    /**
     * Call this on the server to sync this building to the client, it is faster than syncing the entire settlement to the client
     * Call this if only the building has changed (for instance, the workers or inhabitants have changed, or the inventory is changed),
     * If the changes have affected the entire settlement, sync the settlement to the client instead (syncing the settlement will also sync all buildings)
     */
    void syncToClient();

    /**
     * Called to serialize the building's data and save it
     * @param tag NBT tag to serialize data to
     * @return the tag passed as argument, with additional data written to it
     */
    NBTTagCompound writeBuildingToNBT(NBTTagCompound tag);

    /**
     * Called to deserialize the building's data and load it
     * @param tag NBT tag to deserialize data from
     * @return the tag passed as argument
     */
    NBTTagCompound readBuildingFromNBT(NBTTagCompound tag);
}
