package com.InfinityRaider.settlercraft.api.v1;

import java.util.List;
import java.util.function.Function;

/**
 * Interface used to interact with the building registry,
 * the instance of this interface can be retrieved via api.getBuildingRegistry()
 */
@SuppressWarnings("unused")
public interface IBuildingRegistry extends Function<IBuildingType, List<IBuilding>> {
    /**
     * @return a list of all registered buildings
     */
    List<IBuilding> getBuildings();

    /**
     * Gets a building by its name
     * @param name the name of the building
     * @return the building registered with that name, or null if no such building is registered
     */
    IBuilding getBuildingFromName(String name);

    /**
     * @return Settlercraft tier 1 town hall building
     */
    IBuilding buildingTownHallTier1();

    /**
     * @return Settlercraft tier 2 town hall building
     */
    IBuilding buildingTownHallTier2();

    /**
     * @return Settlercraft tier 3 town hall building
     */
    IBuilding buildingTownHallTier3();

    /**
     * @return Settlercraft small house building
     */
    IBuilding buildingHouseSmall();

    /**
     * @return Settlercraft medium house building
     */
    IBuilding buildingHouseMedium();

    /**
     * @return Settlercraft large house building
     */
    IBuilding buildingHouseLarge() ;

    /**
     * @return Settlercraft warehouse building
     */
    IBuilding buildingWarehouse();

    /**
     * @return Settlercraft crafter workshop building
     */
    IBuilding buildingWorkShopCrafter();

    /**
     * @return Settlercraft blacksmith workshop building
     */
    IBuilding buildingWorkShopBlackSmith();

    /**
     * @return Settlercraft quarry building
     */
    IBuilding buildingQuarry();

    /**
     * @return Settlercraft lumber mill building
     */
    IBuilding buildingLumberMill();

    /**
     * @return Settlercraft crop farm house building
     */
    IBuilding buildingCropFarmHouse();

    /**
     * @return Settlercraft crop farm riverside building
     */
    IBuilding buildingCropFarmRiverside();

    /**
     * @return Settlercraft crop farm irrigated building
     */
    IBuilding buildingCropFarmIrrigated();

    /**
     * @return Settlercraft cattle farm stables building
     */
    IBuilding buildingCattleFarmStable();

    /**
     * @return Settlercraft barracks building
     */
    IBuilding buildingBarracks();

    /**
     * @return Settlercraft archery range building
     */
    IBuilding buildingArcheryRange();

    /**
     * @return Settlercraft cavalry stables building
     */
    IBuilding buildingCavalryStables();

    /**
     * @return Settlercraft water well building
     */
    IBuilding buildingWaterWell();

    /**
     * @return Settlercraft sitting corner building
     */
    IBuilding buildingSittingCorner();

    /**
     * @return Settlercraft wall segment building
     */
    IBuilding buildingWallSegment();

    /**
     * @return Settlercraft wall corner convex building
     */
    IBuilding buildingWallCornerConvex();

    /**
     * @return Settlercraft wall corner concave building
     */
    IBuilding buildingWallCornerConcave();

    /**
     * @return Settlercraft wall gate building
     */
    IBuilding buildingWallGate();

    /**
     * @return Settlercraft wall bastion building
     */
    IBuilding buildingWallBastion();

    /**
     * @return Settlercraft railstation building
     */
    IBuilding buildingRailStation();
}
