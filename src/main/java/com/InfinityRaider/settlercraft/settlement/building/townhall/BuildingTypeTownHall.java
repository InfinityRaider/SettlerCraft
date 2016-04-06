package com.InfinityRaider.settlercraft.settlement.building.townhall;

import com.InfinityRaider.settlercraft.api.v1.IBuilding;
import com.InfinityRaider.settlercraft.api.v1.IBuildingTownHall;
import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.settlement.building.BuildingTypeBase;

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
