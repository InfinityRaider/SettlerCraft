package com.infinityraider.settlercraft.settlement.settler.ai.pathfinding.astar;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.*;

public class AStar {
    private final EntityLiving entity;
    private final World world;
    private final Node start;
    private final Node target;
    private final double max_dist;
    private final int searchRange;

    private final List<Node> closedSet;
    private final List<Node> openSet;
    private final Map<Node, Node> cameFrom;

    private boolean openDoors;
    private boolean openFences;
    private boolean swim;
    private boolean climbLadders;
    private int maxFallHeight;
    private int entityHeight;

    public AStar(EntityLiving entity, BlockPos start, BlockPos target, double max_dist, int searchRange) {
        this.entity = entity;
        this.world = entity.getEntityWorld();
        this.start = getNodeForPosition(start).gScore(0);
        this.target = getNodeForPosition(target);
        this.max_dist = max_dist;
        this.searchRange = searchRange;
        this.closedSet = new ArrayList<>();
        this.openSet = new ArrayList<>();
        this.cameFrom = new IdentityHashMap<>();
        this.openSet.add(this.start.fScore(estimateHeuristicCost(this.start, this.target)));
        this.maxFallHeight = 1;
    }

    public Path getPath() {
        List<Node> nodes = run();
        if (nodes == null || nodes.size() <= 0) {
            return null;
        }
        PathPoint[] pathPoints = new PathPoint[nodes.size()];
        for (int i = 0; i < pathPoints.length; i++) {
            pathPoints[i] = nodes.get(nodes.size() - i - 1).toPathPoint();
        }
        return new Path(pathPoints);
    }

    public List<Node> run() {
        if (!checkDistance()) {
            return null;
        }
        buildNodes();
        while (openSet.size() > 0) {
            Node current = currentNode();
            if (!current.isValid()) {
                return null;
            }
            if (current.getNeighbours().size() <= 0) {
                return null;
            }
            if (current == target) {
                return buildPath();
            }
            openSet.remove(current);
            closedSet.add(current);
            for (Node neighbour : current.getNeighbours()) {
                if (closedSet.contains(neighbour)) {
                    continue;
                }
                int gScore = current.gScore() + neighbour.getCost();
                if (!openSet.contains(neighbour)) {
                    openSet.add(neighbour);
                } else if (gScore >= neighbour.gScore()) {
                    continue;
                }
                cameFrom.put(neighbour, current);
                neighbour.gScore(gScore);
                neighbour.fScore(gScore + estimateHeuristicCost(neighbour, target));
            }
        }
        return null;
    }

    private int estimateHeuristicCost(Node start, Node target) {
        return Math.abs(start.getX() - target.getX()) + Math.abs(start.getY() - target.getY()) + Math.abs(start.getZ() - target.getZ());
    }

    private boolean checkDistance() {
        double x1 = start.getX() + 0.5;
        double x2 = target.getX() + 0.5;
        double y1 = start.getY() + 0.5;
        double y2 = target.getY() + 0.5;
        double z1 = start.getZ() + 0.5;
        double z2 = target.getZ() + 0.5;
        return max_dist * max_dist >= (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2);
    }

    private Node currentNode() {
        int fScore = Integer.MAX_VALUE;
        Node current = null;
        for (Node node : openSet) {
            if (node.fScore() <= fScore) {
                current = node;
            }
        }
        return current;
    }

    private List<Node> buildPath() {
        List<Node> path = new ArrayList<>();
        Node node = target;
        while (node != null) {
            path.add(target);
            node = cameFrom.get(node);
        }
        return path;
    }

    public AStar setOpenDoors(boolean status) {
        this.openDoors = status;
        return this;
    }

    public AStar setOpenFences(boolean status) {
        this.openFences = status;
        return this;
    }

    public AStar setSwim(boolean status) {
        this.swim = status;
        return this;
    }

    public AStar setClimbLadders(boolean status) {
        this.climbLadders = status;
        return this;
    }

    public AStar setMaxFallHeight(int height) {
        this.maxFallHeight = Math.max(1, height);
        return this;
    }

    public AStar setEntityHeight(int height) {
        this.entityHeight = Math.max(1, height);
        return this;
    }

