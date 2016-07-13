package com.InfinityRaider.settlercraft.settlement.building.house;

import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import net.minecraft.entity.player.EntityPlayer;

public class BuildingHouseMedium extends BuildingHouse {
    public BuildingHouseMedium() {
        super("medium");
    }

    @Override
    public boolean canBuild(EntityPlayer player, ISettlement settlement) {
        return settlement.tier() >= 1
                && settlement.population() >= 2;
    }

    @Override
    public int maxInhabitants() {
        return 3;
    }
}
