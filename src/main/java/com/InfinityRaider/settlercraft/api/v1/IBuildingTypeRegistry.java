package com.InfinityRaider.settlercraft.api.v1;

import java.util.List;

/**
 * This interface is implemented in the class where I keep track of all registered IBuildingTypes.
 * It can be used to interact with the different building types.
 *
 * To get the instance of this class use APIv1.getBuildingTypeRegistry()
 */
public interface IBuildingTypeRegistry {
    /**
     * @return The building type for the town halls
     */
    IBuildingType buildingTypeTownHall();

    /**
     * @return The building type for the houses
     */
    IBuildingType buildingTypeHouse();

    /**
     * @return a list containing all building types
     */
    List<IBuildingType> allBuildingTypes();

    /**
     * @return a list containing all custom building types (types not native to SettlerCraft)
     */
    List<IBuildingType> customBuildingTypes();

    /**
     * Registers a new building type, use this if you want to create a whole new building type, the buildings for this building type will be automatically registered.
     * If you want to register a new building to an existing building type, call IBuildingType.addBuilding(IBuilding building)
     * Note that any attempts to register a new IBuildingType after FML's init phase will be denied.
     *
     * @param type the new type to register
     * @return if the registering was successful
     */
    boolean registerBuildingType(IBuildingType type);
}
