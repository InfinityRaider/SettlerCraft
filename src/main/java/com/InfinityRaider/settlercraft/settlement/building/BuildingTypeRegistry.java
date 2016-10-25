package com.InfinityRaider.settlercraft.settlement.building;

import com.InfinityRaider.settlercraft.SettlerCraft;
import com.InfinityRaider.settlercraft.api.v1.IBuilding;
import com.InfinityRaider.settlercraft.api.v1.IBuildingType;
import com.InfinityRaider.settlercraft.api.v1.IBuildingTypeRegistry;
import com.InfinityRaider.settlercraft.settlement.building.academy.BuildingTypeAcademy;
import com.InfinityRaider.settlercraft.settlement.building.barracks.BuildingTypeBarracks;
import com.InfinityRaider.settlercraft.settlement.building.decorative.BuildingTypeDecorative;
import com.InfinityRaider.settlercraft.settlement.building.farm.BuildingTypeFarm;
import com.InfinityRaider.settlercraft.settlement.building.house.BuildingTypeHouse;
import com.InfinityRaider.settlercraft.settlement.building.lumbermill.BuildingTypeLumberMill;
import com.InfinityRaider.settlercraft.settlement.building.quarry.BuildingTypeQuarry;
import com.InfinityRaider.settlercraft.settlement.building.townhall.BuildingTypeTownHall;
import com.InfinityRaider.settlercraft.settlement.building.utility.BuildingTypeUtility;
import com.InfinityRaider.settlercraft.settlement.building.wall.BuildingTypeWall;
import com.InfinityRaider.settlercraft.settlement.building.warehouse.BuildingTypeWareHouse;
import com.InfinityRaider.settlercraft.settlement.building.workshop.BuildingTypeWorkshop;
import com.google.common.collect.ImmutableList;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BuildingTypeRegistry implements IBuildingTypeRegistry {
    private static final BuildingTypeRegistry INSTANCE = new BuildingTypeRegistry();

    public static BuildingTypeRegistry getInstance() {
        return INSTANCE;
    }

    private boolean postInit = false;

    private final IBuildingType TOWN_HALL;
    private final IBuildingType HOUSE;
    private final IBuildingType WAREHOUSE;
    private final IBuildingType WORKSHOP;
    private final IBuildingType ACADEMY;
    private final IBuildingType QUARRY;
    private final IBuildingType LUMBER_MILL;
    private final IBuildingType FARM;
    private final IBuildingType BARRACKS;
    private final IBuildingType DECORATIVE;
    private final IBuildingType WALL;
    private final IBuildingType UTILITY;

    private final Map<String, IBuildingType> TYPES;
    private final List<IBuildingType> CUSTOM_TYPES;

    private BuildingTypeRegistry() {
        TOWN_HALL = new BuildingTypeTownHall();
        HOUSE = new BuildingTypeHouse();
        WAREHOUSE = new BuildingTypeWareHouse();
        WORKSHOP = new BuildingTypeWorkshop();
        ACADEMY = new BuildingTypeAcademy();
        QUARRY = new BuildingTypeQuarry();
        LUMBER_MILL = new BuildingTypeLumberMill();
        FARM = new BuildingTypeFarm();
        BARRACKS = new BuildingTypeBarracks();
        DECORATIVE = new BuildingTypeDecorative();
        WALL = new BuildingTypeWall();
        UTILITY = new BuildingTypeUtility();

        TYPES = new HashMap<>();
        CUSTOM_TYPES = new ArrayList<>();

        initTypes();
    }

    @Override
    public IBuildingType buildingTypeTownHall() {
        return TOWN_HALL;
    }

    @Override
    public IBuildingType buildingTypeHouse() {
        return HOUSE;
    }

    @Override
    public IBuildingType buildingTypeWareHouse() {
        return WAREHOUSE;
    }

    @Override
    public IBuildingType buildingTypeWorkshop() {
        return WORKSHOP;
    }

    @Override
    public IBuildingType buildingTypeAcademy() {
        return ACADEMY;
    }

    @Override
    public IBuildingType buildingTypeQuarry() {
        return QUARRY;
    }

    @Override
    public IBuildingType buildingTypeLumberMill() {
        return LUMBER_MILL;
    }

    @Override
    public IBuildingType buildingTypeFarm() {
        return FARM;
    }

    @Override
    public IBuildingType buildingTypeBarracks() {
        return BARRACKS;
    }

    @Override
    public IBuildingType buildingTypeDecorative() {
        return DECORATIVE;
    }

    @Override
    public IBuildingType buildingTypeWall() {
        return WALL;
    }

    @Override
    public IBuildingType buildingTypeUtilities() {
        return UTILITY;
    }

    @Override
    public List<IBuildingType> allBuildingTypes() {
        return ImmutableList.copyOf(TYPES.values());
    }

    @Override
    public List<IBuildingType> customBuildingTypes() {
        return CUSTOM_TYPES;
    }

    @Override
    public boolean registerBuildingType(IBuildingType type) {
        if(!postInit) {
            return false;
        }
        if(TYPES.containsKey(type.name())) {
            return false;
        }
        TYPES.put(type.name(), type);
        CUSTOM_TYPES.add(type);
        for(IBuilding building : type.getAllBuildings()) {
            BuildingRegistry.getInstance().registerBuilding(building);
        }
        return true;
    }

    @Override
    public IBuildingType getBuildingTypeFromName(String name) {
        return TYPES.get(name);
    }

    public void postInit() {
        postInit = true;
    }

    private void initTypes() {
        for(Field field:this.getClass().getDeclaredFields()) {
            if(field.getType() == IBuildingType.class) {
                try {
                    IBuildingType type = (IBuildingType) field.get(this);
                    TYPES.put(type.name(), type);
                } catch (IllegalAccessException e) {
                    SettlerCraft.instance.getLogger().printStackTrace(e);
                }
            }
        }
    }
}
