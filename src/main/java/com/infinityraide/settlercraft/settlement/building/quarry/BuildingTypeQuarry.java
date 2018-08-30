package com.infinityraide.settlercraft.settlement.building.quarry;

import com.infinityraide.settlercraft.api.v1.ISettlement;
import com.infinityraide.settlercraft.settlement.building.BuildingTypeBase;

public class BuildingTypeQuarry extends BuildingTypeBase {
    public BuildingTypeQuarry() {
        super("quarry");
    }

    @Override
    public int maximumBuildingCountPerSettlement(ISettlement settlement) {
        return 1;
    }
}
