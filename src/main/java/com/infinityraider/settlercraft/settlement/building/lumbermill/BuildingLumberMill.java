package com.infinityraider.settlercraft.settlement.building.lumbermill;

import com.infinityraider.settlercraft.api.v1.*;
import com.infinityraider.settlercraft.settlement.building.BuildingBase;
import com.infinityraider.settlercraft.settlement.building.BuildingTypeRegistry;
import com.infinityraider.settlercraft.settlement.settler.profession.ProfessionRegistry;
import net.minecraft.entity.player.EntityPlayer;

public class BuildingLumberMill extends BuildingBase {
    public BuildingLumberMill() {
        super("lumber_mill");
    }

    @Override
    public IBuildingType buildingType() {
        return BuildingTypeRegistry.getInstance().buildingTypeLumberMill();
    }

    @Override
    public boolean canBuild(EntityPlayer player, ISettlement settlement) {
        return settlement.tier() >= 2;
    }

    @Override
    public int maxInhabitants() {
        return 2;
    }

    @Override
    public boolean canSettlerLiveHere(ISettlementBuilding building, ISettler settler) {
        return settler.profession() == ProfessionRegistry.getInstance().professionLumberJack();
    }

    @Override
    public boolean canSettlerWorkHere(ISettlementBuilding building, ISettler settler) {
        return settler.profession() == ProfessionRegistry.getInstance().professionLumberJack();
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
