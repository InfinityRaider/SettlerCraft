package com.InfinityRaider.settlercraft.settlement.building;

import com.InfinityRaider.settlercraft.api.v1.IBuilding;
import com.InfinityRaider.settlercraft.api.v1.IBuildingType;

import java.util.List;

public abstract class BuildingTypeBase implements IBuildingType {
    private final String name;

    public BuildingTypeBase(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public List<IBuilding> getAllBuildings() {
        return BuildingRegistry.getInstance().BUILDINGS_MAP.get(this);
    }

    @Override
    public boolean addNewBuilding(IBuilding building) {
        BuildingRegistry.getInstance().registerBuilding(building);
        return true;
    }
}
