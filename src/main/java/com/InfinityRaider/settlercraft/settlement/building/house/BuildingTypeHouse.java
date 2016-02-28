package com.InfinityRaider.settlercraft.settlement.building.house;

import com.InfinityRaider.settlercraft.api.v1.IBuilding;
import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.settlement.building.BuildingRegistry;
import com.InfinityRaider.settlercraft.settlement.building.BuildingTypeBase;

public class BuildingTypeHouse extends BuildingTypeBase {
    public BuildingTypeHouse() {
        super("house");
    }

    @Override
    public int maximumBuildingCountPerSettlement(ISettlement settlement) {
        return 1 + settlement.population()/2;
    }

    @Override
    public IBuilding startingBuilding() {
        return BuildingRegistry.getInstance().HOUSE_1;
    }
}
