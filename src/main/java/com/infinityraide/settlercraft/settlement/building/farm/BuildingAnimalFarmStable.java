package com.infinityraide.settlercraft.settlement.building.farm;

import com.infinityraide.settlercraft.api.v1.ISettlement;
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
    public int maxInhabitants() {
        return 0;
    }
}
