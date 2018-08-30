package com.infinityraide.settlercraft.settlement.building.farm;

import com.infinityraide.settlercraft.api.v1.ISettlement;
import com.infinityraide.settlercraft.settlement.building.BuildingTypeBase;

public class BuildingTypeFarm extends BuildingTypeBase {
    public BuildingTypeFarm() {
        super("farm");
    }

    @Override
    public int maximumBuildingCountPerSettlement(ISettlement settlement) {
        return 1 + (settlement.population() / 5);
    }
}
