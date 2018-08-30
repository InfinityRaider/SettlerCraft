package com.infinityraider.settlercraft.settlement.settler.ai.pathfinding.astar;

import com.google.common.collect.ImmutableList;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public abstract class Node {
    private final World world;
    private final BlockPos pos;
    private final int cost;
    private final List<Direction> directions;

    private int gScore;
    private int fScore;
    private final List<Node> neighbours;

    public Node(World world, BlockPos pos, Direction... directions) {
        this(world, pos, 1, directions);
    }

    public Node(World world, BlockPos pos, int cost, Direction... directions) {
        this.world = world;
        this.pos = pos;
        this.cost = cost;
        this.directions = ImmutableList.copyOf(directions);
        this.gScore = Integer.MAX_VALUE;
        this.fScore = Integer.MAX_VALUE;
        this.neighbours = new ArrayList<>();
    }

    public World getWorld() {
        return world;
    }

    public BlockPos getPosition() {
        return pos;
    }

    public int getCost() {
        return cost;
    }

    public int getX() {
        return getPosition().getX();
    }

    public int getY() {
        return getPosition().getY();
    }

    public int getZ() {
        return getPosition().getZ();
    }

    public PathPoint toPathPoint() {
        return new PathPoint(getX(), getY(), getZ());
    }

    public Node gScore(int cost) {
        this.gScore = cost;
        return this;
    }

    public int gScore() {
        return gScore;
    }

    public Node fScore(int cost) {
        this.fScore = cost;
        return this;
    }

    public int fScore() {
        return fScore;
    }

    public List<Node> getNeighbours() {
        return neighbours;
    }

    public Node addNeighbour(Node node) {
        this.getNeighbours().add(node);
        return this;
    }

    public List<Direction> getValidDirections() {
        return directions;
    }

    public boolean isValid() {
        return getValidDirections().size() > 0;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof Node)) {
            return false;
        }
        Node other = (Node) obj;
        return this.getWorld() == other.getWorld() && this.getPosition().equals(other.getPosition());
    }

    public static class Fall extends Node {
        public Fall(World world, BlockPos pos) {
            super(world, pos, 2, Direction.DOWN);
        }

        @Override
        public boolean isValid() {
            return true;
        }
    }

    public static class Ground extends Node {
        public Ground(World world, BlockPos pos) {
            super(world, pos,
                    Direction.UP, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST,
                    Direction.NORTH_EAST, Direction.SOUTH_EAST, Direction.SOUTH_WEST, Direction.NORTH_WEST,
                    Direction.NORTH_UP, Direction.EAST_UP, Direction.SOUTH_UP, Direction.WEST_UP,
                    Direction.NORTH_EAST_UP, Direction.SOUTH_EAST_UP, Direction.SOUTH_WEST_UP, Direction.NORTH_WEST_UP,
                    Direction.NORTH_DOWN, Direction.EAST_DOWN, Direction.SOUTH_DOWN, Direction.WEST_DOWN,
                    Direction.NORTH_EAST_DOWN, Direction.SOUTH_EAST_DOWN, Direction.SOUTH_WEST_DOWN, Direction.NORTH_WEST_DOWN);
        }
    }

    public static class Swim extends Node {
        public Swim(World world, BlockPos pos) {
            super(world, pos, 2, Direction.values());
        }
    }

    public static class Climb extends Node {
        public Climb(World world, BlockPos pos) {
            super(world, pos,  Direction.values());
        }
    }

    public static class Doom extends Node {
        public Doom(World world, BlockPos pos) {
            super(world, pos, Integer.MAX_VALUE);
        }
    }
}
