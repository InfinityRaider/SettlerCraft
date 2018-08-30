package com.infinityraider.settlercraft.settlement.settler.ai.pathfinding.astar;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.EnumSet;

public class NodeCreator {
    protected NodeCreator() {}

    public Node getNodeForPosition(World world, ChunkCache cache, BlockPos pos, IBlockState state, PathFinderSettings settings) {
        Node node;
        if (!this.isValidPosition(pos)) {
            node = new Node.Doom(world, pos);
        } else if (this.isPassableBlock(cache, pos, state)) {
            node = this.getNodeForPassableBlock(world, cache, pos, settings);
        } else if (this.isSwimmableLiquid(state)) {
            node = this.getNodeForLiquid(world, pos, settings);
        } else {
            node = this.getNodeForBlock(world, cache, pos, state, settings);
        }
        return node;
    }

    public Node checkNodeConnections(ChunkCache cache, Node node, PathFinderSettings settings) {
        return node;
    }

    protected boolean isValidPosition(BlockPos pos) {
        return (pos.getY() <= 255 || pos.getY() > 0);
    }

    protected boolean isPassableBlock(IBlockAccess world, BlockPos pos, IBlockState state) {
        return state.getBlock().isPassable(world, pos);
    }

    protected Node getNodeForPassableBlock(World world, ChunkCache cache, BlockPos pos, PathFinderSettings settings) {
        int fallCount = 0;
        Node node = null;
        while (node == null) {
            fallCount = fallCount + 1;
            BlockPos posBelow = pos.add(0, -fallCount, 0);
            IBlockState stateBelow = cache.getBlockState(posBelow);
            if(!this.isValidPosition(posBelow)) {
                node = new Node.Doom(world, pos);
            }  else if(!this.isPassableBlock(cache, posBelow, stateBelow)) {
                if (this.canStandOnBlock(cache, posBelow, stateBelow, settings)) {
                    node = new Node.Fall(world, pos);
                } else if(this.isSwimmableLiquid(stateBelow)) {
                    node = settings.canSwim() ? new Node.Swim(world, pos) : new Node.Doom(world, pos);
                }
            }
        }
        if(node instanceof Node.Doom) {
            return node;
        } else {
            return fallCount <= settings.maxFallHeight() ? node : new Node.Doom(world, pos);
        }
    }

    protected boolean isSwimmableLiquid(IBlockState state) {
        return state.getMaterial() == Material.WATER;
    }

    protected Node getNodeForLiquid(World world, BlockPos pos, PathFinderSettings settings) {
        return settings.canSwim() ? new Node.Swim(world, pos) : new Node.Doom(world, pos);
    }

    protected Node getNodeForBlock(World world, ChunkCache cache, BlockPos pos, IBlockState state, PathFinderSettings settings) {
        if(state.getBlock() instanceof BlockDoor) {


        }
        return null;
    }

    protected boolean isClimbableSpace(IBlockAccess world, BlockPos pos, IBlockState state) {
        if(state.getBlock() instanceof BlockLadder || state.getBlock() instanceof BlockVine) {
            return true;
        }
        for(EnumFacing facing : EnumFacing.HORIZONTALS) {
            if (this.canClimbBlockSide(world.getBlockState(pos.offset(facing)), facing)) {
                return true;
            }
        }
        return false;
    }

    protected boolean canClimbBlockSide(IBlockState state, EnumFacing facing) {
        return false;
    }

