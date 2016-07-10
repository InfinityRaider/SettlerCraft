package com.InfinityRaider.settlercraft.settlement.building.barracks;

import com.InfinityRaider.settlercraft.api.v1.*;
import com.InfinityRaider.settlercraft.settlement.building.BuildingBase;
import com.InfinityRaider.settlercraft.settlement.building.BuildingTypeRegistry;
import net.minecraft.entity.player.EntityPlayer;

public class BuildingCavalryStables extends BuildingBase {
    public BuildingCavalryStables() {
        super("cavalry_stables");
    }

    @Override
    public IBuildingType buildingType() {
        return BuildingTypeRegistry.getInstance().buildingTypeBarracks();
    }

    @Override
    public boolean canBuild(EntityPlayer player, ISettlement settlement) {
        return settlement.tier() >= 3;
    }

    @Override
    public IInventorySerializable getDefaultInventory() {
        return null;
    }

    @Override
    public int maxInhabitants() {
        return 3;
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
