package com.InfinityRaider.settlercraft.settlement.building.wall;

public class BuildingWallSegment extends BuildingWallBase {
    public BuildingWallSegment() {
        super("wall_segment");
    }

    @Override
    public int maxNumberOfGuards() {
        return 1;
    }
}
