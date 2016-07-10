package com.InfinityRaider.settlercraft.settlement.building.wall;

import com.InfinityRaider.settlercraft.api.v1.*;
import com.InfinityRaider.settlercraft.settlement.building.BuildingBase;
import com.InfinityRaider.settlercraft.settlement.building.BuildingTypeRegistry;
import net.minecraft.entity.player.EntityPlayer;

public abstract class BuildingWallBase extends BuildingBase {
    protected BuildingWallBase(String name) {
        super(name);
    }

    @Override
    public IBuildingType buildingType() {
        return BuildingTypeRegistry.getInstance().buildingTypeWall();
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
        return 0;
    }

    @Override
    public boolean canSettlerWorkHere(ISettlementBuilding building, ISettler settler) {
        return false;
    }

    public abstract int maxNumberOfGuards();

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
