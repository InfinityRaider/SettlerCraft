package com.InfinityRaider.settlercraft.settlement.building;

import com.InfinityRaider.settlercraft.api.v1.IBuilding;
import com.InfinityRaider.settlercraft.api.v1.IBuildingType;
import com.InfinityRaider.settlercraft.api.v1.IBuildingTypeRegistry;
import com.InfinityRaider.settlercraft.settlement.building.house.BuildingTypeHouse;
import com.InfinityRaider.settlercraft.settlement.building.townhall.BuildingTypeTownHall;
import com.InfinityRaider.settlercraft.utility.LogHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class BuildingTypeRegistry implements IBuildingTypeRegistry {
    private static final BuildingTypeRegistry INSTANCE = new BuildingTypeRegistry();

    public static BuildingTypeRegistry getInstance() {
        return INSTANCE;
    }

    private boolean postInit = false;

    private final IBuildingType TOWN_HALL;
    private final IBuildingType HOUSE;

    private final List<IBuildingType> TYPES;
    private final List<IBuildingType> CUSTOM_TYPES;

    private BuildingTypeRegistry() {
        TOWN_HALL = new BuildingTypeTownHall();
        HOUSE = new BuildingTypeHouse();

        TYPES = new ArrayList<>();
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
    public List<IBuildingType> allBuildingTypes() {
        return TYPES;
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
        TYPES.add(type);
        CUSTOM_TYPES.add(type);
        for(IBuilding building : type.getAllBuildings()) {
            BuildingRegistry.getInstance().registerBuilding(building);
        }
        return true;
    }

    public void postInit() {
        postInit = true;
    }

    private void initTypes() {
        for(Field field:this.getClass().getDeclaredFields()) {
            if(field.getType() == IBuildingType.class) {
                try {
                    TYPES.add((IBuildingType) field.get(this));
                } catch (IllegalAccessException e) {
                    LogHelper.printStackTrace(e);
                }
            }
        }
    }
}
