package com.infinityraide.settlercraft.settlement.building.utility;

import com.infinityraide.settlercraft.api.v1.ISettlement;
import com.infinityraide.settlercraft.settlement.building.BuildingTypeBase;

public class BuildingTypeUtility extends BuildingTypeBase {
    public BuildingTypeUtility() {
        super("utilities");
    }

    @Override
    public int maximumBuildingCountPerSettlement(ISettlement settlement) {
        return settlement.getCompletedBuildings(this).size() + 1;
    }
}
