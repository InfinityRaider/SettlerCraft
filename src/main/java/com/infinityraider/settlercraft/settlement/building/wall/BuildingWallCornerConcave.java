package com.infinityraider.settlercraft.settlement.building.wall;

public class BuildingWallCornerConcave extends BuildingWallBase {
    public BuildingWallCornerConcave() {
        super("wall_corner_concave");
    }

    @Override
    public int maxNumberOfGuards() {
        return 1;
    }
}
