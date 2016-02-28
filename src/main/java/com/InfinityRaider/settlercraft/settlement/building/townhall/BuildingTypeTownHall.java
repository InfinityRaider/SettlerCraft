package com.InfinityRaider.settlercraft.settlement.building.townhall;

import com.InfinityRaider.settlercraft.api.v1.IBuilding;
import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.settlement.building.BuildingRegistry;
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
    public IBuilding startingBuilding() {
        return BuildingRegistry.getInstance().TOWN_HALL_1;
    }
}
