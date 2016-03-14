package com.InfinityRaider.settlercraft.settlement.building.farm;

import com.InfinityRaider.settlercraft.api.v1.*;
import com.InfinityRaider.settlercraft.settlement.building.BuildingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class BuildingFarm extends BuildingBase {
    public BuildingFarm() {
        super("farm1");
    }

    @Override
    public IBuildingType buildingType() {
        return null;
    }

    @Override
    public boolean canBuild(EntityPlayer player, ISettlement settlement) {
        return false;
    }

    @Override
    public IInventorySerializable getStartingInventory() {
        return null;
    }

    @Override
    public int maxInhabitants() {
        return 0;
    }

    @Override
    public boolean canSettlerWorkHere(ISettlementBuilding building, ISettler settler) {
        return false;
    }

    @Override
    public List<ITask> getTasksForVillager(ISettlementBuilding building, ISettler settler) {
        return null;
    }

    @Override
    public boolean needsUpdateTicks() {
        return false;
    }

    @Override
    public void onUpdateTick(ISettlementBuilding building) {}
}
