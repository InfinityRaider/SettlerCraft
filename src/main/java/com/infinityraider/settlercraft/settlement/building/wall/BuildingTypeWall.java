package com.infinityraider.settlercraft.settlement.building.wall;

import com.infinityraider.settlercraft.api.v1.ISettlement;
import com.infinityraider.settlercraft.settlement.building.BuildingTypeBase;

public class BuildingTypeWall extends BuildingTypeBase {
    public BuildingTypeWall() {
        super("wall");
    }

    @Override
    public int maximumBuildingCountPerSettlement(ISettlement settlement) {
        return settlement.getBuildings(this).size() + 1;
    }
}
