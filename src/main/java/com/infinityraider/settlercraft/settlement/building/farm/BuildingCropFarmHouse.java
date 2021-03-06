package com.infinityraider.settlercraft.settlement.building.farm;

import com.infinityraider.settlercraft.api.v1.ISettlement;
import com.infinityraider.settlercraft.api.v1.ISettlementBuilding;
import com.infinityraider.settlercraft.api.v1.ISettler;
import com.infinityraider.settlercraft.settlement.settler.profession.ProfessionRegistry;
import net.minecraft.entity.player.EntityPlayer;

public class BuildingCropFarmHouse extends BuildingCropFarm {
    public BuildingCropFarmHouse() {
        super("crop_farm_house");
    }

    @Override
    public boolean canBuild(EntityPlayer player, ISettlement settlement) {
        return settlement.tier() >= 2;
    }

    @Override
    public boolean canSettlerLiveHere(ISettlementBuilding building, ISettler settler) {
        return settler.profession() == ProfessionRegistry.getInstance().professionFarmer();
    }

    @Override
    public int maxInhabitants() {
        return 2;
    }
}
