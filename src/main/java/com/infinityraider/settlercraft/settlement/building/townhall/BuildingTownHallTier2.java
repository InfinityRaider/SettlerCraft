package com.infinityraider.settlercraft.settlement.building.townhall;

import com.infinityraider.settlercraft.api.v1.ISettlement;
import com.infinityraider.settlercraft.settlement.building.BuildingTypeRegistry;
import net.minecraft.entity.player.EntityPlayer;

public class BuildingTownHallTier2 extends BuildingTownHall {
    public BuildingTownHallTier2() {
        super(2);
    }

    @Override
    public boolean canBuild(EntityPlayer player, ISettlement settlement) {
        return settlement.getCompletedBuildings(BuildingTypeRegistry.getInstance().buildingTypeFarm()).size() >= 2
                && (settlement.population() >= 10)
                && (settlement.getCompletedBuildings().size() >= 5);
    }
}
