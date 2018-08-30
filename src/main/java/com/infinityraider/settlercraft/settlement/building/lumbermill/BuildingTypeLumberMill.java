package com.infinityraider.settlercraft.settlement.building.lumbermill;

import com.infinityraider.settlercraft.api.v1.ISettlement;
import com.infinityraider.settlercraft.settlement.building.BuildingTypeBase;

public class BuildingTypeLumberMill extends BuildingTypeBase {
    public BuildingTypeLumberMill() {
        super("lumber_mill");
    }

    @Override
    public int maximumBuildingCountPerSettlement(ISettlement settlement) {
        return 2;
    }
}
