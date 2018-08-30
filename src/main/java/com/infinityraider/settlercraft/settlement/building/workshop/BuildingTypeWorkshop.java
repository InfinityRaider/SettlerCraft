package com.infinityraider.settlercraft.settlement.building.workshop;

import com.infinityraider.settlercraft.api.v1.ISettlement;
import com.infinityraider.settlercraft.settlement.building.BuildingRegistry;
import com.infinityraider.settlercraft.settlement.building.BuildingTypeBase;

public class BuildingTypeWorkshop extends BuildingTypeBase {
    public BuildingTypeWorkshop() {
        super("workshop");
    }

    @Override
    public int maximumBuildingCountPerSettlement(ISettlement settlement) {
        return BuildingRegistry.getInstance().apply(this).size()*2;
    }
}