    public Node[][][] buildNodes() {
        int minX = Math.min(start.getX(), target.getX());
        int minY = Math.min(start.getY(), target.getY());
        int minZ = Math.min(start.getZ(), target.getZ());
        int maxX = Math.max(start.getX(), target.getX());
        int maxY = Math.max(start.getY(), target.getY());
        int maxZ = Math.max(start.getZ(), target.getZ());
        minX = minX - searchRange;
        minY = minY - searchRange;
        minZ = minZ - searchRange;
        maxX = maxX + searchRange;
        maxY = maxY + searchRange;
        maxZ = maxZ + searchRange;

        Node[][][] nodes = new Node[maxX - minX + 1][maxY - minY + 1][maxZ - minZ + 1];
        //Build node map
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    nodes[x - minX][y - minY][z - minZ] = getNodeForPosition(new BlockPos(x, y, z));
                }
            }
        }
        //Check connections between nodes
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Node nodeFrom = nodes[x - minX][y - minY][z - minZ];
                    if (!nodeFrom.isValid()) {
                        continue;
                    }
                    for (Direction dir : nodeFrom.getValidDirections()) {
                        int xCoord = x + dir.x() - minX;
                        int yCoord = y + dir.y() - minY;
                        int zCoord = z + dir.z() - minZ;
                        if (xCoord >= 0 && xCoord < nodes.length && yCoord >= 0 && yCoord < nodes[0].length && zCoord >= 0 && zCoord < nodes[0][0].length) {
                            Node nodeTo = nodes[xCoord][yCoord][zCoord];
                            if (nodeTo.isValid()) {
                                nodeFrom.addNeighbour(nodeTo);
                            }
                        }
                    }
                }
            }
        }
        return nodes;
    }

    public Node getNodeForPosition(BlockPos pos) {
        if (target != null && pos.equals(target.getPosition())) {
            return target;
        }
        if (start != null && pos.equals(start.getPosition())) {
            return start;
        }
        if (pos.getY() < 0 || pos.getY() > 255) {
            return new Node.Doom(world, pos);
        }
        IBlockState state = world.getBlockState(pos);
        for (int i = 1; i < entityHeight; i++) {
            BlockPos posAt = pos.add(0, i, 0);
            IBlockState above = world.getBlockState(posAt);
            if (above.getMaterial() == Material.AIR) {
                continue;
            }
            if (above.getMaterial() == Material.WATER && swim) {
                continue;
            }
            return new Node.Doom(world, pos);
        }
        if (state.getMaterial() == Material.AIR) {
            return getNodeForAir(pos);
        } else if (state.getMaterial() == Material.WATER) {
            if (swim) {
                return new Node.Swim(world, pos);
            } else {
                return new Node.Doom(world, pos);
            }
        } else if (canClimb(state)) {
            return new Node.Climb(world, pos);
        } else if (isSafeToStandAt(pos.getX(), pos.getY(), pos.getZ())) {
            return new Node.Ground(world, pos);
        } else if (openFences && state.getBlock() instanceof BlockFenceGate) {
            return new Node.Ground(world, pos);
        }
        return new Node.Doom(world, pos);
    }

    private Node getNodeForAir(BlockPos pos) {
        if(isSafeToStandAt(pos.getX(), pos.getY(), pos.getZ())) {
            return new Node.Ground(world, pos);
        }
        int fallCount = 0;
        IBlockState below;
        boolean flag = false;
        while (fallCount <= maxFallHeight) {
            fallCount = fallCount + 1;
            BlockPos posAt = pos.add(0, -fallCount, 0);
            if (posAt.getY() < 0) {
                break;
            }
            below = world.getBlockState(posAt);
            if (below.getMaterial() != Material.AIR) {
                flag = true;
                break;
            }
        }
        if (flag) {
            if (fallCount <= 0) {
                return new Node.Ground(world, pos);
            } else {
                return new Node.Fall(world, pos);
            }
        } else {
            return new Node.Doom(world, pos);
        }
    }

    private boolean canClimb(IBlockState state) {
        return climbLadders && (state.getBlock() instanceof BlockLadder || state.getBlock() instanceof BlockVine);
    }

    private boolean isSafeToStandAt(int x, int y, int z) {
        if(world.getBlockState(new BlockPos(x, y -1, z)).getMaterial() == Material.AIR) {
            return false;
        }
        int sizeX = MathHelper.ceil(entity.width);
        int sizeY = (int) entity.height + 1;
        int i = x - sizeX / 2;
        int j = z - sizeX / 2;
        if (!this.isPositionClear(i, y, j)) {
            return false;
        } else {
            for (int k = i; k < i + sizeX; ++k) {
                for (int l = j; l < j + sizeX; ++l) {
                    PathNodeType pathnodetype = this.getPathNodeType(world, k, y - 1, l, sizeX, sizeY, sizeX);
                    if (pathnodetype == PathNodeType.WATER) {
                        return swim;
                    }
                    if (pathnodetype == PathNodeType.LAVA) {
                        return false;
                    }
                    if (pathnodetype == PathNodeType.OPEN) {
                        return false;
                    }
                    pathnodetype = this.getPathNodeType(world, k, y, l, sizeX, sizeY, sizeX);
                    float f = this.entity.getPathPriority(pathnodetype);
                    if (f < 0.0F || f >= 8.0F) {
                        return false;
                    }
                    if (pathnodetype == PathNodeType.DAMAGE_FIRE || pathnodetype == PathNodeType.DANGER_FIRE || pathnodetype == PathNodeType.DAMAGE_OTHER) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private PathNodeType getPathNodeType(IBlockAccess world, int x, int y, int z, int sizeX, int sizeY, int sizeZ) {
        EnumSet<PathNodeType> enumSet = EnumSet.noneOf(PathNodeType.class);
        PathNodeType pathnodetype = PathNodeType.BLOCKED;
        double d0 = (double) entity.width / 2.0D;
        BlockPos blockpos = new BlockPos(entity);
        for (int i = x; i < x + sizeX; ++i) {
            for (int j = y; j < y + sizeY; ++j) {
                for (int k = z; k < z + sizeZ; ++k) {
                    PathNodeType pathNodeType1 = getPathNodeTypeRaw(world, i, j, k);
                    if (pathNodeType1 == PathNodeType.DOOR_WOOD_CLOSED || pathNodeType1 == PathNodeType.DOOR_OPEN) {
                        pathNodeType1 = openDoors ? PathNodeType.WALKABLE : PathNodeType.BLOCKED;
                    }
                    if (pathNodeType1 == PathNodeType.RAIL && !(world.getBlockState(blockpos).getBlock() instanceof BlockRailBase) && !(world.getBlockState(blockpos.down()).getBlock() instanceof BlockRailBase)) {
                        pathNodeType1 = PathNodeType.FENCE;
                    }
                    if (i == x && j == y && k == z) {
                        pathnodetype = pathNodeType1;
                    }
                    if (j > y && pathNodeType1 != PathNodeType.OPEN) {
                        AxisAlignedBB axisalignedbb = new AxisAlignedBB((double) i - d0 + 0.5D, (double) y + 0.001D, (double) k - d0 + 0.5D,
                                (double) i + d0 + 0.5D, (double) ((float) y + entity.height), (double) k + d0 + 0.5D);
                        if (!entity.getEntityWorld().collidesWithAnyBlock(axisalignedbb)) {
                            pathNodeType1 = PathNodeType.OPEN;
                        }
                    }
                    enumSet.add(pathNodeType1);
                }
            }
        }
        if (enumSet.contains(PathNodeType.FENCE)) {
            return PathNodeType.FENCE;
        } else {
            PathNodeType pathNodeType2 = PathNodeType.BLOCKED;
            for (PathNodeType pathNodeType3 : enumSet) {
                if (entity.getPathPriority(pathNodeType3) < 0.0F) {
                    return pathNodeType3;
                }
                if (entity.getPathPriority(pathNodeType3) >= entity.getPathPriority(pathNodeType2)) {
                    pathNodeType2 = pathNodeType3;
                }
            }
            if (pathnodetype == PathNodeType.OPEN && entity.getPathPriority(pathNodeType2) == 0.0F) {
                return PathNodeType.OPEN;
            } else {
                return pathNodeType2;
            }
        }
    }

    public static PathNodeType getPathNodeTypeRaw(IBlockAccess world, int x, int y, int z) {
        BlockPos blockpos = new BlockPos(x, y, z);
        IBlockState iblockstate = world.getBlockState(blockpos);
        Block block = iblockstate.getBlock();
        Material material = iblockstate.getMaterial();
        return material == Material.AIR ? PathNodeType.OPEN : (block != Blocks.TRAPDOOR && block != Blocks.IRON_TRAPDOOR && block != Blocks.WATERLILY ? (block == Blocks.FIRE ? PathNodeType.DAMAGE_FIRE : (block == Blocks.CACTUS ? PathNodeType.DAMAGE_CACTUS : (block instanceof BlockDoor && material == Material.WOOD && !((Boolean)iblockstate.getValue(BlockDoor.OPEN)).booleanValue() ? PathNodeType.DOOR_WOOD_CLOSED : (block instanceof BlockDoor && material == Material.IRON && !((Boolean)iblockstate.getValue(BlockDoor.OPEN)).booleanValue() ? PathNodeType.DOOR_IRON_CLOSED : (block instanceof BlockDoor && ((Boolean)iblockstate.getValue(BlockDoor.OPEN)).booleanValue() ? PathNodeType.DOOR_OPEN : (block instanceof BlockRailBase ? PathNodeType.RAIL : (!(block instanceof BlockFence) && !(block instanceof BlockWall) && (!(block instanceof BlockFenceGate) || ((Boolean)iblockstate.getValue(BlockFenceGate.OPEN)).booleanValue()) ? (material == Material.WATER ? PathNodeType.WATER : (material == Material.LAVA ? PathNodeType.LAVA : (block.isPassable(world, blockpos) ? PathNodeType.OPEN : PathNodeType.BLOCKED))) : PathNodeType.FENCE))))))) : PathNodeType.TRAPDOOR);
    }

    private boolean isPositionClear(int x, int y, int z) {
        int sizeX = MathHelper.ceil(entity.width);
        int sizeY = (int) entity.height + 1;
        int sizeZ = MathHelper.ceil(entity.width);
        for (BlockPos blockpos : BlockPos.getAllInBox(new BlockPos(x, y, z), new BlockPos(x + sizeX - 1, y + sizeY - 1, z + sizeZ - 1))) {
            Block block = world.getBlockState(blockpos).getBlock();
            if (!block.isPassable(world, blockpos)) {
                return false;
            }
        }
        return true;
    }
}
