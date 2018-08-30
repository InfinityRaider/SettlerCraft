package com.infinityraider.settlercraft.settlement.building.wall;

public class BuildingWallBastion extends BuildingWallBase {
    public BuildingWallBastion() {
        super("wall_bastion");
    }

    @Override
    public int maxNumberOfGuards() {
        return 5;
    }
}
