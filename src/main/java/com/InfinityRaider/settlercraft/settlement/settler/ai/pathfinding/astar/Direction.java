package com.InfinityRaider.settlercraft.settlement.settler.ai.pathfinding.astar;

public enum Direction {
    UP(0, 1, 0),
    DOWN(0, -1, 0),
    NORTH(0, 0, -1),
    EAST(1, 0, 0),
    SOUTH(0, 0, 1),
    WEST(-1, 0, 0),
    NORTH_UP(NORTH, UP),
    EAST_UP(EAST, UP),
    SOUTH_UP(SOUTH, UP),
    WEST_UP(WEST, UP),
    NORTH_DOWN(NORTH, DOWN),
    EAST_DOWN(EAST, DOWN),
    SOUTH_DOWN(SOUTH, DOWN),
    WEST_DOWN(WEST, DOWN),
    NORTH_EAST_UP(NORTH, EAST, UP),
    SOUTH_EAST_UP(SOUTH, EAST, UP),
    NORTH_WEST_UP(NORTH, WEST, UP),
    SOUTH_WEST_UP(SOUTH, WEST, UP),
    NORTH_EAST_DOWN(NORTH, EAST, DOWN),
    SOUTH_EAST_DOWN(SOUTH, EAST, DOWN),
    NORTH_WEST_DOWN(NORTH, WEST, DOWN),
    SOUTH_WEST_DOWN(SOUTH, WEST, DOWN),
    NORTH_EAST(NORTH, EAST),
    SOUTH_EAST(SOUTH, EAST),
    NORTH_WEST(NORTH, WEST),
    SOUTH_WEST(SOUTH, WEST);

    private final int x;
    private final int y;
    private final int z;

    Direction(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    Direction(Direction... directions) {
        int x = 0;
        int y = 0;
        int z = 0;
        for(Direction direction : directions) {
            x = x + direction.x;
            y = y + direction.y;
            z = z + direction.z;
        }
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int x() {
        return this.x;
    }

    public int y() {
        return this.y;
    }

    public int z() {
        return this.z;
    }
}
