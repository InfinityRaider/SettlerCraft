package com.InfinityRaider.settlercraft.settlement.building.farm;

import com.InfinityRaider.settlercraft.api.v1.IInventorySerializable;
import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import net.minecraft.entity.player.EntityPlayer;

public class BuildingAnimalFarmStable extends BuildingAnimalFarm {
    public BuildingAnimalFarmStable() {
        super("cattle_farm_stable");
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
