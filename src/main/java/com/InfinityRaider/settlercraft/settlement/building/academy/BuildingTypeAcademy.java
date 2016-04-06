package com.InfinityRaider.settlercraft.settlement.building.academy;

import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.settlement.building.BuildingTypeBase;

public class BuildingTypeAcademy extends BuildingTypeBase {
    public BuildingTypeAcademy() {
        super("academy");
    }

    @Override
    public int maximumBuildingCountPerSettlement(ISettlement settlement) {
        return settlement.tier();
    }
}
