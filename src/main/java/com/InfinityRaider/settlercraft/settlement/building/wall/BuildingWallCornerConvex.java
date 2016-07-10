package com.InfinityRaider.settlercraft.settlement.building.wall;

public class BuildingWallCornerConvex extends BuildingWallBase {
    public BuildingWallCornerConvex() {
        super("wall_corner_convex");
    }

    @Override
    public int maxNumberOfGuards() {
        return 1;
    }
}
