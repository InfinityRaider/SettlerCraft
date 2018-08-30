package com.infinityraide.settlercraft.settlement.building.decorative;

import com.infinityraide.settlercraft.api.v1.IBuildingType;
import com.infinityraide.settlercraft.api.v1.ISettlementBuilding;
import com.infinityraide.settlercraft.api.v1.ISettler;
import com.infinityraide.settlercraft.api.v1.ITask;
import com.infinityraide.settlercraft.settlement.building.BuildingBase;
import com.infinityraide.settlercraft.settlement.building.BuildingTypeRegistry;

public abstract class BuildingDecorative extends BuildingBase {
    public BuildingDecorative(String name) {
        super(name);
    }

    @Override
    public IBuildingType buildingType() {
        return BuildingTypeRegistry.getInstance().buildingTypeDecorative();
    }

    @Override
    public int maxInhabitants() {
        return 0;
    }

    @Override
    public boolean canSettlerLiveHere(ISettlementBuilding building, ISettler settler) {
        return false;
    }

    @Override
    public boolean canSettlerWorkHere(ISettlementBuilding building, ISettler settler) {
        return false;
    }

    @Override
    public ITask getTaskForSettler(ISettlementBuilding building, ISettler settler) {
        return null;
    }

    @Override
    public boolean needsUpdateTicks() {
        return false;
    }

    @Override
    public void onUpdateTick(ISettlementBuilding building) {}
}
