package com.InfinityRaider.settlercraft.settlement.building.townhall;

import com.InfinityRaider.settlercraft.api.v1.*;
import com.InfinityRaider.settlercraft.settlement.building.BuildingTypeRegistry;
import net.minecraft.entity.player.EntityPlayer;

public class BuildingTownHallTier1 extends BuildingTownHall {
    public BuildingTownHallTier1() {
        super(1);
    }

    @Override
    public IBuildingType buildingType() {
        return BuildingTypeRegistry.getInstance().buildingTypeTownHall();
    }

    @Override
    public boolean canBuild(EntityPlayer player, ISettlement settlement) {
        return settlement.getBuildings(this.buildingType()).size() == 0;
    }
}
