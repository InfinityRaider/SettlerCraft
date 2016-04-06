package com.InfinityRaider.settlercraft.settlement.building.warehouse;

import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.settlement.building.BuildingTypeBase;

public class BuildingTypeWareHouse extends BuildingTypeBase {
    public BuildingTypeWareHouse() {
        super("warehouse");
    }

    @Override
    public int maximumBuildingCountPerSettlement(ISettlement settlement) {
        return settlement.getBuildings(this).size() + 1;
    }
}
