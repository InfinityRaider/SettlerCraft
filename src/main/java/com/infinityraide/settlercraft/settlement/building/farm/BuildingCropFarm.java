package com.infinityraide.settlercraft.settlement.building.farm;

import com.infinityraide.settlercraft.api.v1.IBuildingType;
import com.infinityraide.settlercraft.settlement.building.BuildingBase;
import com.infinityraide.settlercraft.settlement.building.BuildingTypeRegistry;
import com.infinityraide.settlercraft.settlement.settler.profession.ProfessionRegistry;
import com.infinityraide.settlercraft.settlement.settler.profession.farmer.TaskFarmPlants;
import com.infinityraide.settlercraft.api.v1.ISettlementBuilding;
import com.infinityraide.settlercraft.api.v1.ISettler;
import com.infinityraide.settlercraft.api.v1.ITask;

public abstract class BuildingCropFarm extends BuildingBase {
    public BuildingCropFarm(String name) {
        super(name);
    }

    @Override
    public IBuildingType buildingType() {
        return BuildingTypeRegistry.getInstance().buildingTypeFarm();
    }

    @Override
    public boolean canSettlerLiveHere(ISettlementBuilding building, ISettler settler) {
        return false;
    }

    @Override
    public boolean canSettlerWorkHere(ISettlementBuilding building, ISettler settler) {
        return settler.profession() == ProfessionRegistry.getInstance().professionFarmer();
    }

    @Override
    public ITask getTaskForSettler(ISettlementBuilding building, ISettler settler) {
        return new TaskFarmPlants(building.settlement(), settler, building, this);
    }

    @Override
    public boolean needsUpdateTicks() {
        return false;
    }

    @Override
    public void onUpdateTick(ISettlementBuilding building) {}
}
