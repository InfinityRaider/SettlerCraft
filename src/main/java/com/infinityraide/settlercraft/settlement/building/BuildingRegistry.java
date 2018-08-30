package com.infinityraide.settlercraft.settlement.building;

import com.infinityraide.settlercraft.api.v1.IBuilding;
import com.infinityraide.settlercraft.api.v1.IBuildingRegistry;
import com.infinityraide.settlercraft.api.v1.IBuildingType;
import com.infinityraide.settlercraft.settlement.building.academy.BuildingAcademy;
import com.infinityraide.settlercraft.settlement.building.barracks.BuildingCavalryStables;
import com.infinityraide.settlercraft.settlement.building.farm.BuildingAnimalFarmStable;
import com.infinityraide.settlercraft.settlement.building.farm.BuildingCropFarmHouse;
import com.infinityraide.settlercraft.settlement.building.farm.BuildingCropFarmRiverside;
import com.infinityraide.settlercraft.settlement.building.house.BuildingHouseLarge;
import com.infinityraide.settlercraft.settlement.building.townhall.BuildingTownHallTier2;
import com.infinityraide.settlercraft.settlement.building.townhall.BuildingTownHallTier3;
import com.infinityraide.settlercraft.settlement.building.utility.BuildingCouncilHall;
import com.infinityraide.settlercraft.settlement.building.utility.BuildingPrisonBlock;
import com.infinityraide.settlercraft.settlement.building.utility.BuildingRailStation;
import com.infinityraide.settlercraft.settlement.building.utility.BuildingStables;
import com.google.common.collect.ImmutableList;
import com.infinityraide.settlercraft.settlement.building.barracks.BuildingArcheryRange;
import com.infinityraide.settlercraft.settlement.building.barracks.BuildingBarracks;
import com.infinityraide.settlercraft.settlement.building.decorative.BuildingSittingCorner;
import com.infinityraide.settlercraft.settlement.building.decorative.BuildingWaterWell;
import com.infinityraide.settlercraft.settlement.building.farm.BuildingCropFarmIrrigated;
import com.infinityraide.settlercraft.settlement.building.house.BuildingHouseMedium;
import com.infinityraide.settlercraft.settlement.building.house.BuildingHouseSmall;
import com.infinityraide.settlercraft.settlement.building.lumbermill.BuildingLumberMill;
import com.infinityraide.settlercraft.settlement.building.quarry.BuildingQuarry;
import com.infinityraide.settlercraft.settlement.building.townhall.BuildingTownHallTier1;
import com.infinityraide.settlercraft.settlement.building.wall.*;
import com.infinityraide.settlercraft.settlement.building.warehouse.BuildingWareHouse;
import com.infinityraide.settlercraft.settlement.building.workshop.BuildingWorkShopBlackSmith;
import com.infinityraide.settlercraft.settlement.building.workshop.BuildingWorkshopCrafter;

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
    private final List<IBuilding> BUILDINGS_LIST;
    private final Map<String, IBuilding> BUILDINGS_BY_NAME;
    private final Map<IBuildingType, List<IBuilding>> BUILDINGS_MAP;

    /** Town halls */
    private final IBuilding TOWN_HALL_1;
    private final IBuilding TOWN_HALL_2;
    private final IBuilding TOWN_HALL_3;

    /** Houses */
    private final IBuilding HOUSE_SMALL;
    private final IBuilding HOUSE_MEDIUM;
    private final IBuilding HOUSE_LARGE;

    /** Warehouses */
    private final IBuilding WAREHOUSE;

    /** Workshops */
    private final IBuilding WORKSHOP_CRAFTER;
    private final IBuilding WORKSHOP_BLACKSMITH;

    /** Academies */
    private final IBuilding ACADEMY;

    /** Quarries */
    private final IBuilding QUARRY;

    /** Lumber mills */
    private final IBuilding LUMBER_MILL;

    /** Farms */
    private final IBuilding CROP_FARM_HOUSE;
    private final IBuilding CROP_FARM_RIVERSIDE;
    private final IBuilding CROP_FARM_IRRIGATED;
    private final IBuilding CATTLE_FARM_STABLE;

    /** Barracks */
    private final IBuilding BARRACKS;
    private final IBuilding ARCHERY_RANGE;
    private final IBuilding CAVALRY_STABLES;

    /** Decorative */
    private final IBuilding WATER_WELL;
    private final IBuilding SITTING_CORNER;

    /** Walls */
    private final IBuilding WALL_SEGMENT;
    private final IBuilding WALL_CORNER_CONVEX;
    private final IBuilding WALL_CORNER_CONCAVE;
    private final IBuilding WALL_GATE;
    private final IBuilding WALL_BASTION;

    /** Utilities */
    private final IBuilding RAILWAY_STATION;
    private final IBuilding PRISON_BLOCK;
    private final IBuilding COUNCIL_HALL;
    private final IBuilding STABLES;

    private BuildingRegistry() {
        //Buildings
        BUILDINGS_LIST = new ArrayList<>();
        BUILDINGS_BY_NAME = new HashMap<>();
        BUILDINGS_MAP = new HashMap<>();

        //Town Halls
        TOWN_HALL_1 = registerBuilding(new BuildingTownHallTier1());
        TOWN_HALL_2 = registerBuilding(new BuildingTownHallTier2());
        TOWN_HALL_3 = registerBuilding(new BuildingTownHallTier3());

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
        ACADEMY = registerBuilding(new BuildingAcademy());

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
        WALL_SEGMENT = registerBuilding(new BuildingWallSegment());
        WALL_CORNER_CONVEX = registerBuilding(new BuildingWallCornerConvex());
        WALL_CORNER_CONCAVE = registerBuilding(new BuildingWallCornerConcave());
        WALL_GATE = registerBuilding(new BuildingWallGate());
        WALL_BASTION = registerBuilding(new BuildingWallBastion());

        //utilities
        RAILWAY_STATION = registerBuilding(new BuildingRailStation());
        PRISON_BLOCK = registerBuilding(new BuildingPrisonBlock());
        COUNCIL_HALL = registerBuilding(new BuildingCouncilHall());
        STABLES = registerBuilding(new BuildingStables());
    }

    @Override
    public IBuilding registerBuilding(IBuilding building) {
        BUILDINGS_LIST.add(building);
        BUILDINGS_BY_NAME.put(building.name(), building);
        if(!BUILDINGS_MAP.containsKey(building.buildingType())) {
            BUILDINGS_MAP.put(building.buildingType(), new ArrayList<>());
        }
        BUILDINGS_MAP.get(building.buildingType()).add(building);
        return building;
    }

    @Override
    public List<IBuilding> getBuildings() {
        return ImmutableList.copyOf(this.BUILDINGS_LIST);
    }

    @Override
    public IBuilding getBuildingFromName(String name) {
        return BUILDINGS_BY_NAME.get(name);
    }

    @Override
    public IBuilding buildingTownHallTier1() {
        return TOWN_HALL_1;
    }

    @Override
    public IBuilding buildingTownHallTier2() {
        return TOWN_HALL_2;
    }

    @Override
    public IBuilding buildingTownHallTier3() {
        return TOWN_HALL_3;
    }

    @Override
    public IBuilding buildingHouseSmall() {
        return HOUSE_SMALL;
    }

    @Override
    public IBuilding buildingHouseMedium() {
        return HOUSE_MEDIUM;
    }

    @Override
    public IBuilding buildingHouseLarge() {
        return HOUSE_LARGE;
    }

    @Override
    public IBuilding buildingWarehouse() {
        return WAREHOUSE;
    }

    @Override
    public IBuilding buildingWorkShopCrafter() {
        return WORKSHOP_CRAFTER;
    }

    @Override
    public IBuilding buildingWorkShopBlackSmith() {
        return WORKSHOP_BLACKSMITH;
    }

    @Override
    public IBuilding buildingAcademy() {
        return ACADEMY;
    }

    @Override
    public IBuilding buildingQuarry() {
        return QUARRY;
    }

    @Override
    public IBuilding buildingLumberMill() {
        return LUMBER_MILL;
    }

    @Override
    public IBuilding buildingCropFarmHouse() {
        return CROP_FARM_HOUSE;
    }

    @Override
    public IBuilding buildingCropFarmRiverside() {
        return CROP_FARM_RIVERSIDE;
    }

    @Override
    public IBuilding buildingCropFarmIrrigated() {
        return CROP_FARM_IRRIGATED;
    }

    @Override
    public IBuilding buildingCattleFarmStable() {
        return CATTLE_FARM_STABLE;
    }

    @Override
    public IBuilding buildingBarracks() {
        return BARRACKS;
    }

    @Override
    public IBuilding buildingArcheryRange() {
        return ARCHERY_RANGE;
    }

    @Override
    public IBuilding buildingCavalryStables() {
        return CAVALRY_STABLES;
    }

    @Override
    public IBuilding buildingWaterWell() {
        return WATER_WELL;
    }

    @Override
    public IBuilding buildingSittingCorner() {
        return SITTING_CORNER;
    }

    @Override
    public IBuilding buildingWallSegment() {
        return WALL_SEGMENT;
    }

    @Override
    public IBuilding buildingWallCornerConvex() {
        return WALL_CORNER_CONVEX;
    }

    @Override
    public IBuilding buildingWallCornerConcave() {
        return WALL_CORNER_CONCAVE;
    }

    @Override
    public IBuilding buildingWallGate() {
        return WALL_GATE;
    }

    @Override
    public IBuilding buildingWallBastion() {
        return WALL_BASTION;
    }

    @Override
    public IBuilding buildingRailStation() {
        return RAILWAY_STATION;
    }

    @Override
    public IBuilding buildingPrisonBlock() {
        return PRISON_BLOCK;
    }

    @Override
    public IBuilding buildingCouncilHall() {
        return COUNCIL_HALL;
    }

    @Override
    public IBuilding buildingStables() {
        return STABLES;
    }

    @Override
    public List<IBuilding> apply(IBuildingType buildingType) {
        return BUILDINGS_MAP.containsKey(buildingType) ? BUILDINGS_MAP.get(buildingType) : ImmutableList.of();
    }
}
