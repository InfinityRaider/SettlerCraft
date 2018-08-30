package com.infinityraider.settlercraft.settlement.building.academy;

import com.infinityraider.settlercraft.api.v1.ISettlement;
import com.infinityraider.settlercraft.settlement.building.BuildingTypeBase;

public class BuildingTypeAcademy extends BuildingTypeBase {
    public BuildingTypeAcademy() {
        super("academy");
    }

    @Override
    public int maximumBuildingCountPerSettlement(ISettlement settlement) {
        return settlement.tier();
    }
}
