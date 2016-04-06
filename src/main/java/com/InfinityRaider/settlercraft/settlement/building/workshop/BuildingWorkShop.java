package com.InfinityRaider.settlercraft.settlement.building.workshop;

import com.InfinityRaider.settlercraft.api.v1.*;
import com.InfinityRaider.settlercraft.settlement.building.BuildingBase;
import com.InfinityRaider.settlercraft.settlement.building.BuildingTypeRegistry;

public abstract class BuildingWorkShop extends BuildingBase {
    public BuildingWorkShop(String name) {
        super(name);
    }

    @Override
    public IBuildingType buildingType() {
        return BuildingTypeRegistry.getInstance().buildingTypeWorkshop();
    }

    @Override
    public int maxInhabitants() {
        return 0;
    }
}
