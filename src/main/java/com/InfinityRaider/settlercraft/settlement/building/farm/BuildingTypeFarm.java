package com.InfinityRaider.settlercraft.settlement.building.farm;

import com.InfinityRaider.settlercraft.api.v1.IBuilding;
import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.settlement.building.BuildingTypeBase;

public class BuildingTypeFarm extends BuildingTypeBase {
    public BuildingTypeFarm() {
        super("farm");
    }

    @Override
    public int maximumBuildingCountPerSettlement(ISettlement settlement) {
        return 2;
    }

    @Override
    public IBuilding startingBuilding() {
        return null;
    }
}
