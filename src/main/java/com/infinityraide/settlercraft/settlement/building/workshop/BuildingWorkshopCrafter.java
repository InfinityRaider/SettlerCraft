package com.infinityraide.settlercraft.settlement.building.workshop;

import com.infinityraide.settlercraft.api.v1.ISettlement;
import com.infinityraide.settlercraft.api.v1.ISettlementBuilding;
import com.infinityraide.settlercraft.api.v1.ISettler;
import com.infinityraide.settlercraft.api.v1.ITask;
import net.minecraft.entity.player.EntityPlayer;

public class BuildingWorkshopCrafter extends BuildingWorkShop {
    public BuildingWorkshopCrafter() {
        super("workshop_crafting");
    }

    @Override
    public boolean canBuild(EntityPlayer player, ISettlement settlement) {
        return settlement.tier() >=2;
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
