package com.infinityraider.settlercraft.settlement.building.townhall;

import com.infinityraider.settlercraft.api.v1.ISettlement;
import net.minecraft.entity.player.EntityPlayer;

public class BuildingTownHallTier1 extends BuildingTownHall {
    public BuildingTownHallTier1() {
        super(1);
    }

    @Override
    public boolean canBuild(EntityPlayer player, ISettlement settlement) {
        return true;
    }
}
