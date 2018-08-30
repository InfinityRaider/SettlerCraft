package com.infinityraide.settlercraft.settlement.building.workshop;

import com.infinityraide.settlercraft.api.v1.IBuildingType;
import com.infinityraide.settlercraft.settlement.building.BuildingBase;
import com.infinityraide.settlercraft.settlement.building.BuildingTypeRegistry;
import com.infinityraide.settlercraft.api.v1.ISettlementBuilding;
import com.infinityraide.settlercraft.api.v1.ISettler;

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
