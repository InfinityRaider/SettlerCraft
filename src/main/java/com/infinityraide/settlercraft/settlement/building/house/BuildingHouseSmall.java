package com.infinityraide.settlercraft.settlement.building.house;

import com.infinityraide.settlercraft.api.v1.ISettlement;
import net.minecraft.entity.player.EntityPlayer;

public class BuildingHouseSmall extends BuildingHouse {
    public BuildingHouseSmall() {
        super("small");
    }

    @Override
    public boolean canBuild(EntityPlayer player, ISettlement settlement) {
        return settlement.tier() >= 1;
    }

    @Override
    public int maxInhabitants() {
        return 2;
    }
}
