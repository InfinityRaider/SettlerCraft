package com.InfinityRaider.settlercraft.settlement.building;

import com.InfinityRaider.settlercraft.api.v1.IBuilding;
import com.InfinityRaider.settlercraft.api.v1.IBuildingRegistry;
import com.InfinityRaider.settlercraft.api.v1.IBuildingType;
import com.InfinityRaider.settlercraft.settlement.building.decorative.BuildingSittingCorner;
import com.InfinityRaider.settlercraft.settlement.building.decorative.BuildingWaterWell;
import com.InfinityRaider.settlercraft.settlement.building.farm.BuildingAnimalFarmStable;
import com.InfinityRaider.settlercraft.settlement.building.farm.BuildingCropFarmHouse;
import com.InfinityRaider.settlercraft.settlement.building.farm.BuildingCropFarmIrrigated;
import com.InfinityRaider.settlercraft.settlement.building.farm.BuildingCropFarmRiverside;
import com.InfinityRaider.settlercraft.settlement.building.house.BuildingHouseLarge;
import com.InfinityRaider.settlercraft.settlement.building.house.BuildingHouseMedium;
import com.InfinityRaider.settlercraft.settlement.building.house.BuildingHouseSmall;
import com.InfinityRaider.settlercraft.settlement.building.quarry.BuildingQuarry;
import com.InfinityRaider.settlercraft.settlement.building.townhall.BuildingTownHallTier1;
import com.InfinityRaider.settlercraft.settlement.building.warehouse.BuildingWareHouse;
import com.InfinityRaider.settlercraft.settlement.building.workshop.BuildingWorkShopBlackSmith;
import com.InfinityRaider.settlercraft.settlement.building.workshop.BuildingWorkshopCrafter;
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

    /** Farms */
    public final IBuilding CROP_FARM_HOUSE;
    public final IBuilding CROP_FARM_RIVERSIDE;
    public final IBuilding CROP_FARM_IRRIGATED;
    public final IBuilding CATTLE_FARM_STABLE;

    /** Barracks */

    /** Decorative */
    public final IBuilding WATER_WELL;
    public final IBuilding SITTING_CORNER;

    private BuildingRegistry() {
        //Buildings
        BUILDINGS_LIST = new ArrayList<>();
        BUILDINGS_BY_NAME = new HashMap<>();
        BUILDINGS_MAP = new HashMap<>();

        //Town Halls
        TOWN_HALL_1 = registerBuilding(new BuildingTownHallTier1());

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

        //farms
        CROP_FARM_HOUSE = registerBuilding(new BuildingCropFarmHouse());
        CROP_FARM_RIVERSIDE = registerBuilding(new BuildingCropFarmRiverside());
        CROP_FARM_IRRIGATED = registerBuilding(new BuildingCropFarmIrrigated());
        CATTLE_FARM_STABLE = registerBuilding(new BuildingAnimalFarmStable());

        //barracks

        //decorative
        WATER_WELL = registerBuilding(new BuildingWaterWell());
        SITTING_CORNER = registerBuilding(new BuildingSittingCorner());
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
