package com.infinityraider.settlercraft.settlement.building.decorative;

import com.infinityraider.settlercraft.api.v1.ISettlement;
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
