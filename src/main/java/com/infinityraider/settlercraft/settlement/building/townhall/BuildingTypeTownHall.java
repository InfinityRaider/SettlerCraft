package com.infinityraider.settlercraft.settlement.building.townhall;

import com.infinityraider.settlercraft.api.v1.IBuilding;
import com.infinityraider.settlercraft.api.v1.IBuildingTownHall;
import com.infinityraider.settlercraft.api.v1.ISettlement;
import com.infinityraider.settlercraft.settlement.building.BuildingTypeBase;

public class BuildingTypeTownHall extends BuildingTypeBase {
    public BuildingTypeTownHall() {
        super("town_hall");
    }

    @Override
    public int maximumBuildingCountPerSettlement(ISettlement settlement) {
        return 1;
    }

    @Override
    public boolean addNewBuilding(IBuilding building) {
        return (building instanceof IBuildingTownHall) && super.addNewBuilding(building);
    }
}
