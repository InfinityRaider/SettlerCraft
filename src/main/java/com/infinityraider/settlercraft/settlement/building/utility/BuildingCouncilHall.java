package com.infinityraider.settlercraft.settlement.building.utility;

import com.infinityraider.settlercraft.api.v1.*;
import com.infinityraider.settlercraft.settlement.building.BuildingBase;
import com.infinityraider.settlercraft.settlement.building.BuildingTypeRegistry;
import net.minecraft.entity.player.EntityPlayer;

public class BuildingCouncilHall extends BuildingBase {
    public BuildingCouncilHall() {
        super("council_hall");
    }

    @Override
    public IBuildingType buildingType() {
        return BuildingTypeRegistry.getInstance().buildingTypeUtilities();
    }

    @Override
    public boolean canBuild(EntityPlayer player, ISettlement settlement) {
        return settlement.tier() >= 3 && !this.isBuilt(settlement);
    }

    private boolean isBuilt(ISettlement settlement) {
        for(ISettlementBuilding building : settlement.getBuildings(this.buildingType())) {
            if(building.building() == this) {
                return true;
            }
        }
        return false;
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
