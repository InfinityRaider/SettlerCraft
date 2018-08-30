package com.infinityraide.settlercraft.settlement.building.barracks;

import com.infinityraide.settlercraft.api.v1.ISettlement;
import com.infinityraide.settlercraft.settlement.building.BuildingRegistry;
import com.infinityraide.settlercraft.settlement.building.BuildingTypeBase;

public class BuildingTypeBarracks extends BuildingTypeBase {
    public BuildingTypeBarracks() {
        super("barracks");
    }

    @Override
    public int maximumBuildingCountPerSettlement(ISettlement settlement) {
        return BuildingRegistry.getInstance().apply(this).size() * Math.max(0, settlement.tier() - 1);
    }
}
