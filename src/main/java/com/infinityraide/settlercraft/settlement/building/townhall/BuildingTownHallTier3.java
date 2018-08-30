package com.infinityraide.settlercraft.settlement.building.townhall;

import com.infinityraide.settlercraft.api.v1.ISettlement;
import com.infinityraide.settlercraft.settlement.building.BuildingTypeRegistry;
import net.minecraft.entity.player.EntityPlayer;

public class BuildingTownHallTier3 extends BuildingTownHall {
    public BuildingTownHallTier3() {
        super(3);
    }

    @Override
    public boolean canBuild(EntityPlayer player, ISettlement settlement) {
        return settlement.getCompletedBuildings(BuildingTypeRegistry.getInstance().buildingTypeWorkshop()).size() >= 2
                && settlement.population() >= 20
                && settlement.getCompletedBuildings().size() >= 10;
    }
}
