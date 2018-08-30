package com.infinityraider.settlercraft.settlement.building.quarry;

import com.infinityraider.settlercraft.api.v1.ISettlement;
import com.infinityraider.settlercraft.settlement.building.BuildingTypeBase;

public class BuildingTypeQuarry extends BuildingTypeBase {
    public BuildingTypeQuarry() {
        super("quarry");
    }

    @Override
    public int maximumBuildingCountPerSettlement(ISettlement settlement) {
        return 1;
    }
}
