package com.InfinityRaider.settlercraft.api.v1;

import java.util.List;

/**
 * Buildings are grouped per types, this interface represents such a type.
 *
 * This interface can be used to interact with existing building types, or to create brand new ones.
 * If you want to create a new IBuildingType, implement this interface and register the instance with
 * IBuildingTypeRegistry.registerBuildingType(IBuildingType type)
 */
public interface IBuildingType {
    /**
     * @return the name of this building type
     */
    String name();

    /**
     * This method is used by the settlement to see if a building of this type is allowed to be built there
     * @param settlement the settlement to build a building of this type
     * @return the maximum number of buildings for this type allowed in the settlement
     */
    int maximumBuildingCountPerSettlement(ISettlement settlement);

    /**
     * @return a List containing the instance of every building for this building type
     */
    List<IBuilding> getAllBuildings();

    /**
     * This method is used to register a new building to this type, if no new buildings are allowed, simply return false.
     * Make sure that if you allow new buildings to be registered, that they will be added to the list returned by getAllBuildings()
     *
     * @param building a new building to be registered to this type
     * @return if the registering was successful
     */
    boolean addNewBuilding(IBuilding building);

    /**
     * This method gets the unlocalized name of this building type, it is used in dialogues.
     * Add a translation for this language key to your lang files.
     * @return the unlocalized name of this building type
     */
    String unlocalizedName();
}
