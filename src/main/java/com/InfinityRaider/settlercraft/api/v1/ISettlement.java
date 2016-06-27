package com.InfinityRaider.settlercraft.api.v1;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nullable;
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
     * Settlement tier is an arbitrary numbering system based on the town hall, it can be used to quickly determine if a building can be built.
     * The more advanced the town hall, the more advanced buildings can be built.
     *  - Tier 0 means there is no town hall meaning a town hall has to be built.
     *  - Tier 1 means there is a basic town hall.
     *  - Tier 2 means that there is more advanced townhall
     *  - ...
     * @return the tier of the settlement
     */
    int tier();

    /**
     * @return The name of this settlement (decided by the player)
     */
    String name();

    /**
     * Renames this settlement
     * @param name the new name of the settlement
     */
    void rename(String name);

    /**
     * @return the building style for this settlement
     */
    IBuildingStyle getBuildingStyle();

    /**
     * Gets a building in this settlement by its id
     * @param id of the building
     * @return building with that id, or null if there is no building with this id
     */
    ISettlementBuilding getBuildingFromId(int id);

    /**
     * @return a list of all buildings currently built or being built in this settlement
     */
    List<ISettlementBuilding> getBuildings();

    /**
     * @return a list of all completed buildings currently built in this settlement
     */
    List<ISettlementBuilding> getCompletedBuildings();

    /**
     * @param buildingType a building type
     * @return a list of all buildings of a specific building type currently built or being built in this settlement
     */
    List<ISettlementBuilding> getBuildings(IBuildingType buildingType);

    /**
     * @param buildingType a building type
     * @return a list of all completed buildings of a specific building type in this settlement
     */
    List<ISettlementBuilding> getCompletedBuildings(IBuildingType buildingType);

    /**
     * Checks if a specific building is built
     * @param building specific building
     * @return if the building is built int his settlement
     */
    boolean hasBuilding(IBuilding building);

    /**
     * Gets a list of all buildings of a type which can be built in this settlement
     * @param type building type to check buildings for
     * @return a list of buildings which can be built in this settlement for a given building type at the current time
     */
    List<IBuilding> getBuildableBuildings(IBuildingType type);

    /**
     * Checks if all the requirements are met for a building to be built in this settlement
     * @param building the building to be built
     * @return if the building can be built
     */
    boolean canBuildNewBuilding(IBuilding building);

    /**
     * Checks and tries to build the building with the applied rotation at the position,
     * this calls canBuildNewBuilding(building) internally
     * @param player player trying to build the new building
     * @param building the building to build
     * @param pos the position at which to build the building
     * @param rotation the rotation to build the building with
     * @return the ISettlementBuilding for the building if the building was successfully added, or null if the building can not be built here
     */
    @Nullable
    ISettlementBuilding tryBuildNewBuildingAtLocation(EntityPlayer player, IBuilding building, BlockPos pos, int rotation);

    /**
     * Removes a building from this settlement
     * @param building the building to be removed
     */
    void removeBuilding(ISettlementBuilding building);

    /**
     * Called when a settler joins this settlement
     * @param settler a settler which joined this settlement
     */
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
     * @return if the building was fully updated
     */
    boolean onBuildingUpdated(ISettlementBuilding building);

    /**
     * Called to serialize the settlement's data and save it
     * @param tag NBT tag to serialize data to
     * @return the tag passed as argument, with additional data written to it
     */
    NBTTagCompound writeSettlementToNBT(NBTTagCompound tag);

    /**
     * Called to deserialize the settlement's data and load it
     * @param tag NBT tag to deserialize data from
     * @return the tag passed as argument
     */
    NBTTagCompound readSettlementFromNBT(NBTTagCompound tag);
}
