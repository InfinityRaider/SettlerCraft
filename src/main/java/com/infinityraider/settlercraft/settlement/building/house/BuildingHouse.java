package com.infinityraider.settlercraft.settlement.building.house;

import com.infinityraider.settlercraft.api.v1.IBuildingType;
import com.infinityraider.settlercraft.settlement.building.BuildingBase;
import com.infinityraider.settlercraft.settlement.building.BuildingTypeRegistry;
import com.infinityraider.settlercraft.api.v1.ISettlementBuilding;
import com.infinityraider.settlercraft.api.v1.ISettler;
import com.infinityraider.settlercraft.api.v1.ITask;

public abstract class BuildingHouse extends BuildingBase {
    public BuildingHouse(String type) {
        super("house_" + type);
    }

    @Override
    public IBuildingType buildingType() {
        return BuildingTypeRegistry.getInstance().buildingTypeHouse();
    }

    @Override
    public boolean canSettlerLiveHere(ISettlementBuilding building, ISettler settler) {
        return true;
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
