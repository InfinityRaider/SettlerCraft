package com.infinityraider.settlercraft.settlement.building.utility;

import com.infinityraider.settlercraft.api.v1.ISettlement;
import com.infinityraider.settlercraft.settlement.building.BuildingTypeBase;

public class BuildingTypeUtility extends BuildingTypeBase {
    public BuildingTypeUtility() {
        super("utilities");
    }

    @Override
    public int maximumBuildingCountPerSettlement(ISettlement settlement) {
        return settlement.getCompletedBuildings(this).size() + 1;
    }
}
