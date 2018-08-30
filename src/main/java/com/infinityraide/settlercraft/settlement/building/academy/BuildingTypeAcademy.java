package com.infinityraide.settlercraft.settlement.building.academy;

import com.infinityraide.settlercraft.api.v1.ISettlement;
import com.infinityraide.settlercraft.settlement.building.BuildingTypeBase;

public class BuildingTypeAcademy extends BuildingTypeBase {
    public BuildingTypeAcademy() {
        super("academy");
    }

    @Override
    public int maximumBuildingCountPerSettlement(ISettlement settlement) {
        return settlement.tier();
    }
}
