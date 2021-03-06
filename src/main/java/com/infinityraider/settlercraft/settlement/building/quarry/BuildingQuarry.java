package com.infinityraider.settlercraft.settlement.building.quarry;

import com.infinityraider.settlercraft.api.v1.*;
import com.infinityraider.settlercraft.settlement.building.BuildingBase;
import com.infinityraider.settlercraft.settlement.building.BuildingTypeRegistry;
import com.infinityraider.settlercraft.settlement.settler.profession.ProfessionRegistry;
import com.infinityraider.settlercraft.settlement.settler.profession.miner.TaskMine;
import net.minecraft.entity.player.EntityPlayer;

public class BuildingQuarry extends BuildingBase {
    public BuildingQuarry() {
        super("quarry");
    }

    @Override
    public IBuildingType buildingType() {
        return BuildingTypeRegistry.getInstance().buildingTypeQuarry();
    }

    @Override
    public boolean canBuild(EntityPlayer player, ISettlement settlement) {
        return settlement.tier() >= 2;
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
        return settler.profession() == ProfessionRegistry.getInstance().professionMiner();
    }

    @Override
    public ITask getTaskForSettler(ISettlementBuilding building, ISettler settler) {
        return new TaskMine(building.settlement(), settler, building, this);
    }

    @Override
    public boolean needsUpdateTicks() {
        return false;
    }

    @Override
    public void onUpdateTick(ISettlementBuilding building) {

    }
}
