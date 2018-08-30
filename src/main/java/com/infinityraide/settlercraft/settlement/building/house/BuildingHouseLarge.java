package com.infinityraide.settlercraft.settlement.building.house;

import com.infinityraide.settlercraft.api.v1.ISettlement;
import net.minecraft.entity.player.EntityPlayer;

public class BuildingHouseLarge extends BuildingHouse {
    public BuildingHouseLarge() {
        super("large");
    }

    @Override
    public boolean canBuild(EntityPlayer player, ISettlement settlement) {
        return settlement.tier() >= 2 && settlement.population() >= 10;
    }

    @Override
    public int maxInhabitants() {
        return 4;
    }
}
