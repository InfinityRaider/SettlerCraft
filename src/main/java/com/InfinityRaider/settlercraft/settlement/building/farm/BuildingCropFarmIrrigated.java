package com.InfinityRaider.settlercraft.settlement.building.farm;

import com.InfinityRaider.settlercraft.api.v1.IInventorySerializable;
import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import net.minecraft.entity.player.EntityPlayer;

public class BuildingCropFarmIrrigated extends BuildingCropFarm {
    public BuildingCropFarmIrrigated() {
        super("crop_farm_irrigated");
    }

    @Override
    public boolean canBuild(EntityPlayer player, ISettlement settlement) {
        return settlement.tier() >= 1;
    }

    @Override
    public IInventorySerializable getDefaultInventory() {
        return null;
    }

    @Override
    public int maxInhabitants() {
        return 0;
    }
}
