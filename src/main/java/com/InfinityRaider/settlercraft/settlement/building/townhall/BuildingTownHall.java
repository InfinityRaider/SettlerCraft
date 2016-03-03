package com.InfinityRaider.settlercraft.settlement.building.townhall;

import com.InfinityRaider.settlercraft.api.v1.*;
import com.InfinityRaider.settlercraft.settlement.building.BuildingTypeRegistry;
import com.InfinityRaider.settlercraft.settlement.building.BuildingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class BuildingTownHall extends BuildingBase {
    public BuildingTownHall() {
        super("town_hall1");
    }

    @Override
    public IBuildingType buildingType() {
        return BuildingTypeRegistry.getInstance().buildingTypeTownHall();
    }

    @Override
    public boolean canBuild(EntityPlayer player, ISettlement settlement) {
        return settlement.getBuildings(this.buildingType()).size() == 0;
    }

    @Override
    public IInventory getStartingInventory() {
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
    public ITask getTaskForVillager(ISettlementBuilding building, ISettler settler) {
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
