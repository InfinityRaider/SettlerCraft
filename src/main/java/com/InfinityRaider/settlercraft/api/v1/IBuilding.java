package com.InfinityRaider.settlercraft.api.v1;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * This interface is used to construct buildings, only one instance per building is created and registered
 *
 * If you are creating a whole new building type, the IBuilding objects for that type will be automatically registered (assuming your implementation is correct)
 * If you want to add a new building to an existing building type, retrieve that building type from the IBuildingTypeRegistry (which can be retrieved via the APIv1)
 * and call IBuildingType.registerBuilding(IBuilding building)
 */
public interface IBuilding {
    /**
     * @return a name unique to this building, this should be an unlocalized string
     */
    String name();

    /**
     * @return The building type this building belongs to
     */
    IBuildingType buildingType();

    /**
     * This method is called when the player wants to build this building in his settlement,
     * use it to check if all the required prerequisites are met
     *
     * @param player the player owning the settlement
     * @param settlement the settlement
     * @return if the player is allowed to build this building in the settlement
     */
    boolean canBuild(EntityPlayer player, ISettlement settlement);

    /**
     * Every building has an inventory, this is the default starting inventory of a newly created building and will be used further.
     * It is important that a new IInventory object is returned from this method, or all buildings of this instance will share the same inventory.
     *
     * @return a new IInventory instance for the starting inventory of the new building
     */
    IInventorySerializable getDefaultInventory();

    /**
     * This method is used to read json schematics for the buildings, example:
     * new ResourceLocation("settlercraft", "buildings/house/house1") will be converted to "assets/settlercraft/buildings/house/house1.json"
     *
     * Schematics can be created by building the structure in a world and then using the schematic creator item to export the building as a json to
     * the file specified in the config.
     *
     * @return a ResourceLocation containing the path to the json file defining this building
     */
    ResourceLocation schematicLocation();

    /**
     * @return the maximum number of settlers living in this building
     */
    int maxInhabitants();

    /**
     * Checks if a settler can work here, this is called when a settler is idle and needs somewhere to work
     * @param building the ISettlementBuilding object where this is built
     * @param settler the settler wanting to work here
     * @return if the settler can work here
     */
    boolean canSettlerWorkHere(ISettlementBuilding building, ISettler settler);

    /**
     * If the settler can do work for this building ( true returned from canDoWorkHere(settler) ),
     * this method will be called to get a task for the settler to fulfill.
     *
     * @param building the ISettlementBuilding object where this is built
     * @param settler the settler needing work
     * @return a task for the settler, should never be null
     */
    ITask getTaskForSettler(ISettlementBuilding building, ISettler settler);

    /**
     * This method is called right after this building has been built somewhere.
     * It can be used to perform final sets on TileEntities, spawn something, or do any operation you need.
     * @param world the World object where this building has been built in
     * @param settlement the settlement where this building has been built in
     * @param building the ISettlementBuilding object which has been built
     */
    void onBuildingBuilt(World world, ISettlement settlement, ISettlementBuilding building);

    /**
     * @return if this building needs to receive ticks
     */
    boolean needsUpdateTicks();

    /**
     * Called every tick, only if true is returned from needsUpdateTicks()
     * @param building the ISettlementBuilding object where this is built
     */
    void onUpdateTick(ISettlementBuilding building);
}
