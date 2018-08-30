package com.infinityraider.settlercraft.settlement.building.house;

import com.infinityraider.settlercraft.api.v1.ISettlement;
import com.infinityraider.settlercraft.settlement.building.BuildingTypeBase;

public class BuildingTypeHouse extends BuildingTypeBase {
    public BuildingTypeHouse() {
        super("house");
    }

    @Override
    public int maximumBuildingCountPerSettlement(ISettlement settlement) {
        return 1 + settlement.population()/2;
    }
}
