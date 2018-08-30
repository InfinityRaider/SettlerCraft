package com.infinityraide.settlercraft.settlement.building.wall;

public class BuildingWallBastion extends BuildingWallBase {
    public BuildingWallBastion() {
        super("wall_bastion");
    }

    @Override
    public int maxNumberOfGuards() {
        return 5;
    }
}
