package com.infinityraider.settlercraft.settlement.building.wall;

public class BuildingWallGate extends BuildingWallBase {
    public BuildingWallGate() {
        super("wall_gate");
    }

    @Override
    public int maxNumberOfGuards() {
        return 3;
    }
}
