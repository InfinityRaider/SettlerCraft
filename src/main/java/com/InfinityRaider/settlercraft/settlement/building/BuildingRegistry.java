package com.InfinityRaider.settlercraft.settlement.building;

import com.InfinityRaider.settlercraft.api.v1.*;
import com.InfinityRaider.settlercraft.settlement.building.barracks.*;
import com.InfinityRaider.settlercraft.settlement.building.decorative.*;
import com.InfinityRaider.settlercraft.settlement.building.farm.*;
import com.InfinityRaider.settlercraft.settlement.building.house.*;
import com.InfinityRaider.settlercraft.settlement.building.lumbermill.*;
import com.InfinityRaider.settlercraft.settlement.building.quarry.*;
import com.InfinityRaider.settlercraft.settlement.building.townhall.*;
import com.InfinityRaider.settlercraft.settlement.building.utility.BuildingRailStation;
import com.InfinityRaider.settlercraft.settlement.building.wall.*;
import com.InfinityRaider.settlercraft.settlement.building.warehouse.*;
import com.InfinityRaider.settlercraft.settlement.building.workshop.*;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildingRegistry implements IBuildingRegistry {
    private static final BuildingRegistry INSTANCE = new BuildingRegistry();

    public static BuildingRegistry getInstance() {
        return INSTANCE;
    }

    /** Buildings */
    public final List<IBuilding> BUILDINGS_LIST;
    public final Map<String, IBuilding> BUILDINGS_BY_NAME;
    public final Map<IBuildingType, List<IBuilding>> BUILDINGS_MAP;

    /** Town halls */
    public final IBuilding TOWN_HALL_1;
    public final IBuilding TOWN_HALL_2;

    /** Houses */
    public final IBuilding HOUSE_SMALL;
    public final IBuilding HOUSE_MEDIUM;
    public final IBuilding HOUSE_LARGE;

    /** Warehouses */
    public final IBuilding WAREHOUSE;

    /** Workshops */
    public final IBuilding WORKSHOP_CRAFTER;
    public final IBuilding WORKSHOP_BLACKSMITH;

    /** Academies */

    /** Quarries */
    public final IBuilding QUARRY;

    /** Lumber mills */
    public final IBuilding LUMBER_MILL;

    /** Farms */
    public final IBuilding CROP_FARM_HOUSE;
    public final IBuilding CROP_FARM_RIVERSIDE;
    public final IBuilding CROP_FARM_IRRIGATED;
    public final IBuilding CATTLE_FARM_STABLE;

    /** Barracks */
    public final IBuilding BARRACKS;
    public final IBuilding ARCHERY_RANGE;
    public final IBuilding CAVALRY_STABLES;

    /** Decorative */
    public final IBuilding WATER_WELL;
    public final IBuilding SITTING_CORNER;

    /** Walls */
    public final IBuilding WALL_SEGMENT;
    public final IBuilding WALL_CORNER_CONVEX;
    public final IBuilding WALL_CORNER_CONCAVE;
    public final IBuilding WALL_GATE;
    public final IBuilding WALL_BASTION;

    /** Utilities */
    public final IBuilding RAILWAY_STATION;

    private BuildingRegistry() {
        //Buildings
        BUILDINGS_LIST = new ArrayList<>();
        BUILDINGS_BY_NAME = new HashMap<>();
        BUILDINGS_MAP = new HashMap<>();

        //Town Halls
        TOWN_HALL_1 = registerBuilding(new BuildingTownHallTier1());
        TOWN_HALL_2 = registerBuilding(new BuildingTownHallTier2());

        //Houses
        HOUSE_SMALL = registerBuilding(new BuildingHouseSmall());
        HOUSE_MEDIUM = registerBuilding(new BuildingHouseMedium());
        HOUSE_LARGE = registerBuilding(new BuildingHouseLarge());

        //Warehouses
        WAREHOUSE = registerBuilding(new BuildingWareHouse());

        //workshops
        WORKSHOP_CRAFTER = registerBuilding(new BuildingWorkshopCrafter());
        WORKSHOP_BLACKSMITH = registerBuilding(new BuildingWorkShopBlackSmith());

        //academies

        //quarry
        QUARRY = registerBuilding(new BuildingQuarry());

        //lumber mill
        LUMBER_MILL = registerBuilding(new BuildingLumberMill());

        //farms
        CROP_FARM_HOUSE = registerBuilding(new BuildingCropFarmHouse());
        CROP_FARM_RIVERSIDE = registerBuilding(new BuildingCropFarmRiverside());
        CROP_FARM_IRRIGATED = registerBuilding(new BuildingCropFarmIrrigated());
        CATTLE_FARM_STABLE = registerBuilding(new BuildingAnimalFarmStable());

        //barracks
        BARRACKS = registerBuilding(new BuildingBarracks());
        ARCHERY_RANGE = registerBuilding(new BuildingArcheryRange());
        CAVALRY_STABLES = registerBuilding(new BuildingCavalryStables());

        //decorative
        WATER_WELL = registerBuilding(new BuildingWaterWell());
        SITTING_CORNER = registerBuilding(new BuildingSittingCorner());

        //walls
        WALL_SEGMENT = new BuildingWallSegment();
        WALL_CORNER_CONVEX = new BuildingWallCornerConvex();
        WALL_CORNER_CONCAVE = new BuildingWallCornerConcave();
        WALL_GATE = new BuildingWallGate();
        WALL_BASTION = new BuildingWallBastion();

        //utilities
        RAILWAY_STATION = new BuildingRailStation();
    }

    public IBuilding registerBuilding(IBuilding building) {
        BUILDINGS_LIST.add(building);
        BUILDINGS_BY_NAME.put(building.name(), building);
        if(!BUILDINGS_MAP.containsKey(building.buildingType())) {
            BUILDINGS_MAP.put(building.buildingType(), new ArrayList<>());
        }
        BUILDINGS_MAP.get(building.buildingType()).add(building);
        return building;
    }

    public IBuilding getBuildingFromName(String name) {
        return BUILDINGS_BY_NAME.get(name);
    }

    @Override
    public List<IBuilding> apply(IBuildingType buildingType) {
        return BUILDINGS_MAP.containsKey(buildingType) ? BUILDINGS_MAP.get(buildingType) : ImmutableList.of();
    }
}
