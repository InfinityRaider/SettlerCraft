package com.InfinityRaider.settlercraft.settlement.building.barracks;

import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.settlement.building.BuildingRegistry;
import com.InfinityRaider.settlercraft.settlement.building.BuildingTypeBase;

public class BuildingTypeBarracks extends BuildingTypeBase {
    public BuildingTypeBarracks() {
        super("barracks");
    }

    @Override
    public int maximumBuildingCountPerSettlement(ISettlement settlement) {
        return BuildingRegistry.getInstance().apply(this).size() * Math.max(0, settlement.tier() - 1);
    }
}
