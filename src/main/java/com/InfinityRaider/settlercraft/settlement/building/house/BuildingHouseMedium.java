package com.InfinityRaider.settlercraft.settlement.building.house;

import com.InfinityRaider.settlercraft.api.v1.IInventorySerializable;
import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.settlement.building.BuildingTypeRegistry;
import net.minecraft.entity.player.EntityPlayer;

public class BuildingHouseMedium extends BuildingHouse {
    public BuildingHouseMedium() {
        super("medium");
    }

    @Override
    public boolean canBuild(EntityPlayer player, ISettlement settlement) {
        return settlement.getCompletedBuildings(BuildingTypeRegistry.getInstance().buildingTypeTownHall()).size() > 0
                && settlement.getSettlementInhabitants().size() >= 2;
    }

    @Override
    public IInventorySerializable getDefaultInventory() {
        return null;
    }

    @Override
    public int maxInhabitants() {
        return 3;
    }
}
