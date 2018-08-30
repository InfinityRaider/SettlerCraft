package com.infinityraider.settlercraft.settlement.building.farm;

import com.infinityraider.settlercraft.api.v1.ISettlement;
import com.infinityraider.settlercraft.settlement.building.BuildingTypeBase;

public class BuildingTypeFarm extends BuildingTypeBase {
    public BuildingTypeFarm() {
        super("farm");
    }

    @Override
    public int maximumBuildingCountPerSettlement(ISettlement settlement) {
        return 1 + (settlement.population() / 5);
    }
}
