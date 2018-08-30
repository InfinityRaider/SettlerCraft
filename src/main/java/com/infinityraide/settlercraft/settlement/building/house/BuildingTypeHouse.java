package com.infinityraide.settlercraft.settlement.building.house;

import com.infinityraide.settlercraft.api.v1.ISettlement;
import com.infinityraide.settlercraft.settlement.building.BuildingTypeBase;

public class BuildingTypeHouse extends BuildingTypeBase {
    public BuildingTypeHouse() {
        super("house");
    }

    @Override
    public int maximumBuildingCountPerSettlement(ISettlement settlement) {
        return 1 + settlement.population()/2;
    }
}
