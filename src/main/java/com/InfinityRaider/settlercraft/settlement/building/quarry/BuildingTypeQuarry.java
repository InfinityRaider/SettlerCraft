package com.InfinityRaider.settlercraft.settlement.building.quarry;

import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.settlement.building.BuildingTypeBase;

public class BuildingTypeQuarry extends BuildingTypeBase {
    public BuildingTypeQuarry() {
        super("quarry");
    }

    @Override
    public int maximumBuildingCountPerSettlement(ISettlement settlement) {
        return 1;
    }
}
