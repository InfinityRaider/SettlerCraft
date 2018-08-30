package com.infinityraide.settlercraft.settlement.building.warehouse;

import com.infinityraide.settlercraft.api.v1.ISettlement;
import com.infinityraide.settlercraft.settlement.building.BuildingTypeBase;

public class BuildingTypeWareHouse extends BuildingTypeBase {
    public BuildingTypeWareHouse() {
        super("warehouse");
    }

    @Override
    public int maximumBuildingCountPerSettlement(ISettlement settlement) {
        return settlement.getBuildings(this).size() + 1;
    }
}
