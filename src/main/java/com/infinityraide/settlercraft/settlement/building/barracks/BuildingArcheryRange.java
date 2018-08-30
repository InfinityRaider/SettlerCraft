package com.infinityraide.settlercraft.settlement.building.barracks;

import com.infinityraide.settlercraft.api.v1.*;
import com.infinityraide.settlercraft.settlement.building.BuildingBase;
import com.infinityraide.settlercraft.settlement.building.BuildingTypeRegistry;
import com.infinityraide.settlercraft.settlement.settler.profession.ProfessionRegistry;
import net.minecraft.entity.player.EntityPlayer;

public class BuildingArcheryRange extends BuildingBase {
    public BuildingArcheryRange() {
        super("archery_range");
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
    public int maxInhabitants() {
        return 3;
    }

    @Override
    public boolean canSettlerLiveHere(ISettlementBuilding building, ISettler settler) {
        return settler.profession() == ProfessionRegistry.getInstance().professionSoldier();
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
