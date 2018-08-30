package com.infinityraide.settlercraft.settlement.building.decorative;

import com.infinityraide.settlercraft.api.v1.ISettlement;
import com.infinityraide.settlercraft.settlement.building.BuildingTypeBase;

public class BuildingTypeDecorative extends BuildingTypeBase {
    public BuildingTypeDecorative() {
        super("decorative");
    }

    @Override
    public int maximumBuildingCountPerSettlement(ISettlement settlement) {
        return settlement.getBuildings(this).size() + 1;
    }
}
