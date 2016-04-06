package com.InfinityRaider.settlercraft.settlement.building.workshop;

import com.InfinityRaider.settlercraft.api.v1.*;
import net.minecraft.entity.player.EntityPlayer;

public class BuildingWorkshopCrafter extends BuildingWorkShop {
    public BuildingWorkshopCrafter() {
        super("workshop_crafting");
    }

    @Override
    public boolean canBuild(EntityPlayer player, ISettlement settlement) {
        return false;
    }

    @Override
    public IInventorySerializable getDefaultInventory() {
        return null;
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
