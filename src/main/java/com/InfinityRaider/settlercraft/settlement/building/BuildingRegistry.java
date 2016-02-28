package com.InfinityRaider.settlercraft.settlement.building;

import com.InfinityRaider.settlercraft.api.v1.IBuilding;
import com.InfinityRaider.settlercraft.api.v1.IBuildingRegistry;
import com.InfinityRaider.settlercraft.api.v1.IBuildingType;
import com.InfinityRaider.settlercraft.settlement.building.house.BuildingHouse;
import com.InfinityRaider.settlercraft.settlement.building.townhall.BuildingTownHall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildingRegistry implements IBuildingRegistry {
    private static final BuildingRegistry INSTANCE = new BuildingRegistry();

    public static BuildingRegistry getInstance() {
        return INSTANCE;
    }

    /**
     * ---------
     * Buildings
     * ---------
     */
    public final List<IBuilding> BUILDINGS_LIST;
    public final Map<String, IBuilding> BUILDINGS_BY_NAME;
    public final Map<IBuildingType, List<IBuilding>> BUILDINGS_MAP;

    /**
     * Town halls
     */
    public final IBuilding TOWN_HALL_1;

    /**
     * Houses
     */
    public final IBuilding HOUSE_1;


    private BuildingRegistry() {
        //Buildings
        BUILDINGS_LIST = new ArrayList<>();
        BUILDINGS_BY_NAME = new HashMap<>();
       BUILDINGS_MAP = new HashMap<>();


        //Town Halls
        TOWN_HALL_1 = registerBuilding(new BuildingTownHall());

        //Houses
        HOUSE_1 = registerBuilding(new BuildingHouse());
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
}
