package com.InfinityRaider.settlercraft.settlement.building.townhall;

import com.InfinityRaider.settlercraft.api.v1.IBuildingType;
import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.settlement.building.BuildingTypeRegistry;
import net.minecraft.entity.player.EntityPlayer;

public class BuildingTownHallTier2 extends BuildingTownHall {
    public BuildingTownHallTier2() {
        super(2);
    }

    @Override
    public IBuildingType buildingType() {
        return BuildingTypeRegistry.getInstance().buildingTypeTownHall();
    }

    @Override
    public boolean canBuild(EntityPlayer player, ISettlement settlement) {
        return (settlement.getBuildings(this.buildingType()).size() == 0)
                && (settlement.population() >= 10)
                && (settlement.getBuildings().size() >= 5);
    }
}
