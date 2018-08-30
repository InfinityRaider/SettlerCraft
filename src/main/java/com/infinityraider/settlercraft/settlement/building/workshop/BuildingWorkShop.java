package com.infinityraider.settlercraft.settlement.building.workshop;

import com.infinityraider.settlercraft.api.v1.IBuildingType;
import com.infinityraider.settlercraft.settlement.building.BuildingBase;
import com.infinityraider.settlercraft.settlement.building.BuildingTypeRegistry;
import com.infinityraider.settlercraft.api.v1.ISettlementBuilding;
import com.infinityraider.settlercraft.api.v1.ISettler;

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

    @Override
    public boolean canSettlerLiveHere(ISettlementBuilding building, ISettler settler) {
        return false;
    }
}
