package com.infinityraider.settlercraft.settlement.building.warehouse;

import com.infinityraider.settlercraft.api.v1.*;
import com.infinityraider.settlercraft.settlement.building.BuildingBase;
import com.infinityraider.settlercraft.settlement.building.BuildingTypeRegistry;
import net.minecraft.entity.player.EntityPlayer;

public class BuildingWareHouse extends BuildingBase {
    public BuildingWareHouse() {
        super("warehouse");
    }

    @Override
    public IBuildingType buildingType() {
        return BuildingTypeRegistry.getInstance().buildingTypeWareHouse();
    }

    @Override
    public boolean canBuild(EntityPlayer player, ISettlement settlement) {
        return settlement.tier() >= 1;
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
    public void onUpdateTick(ISettlementBuilding building) {

    }
}
