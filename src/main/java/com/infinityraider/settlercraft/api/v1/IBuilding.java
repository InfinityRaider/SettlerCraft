package com.infinityraider.settlercraft.api.v1;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

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
     * use it to check if all the required prerequisites are met.
     * This is called from within the settlement code from ISettlement.canBuildNewBuilding(IBuilding building),
     * don't call that method from within this method to prevent infinite loops.
     * The maximum building count per building type has already been checked from the settlement code
     *
     * @param player the player owning the settlement
     * @param settlement the settlement
     * @return if the player is allowed to build this building in the settlement
     */
    boolean canBuild(EntityPlayer player, ISettlement settlement);

    /**
     * This method is used to read json schematics for the building,
     * NEVER CALL THIS METHOD DIRECTLY, instead use IBuildingStyleRegistry.getSchematicLocation(building, style).
     *
     * the format of the resource location will be according to this example:
     * new ResourceLocation("settlercraft", "buildings/house/house1") will be converted to "assets/settlercraft/buildings/house/house1.json"
     *
     * This method should never return null, if you do not recognize the building style, do not return null, instead return the default style.
     * You should not call style.getSchematicLocation() from within this method, if this method is called, it means the style does not recognize your building.
     *
     * Schematics can be created by building the structure in a world and then using the schematic creator item to export the building as a json to
     * the file specified in the config.
     *
     * @param style style to be applied
     * @return a ResourceLocation containing the path to the json file defining this building
     */
    ResourceLocation schematicLocation(IBuildingStyle style);

    /**
     * @return the maximum number of settlers living in this building
     */
    int maxInhabitants();

    /**
     * Checks if a settler can live here, this is called when the mayor assigns a house to a settler
     * @param building the ISettlementBuilding object where this is built
     * @param settler the settler wanting to live here
     * @return true if this building can be set as the settler's house
     */
    boolean canSettlerLiveHere(ISettlementBuilding building, ISettler settler);

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
     * Note that the building has already been scanned for beds and inventories
     * @param building the ISettlementBuilding object which has been built
     */
    void onBuildingCompleted(ISettlementBuilding building);

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
