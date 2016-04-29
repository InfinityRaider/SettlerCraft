package com.InfinityRaider.settlercraft.settlement.building.farm;

import com.InfinityRaider.settlercraft.api.v1.IInventorySerializable;
import com.InfinityRaider.settlercraft.api.v1.ISettlement;
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
    public IInventorySerializable getDefaultInventory() {
        return null;
    }

    @Override
    public int maxInhabitants() {
        return 2;
    }
}
