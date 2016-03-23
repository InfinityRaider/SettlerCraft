package com.InfinityRaider.settlercraft.api.v1;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.List;

/**
 * This interface is used internally and should not be implemented,
 * it can be used to interact with settlements
 */
public interface ISettlement extends ITickable {
    /** Gets the id for this settlement */
    int id();

    /**
     * @return The world object in which this settlement is built
     */
    World world();

    /**
     * @return the homeChunk for this settlement (aka the chunk this settlement was created in)
     */
    Chunk homeChunk();

    /**
     * @return The player who founded this settlement
     */
    EntityPlayer mayor();

    /**
     * Checks if a player is the mayor of this settlement
     * @param player the player
     * @return if the player is the mayer
     */
    boolean isMayor(EntityPlayer player);

    /**
     * @return The name of this settlement (decided by the player)
     */
    String name();

    /**
     * Renames this settlement
     * @param name the new name of the settlement
     */
    void rename(String name);

    ISettlementBuilding getBuildingFromId(int id);

    /**
     * @return a list of all buildings currently built in this settlement
     */
    List<ISettlementBuilding> getBuildings();

    /**
     * @param buildingType a building type
     * @return a list of all buildings of a specific building type currently built in this settlement
     */
    List<ISettlementBuilding> getBuildings(IBuildingType buildingType);

    /**
     * Checks if a specific building is built
     * @param building specific building
     * @return if the building is built int his settlement
     */
    boolean hasBuilding(IBuilding building);

    /**
     * @return a list of buildings which can be built in this settlement at the current time
     */
    List<IBuilding> getBuildableBuildings();

    /**
     * Checks if all the requirements are met for a building to be built in this settlement
     * @param building the building to be built
     * @return if the building can be built
     */
    boolean canBuildNewBuilding(IBuilding building);

    /**
     * Adds a new building to this settlement
     * @param building the building to be added
     */
    void addBuilding(ISettlementBuilding building);

    /**
     * Removes a building from this settlement
     * @param building the building to be removed
     */
    void removeBuilding(ISettlementBuilding building);

    void addInhabitant(ISettler settler);

    /**
     * @return a list of all settlers living in this settlement
     */
    List<ISettler> getSettlementInhabitants();

    /**
     * @return the total population count (including the mayor)
     */
    int population();

    /**
     * Checks if a set of coordinates is within this settlement or not
     * @param x x-coordinate
     * @param y y-coordinate
     * @param z z-coordinate
     * @return if the coordinates are inside the bounds of this settlement
     */
    boolean isWithinSettlementBounds(double x, double y, double z);

    /**
     * @return the x size of the settlement
     */
    int xSize();

    /**
     * @return the y size of the settlement
     */
    int ySize();

    /**
     * @return the z size of the settlement
     */
    int zSize();

    /**
     * @return the bounding box encompassing this settlement
     */
    IBoundingBox getBoundingBox();

    /**
     * Calculates the squared distance between this settlement's starting point and a position
     * @param pos the position
     * @return the squared distance
     */
    double calculateDistanceSquaredToSettlement(BlockPos pos);

    /**
     * This is called from the argument, it is called when the building is updated and synced from the client.
     * This way the settlement knows this building has updated.
     * @param building the building which has been updated
     */
    void onBuildingUpdated(ISettlementBuilding building);

    NBTTagCompound writeSettlementToNBT(NBTTagCompound tag);

    NBTTagCompound readSettlementFromNBT(NBTTagCompound tag);
}
