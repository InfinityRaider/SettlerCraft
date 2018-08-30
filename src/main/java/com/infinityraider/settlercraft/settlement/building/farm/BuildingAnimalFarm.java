package com.infinityraider.settlercraft.settlement.building.farm;

import com.infinityraider.settlercraft.api.v1.IBuildingType;
import com.infinityraider.settlercraft.settlement.building.BuildingBase;
import com.infinityraider.settlercraft.settlement.building.BuildingTypeRegistry;
import com.infinityraider.settlercraft.settlement.settler.profession.ProfessionRegistry;
import com.infinityraider.settlercraft.settlement.settler.profession.farmer.TaskFarmAnimals;
import com.infinityraider.settlercraft.api.v1.ISettlementBuilding;
import com.infinityraider.settlercraft.api.v1.ISettler;
import com.infinityraider.settlercraft.api.v1.ITask;

public abstract class BuildingAnimalFarm extends BuildingBase {
    public BuildingAnimalFarm(String name) {
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
        return new TaskFarmAnimals(building.settlement(), settler, building, this);
    }

    @Override
    public boolean needsUpdateTicks() {
        return false;
    }

    @Override
    public void onUpdateTick(ISettlementBuilding building) {

    }
}
