package com.InfinityRaider.settlercraft.settlement.building.townhall;

import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.settlement.building.BuildingTypeRegistry;
import net.minecraft.entity.player.EntityPlayer;

public class BuildingTownHallTier3 extends BuildingTownHall {
    public BuildingTownHallTier3() {
        super(3);
    }

    @Override
    public boolean canBuild(EntityPlayer player, ISettlement settlement) {
        return settlement.getCompletedBuildings(BuildingTypeRegistry.getInstance().buildingTypeAcademy()).size() >= 1
                && settlement.population() >= 20
                && settlement.getCompletedBuildings().size() >= 10;
    }
}