    protected boolean canStandOnBlock(IBlockAccess world, BlockPos pos, IBlockState state, PathFinderSettings settings) {
        if(state.getMaterial() == Material.AIR) {
            return false;
        }
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        int sizeX = MathHelper.ceil(settings.getEntity().width);
        int sizeY = (int) settings.getEntity().height + 1;
        int i = x - sizeX / 2;
        int j = z - sizeX / 2;
        if (!this.isPositionClear(world, i, y, j, settings)) {
            return false;
        } else {
            for (int k = i; k < i + sizeX; ++k) {
                for (int l = j; l < j + sizeX; ++l) {
                    PathNodeType pathnodetype = this.getPathNodeType(world, k, y - 1, l, sizeX, sizeY, sizeX, settings);
                    if (pathnodetype == PathNodeType.WATER) {
                        return settings.canSwim();
                    }
                    if (pathnodetype == PathNodeType.LAVA) {
                        return false;
                    }
                    if (pathnodetype == PathNodeType.OPEN) {
                        return false;
                    }
                    pathnodetype = this.getPathNodeType(world, k, y, l, sizeX, sizeY, sizeX, settings);
                    float f = settings.getEntity().getPathPriority(pathnodetype);
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

    private PathNodeType getPathNodeType(IBlockAccess world, int x, int y, int z, int sizeX, int sizeY, int sizeZ, PathFinderSettings settings) {
        EnumSet<PathNodeType> enumSet = EnumSet.noneOf(PathNodeType.class);
        PathNodeType pathnodetype = PathNodeType.BLOCKED;
        double d0 = (double) settings.getEntity().width / 2.0D;
        BlockPos blockpos = new BlockPos(settings.getEntity());
        for (int i = x; i < x + sizeX; ++i) {
            for (int j = y; j < y + sizeY; ++j) {
                for (int k = z; k < z + sizeZ; ++k) {
                    PathNodeType pathNodeType1 = AStar.getPathNodeTypeRaw(world, i, j, k);
                    if (pathNodeType1 == PathNodeType.DOOR_WOOD_CLOSED || pathNodeType1 == PathNodeType.DOOR_OPEN) {
                        pathNodeType1 = settings.canOpenDoors() ? PathNodeType.WALKABLE : PathNodeType.BLOCKED;
                    }
                    if (pathNodeType1 == PathNodeType.RAIL && !(world.getBlockState(blockpos).getBlock() instanceof BlockRailBase) && !(world.getBlockState(blockpos.down()).getBlock() instanceof BlockRailBase)) {
                        pathNodeType1 = PathNodeType.FENCE;
                    }
                    if (i == x && j == y && k == z) {
                        pathnodetype = pathNodeType1;
                    }
                    if (j > y && pathNodeType1 != PathNodeType.OPEN) {
                        AxisAlignedBB axisalignedbb = new AxisAlignedBB((double) i - d0 + 0.5D, (double) y + 0.001D, (double) k - d0 + 0.5D,
                                (double) i + d0 + 0.5D, (double) ((float) y + settings.getEntity().height), (double) k + d0 + 0.5D);
                        if (!settings.getEntity().getEntityWorld().collidesWithAnyBlock(axisalignedbb)) {
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
                if (settings.getEntity().getPathPriority(pathNodeType3) < 0.0F) {
                    return pathNodeType3;
                }
                if (settings.getEntity().getPathPriority(pathNodeType3) >= settings.getEntity().getPathPriority(pathNodeType2)) {
                    pathNodeType2 = pathNodeType3;
                }
            }
            if (pathnodetype == PathNodeType.OPEN && settings.getEntity().getPathPriority(pathNodeType2) == 0.0F) {
                return PathNodeType.OPEN;
            } else {
                return pathNodeType2;
            }
        }
    }

    private boolean isPositionClear(IBlockAccess world, int x, int y, int z, PathFinderSettings settings) {
        int sizeX = MathHelper.ceil(settings.getEntity().width);
        int sizeY = (int) settings.getEntity().height + 1;
        int sizeZ = MathHelper.ceil(settings.getEntity().width);
        for (BlockPos blockpos : BlockPos.getAllInBox(new BlockPos(x, y, z), new BlockPos(x + sizeX - 1, y + sizeY - 1, z + sizeZ - 1))) {
            Block block = world.getBlockState(blockpos).getBlock();
            if (!block.isPassable(world, blockpos)) {
                return false;
            }
        }
        return true;
    }
}
