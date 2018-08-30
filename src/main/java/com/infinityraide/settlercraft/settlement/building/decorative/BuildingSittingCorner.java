package com.infinityraide.settlercraft.settlement.building.decorative;

import com.infinityraide.settlercraft.api.v1.ISettlement;
import net.minecraft.entity.player.EntityPlayer;

public class BuildingSittingCorner extends BuildingDecorative {
    public BuildingSittingCorner() {
        super("sitting_corner");
    }

    @Override
    public boolean canBuild(EntityPlayer player, ISettlement settlement) {
        return settlement.tier() >= 2;
    }
}
