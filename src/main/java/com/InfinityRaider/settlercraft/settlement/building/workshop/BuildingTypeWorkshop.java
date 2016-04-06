package com.InfinityRaider.settlercraft.settlement.building.workshop;

import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.settlement.building.BuildingRegistry;
import com.InfinityRaider.settlercraft.settlement.building.BuildingTypeBase;

public class BuildingTypeWorkshop extends BuildingTypeBase {
    public BuildingTypeWorkshop() {
        super("workshop");
    }

    @Override
    public int maximumBuildingCountPerSettlement(ISettlement settlement) {
        return BuildingRegistry.getInstance().apply(this).size()*2;
    }
}
