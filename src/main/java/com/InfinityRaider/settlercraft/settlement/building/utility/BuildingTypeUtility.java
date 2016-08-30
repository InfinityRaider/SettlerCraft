package com.InfinityRaider.settlercraft.settlement.building.utility;

import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.settlement.building.BuildingTypeBase;

public class BuildingTypeUtility extends BuildingTypeBase {
    public BuildingTypeUtility() {
        super("utilities");
    }

    @Override
    public int maximumBuildingCountPerSettlement(ISettlement settlement) {
        return settlement.getCompletedBuildings(this).size() + 1;
    }
}
