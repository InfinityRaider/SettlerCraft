package com.infinityraide.settlercraft.api.v1;

import java.util.List;

/**
 * This interface is implemented in the class where I keep track of all registered IBuildingTypes.
 * It can be used to interact with the different building types.
 *
 * To get the instance of this class use APIv1.getBuildingTypeRegistry()
 */
@SuppressWarnings("unused")
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
     * @return The building type for warehouses
     */
    IBuildingType buildingTypeWareHouse();

    /**
     * @return The building type for workshops
     */
    IBuildingType buildingTypeWorkshop();

    /**
     * @return The building type for academies
     */
    IBuildingType buildingTypeAcademy();

    /**
     * @return The building type for quarries
     */
    IBuildingType buildingTypeQuarry();

    /**
     * @return The building type for lumber mills
     */
    IBuildingType buildingTypeLumberMill();

    /**
     * @return The building type for farms
     */
    IBuildingType buildingTypeFarm();

    /**
     * @return The building type for barracks
     */
    IBuildingType buildingTypeBarracks();

    /**
     * @return The building type for decorative buildings
     */
    IBuildingType buildingTypeDecorative();

    /**
     * @return The building type for walls
     */
    IBuildingType buildingTypeWall();

    /**
     * @return The building type for utility buildings
     */
    IBuildingType buildingTypeUtilities();

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

    /**
     * Gets a previously registered building type back from the registry by using its name
     * @param name The name of the building type
     * @return the building type, or null if no building type with this name is registered
     */
    IBuildingType getBuildingTypeFromName(String name);
}
