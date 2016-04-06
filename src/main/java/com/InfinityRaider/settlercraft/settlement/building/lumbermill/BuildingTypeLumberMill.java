package com.InfinityRaider.settlercraft.settlement.building.lumbermill;

import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.settlement.building.BuildingTypeBase;

public class BuildingTypeLumberMill extends BuildingTypeBase {
    public BuildingTypeLumberMill() {
        super("lumber_mill");
    }

    @Override
    public int maximumBuildingCountPerSettlement(ISettlement settlement) {
        return 2;
    }
}
