package com.InfinityRaider.settlercraft.settlement.settler.ai.pathfinding.test;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

public class WalkNodeProcessor extends NodeProcessor {
    private float avoidsWater;

    public void setEntity(IBlockAccess sourceIn, EntityLiving mob) {
        super.setEntity(sourceIn, mob);
        this.avoidsWater = mob.getPathPriority(PathNodeType.WATER);
    }

    /**
     * This method is called when all nodes have been processed and PathEntity is created.
     */
    public void postProcess() {
        super.postProcess();
        this.entity.setPathPriority(PathNodeType.WATER, this.avoidsWater);
    }

    public PathPoint getStartingPoint() {
        //get block pos for the start position
        int y;
        if (this.getCanSwim() && this.entity.isInWater()) {
            y = (int)this.entity.getEntityBoundingBox().minY;
            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(MathHelper.floor_double(this.entity.posX), y, MathHelper.floor_double(this.entity.posZ));
            for (Block block = this.world.getBlockState(pos).getBlock(); block == Blocks.FLOWING_WATER || block == Blocks.WATER; block = this.world.getBlockState(pos).getBlock()) {
                ++y;
                pos.setPos(MathHelper.floor_double(this.entity.posX), y, MathHelper.floor_double(this.entity.posZ));
            }
        }
        else if (!this.entity.onGround) {
            BlockPos blockpos;
            for (blockpos = new BlockPos(this.entity); (this.world.getBlockState(blockpos).getMaterial() == Material.AIR || this.world.getBlockState(blockpos).getBlock().isPassable(this.world, blockpos)) && blockpos.getY() > 0; blockpos = blockpos.down()) {}
            y = blockpos.up().getY();
        } else {
            y = MathHelper.floor_double(this.entity.getEntityBoundingBox().minY + 0.5D);
        }
        BlockPos pos = new BlockPos(this.entity);
        PathNodeType pathNodeType = this.getPathNodeType(this.entity, pos.getX(), y, pos.getZ());

        if (this.entity.getPathPriority(pathNodeType) < 0.0F) {
            Set<BlockPos> set = new HashSet<>();
            set.add(new BlockPos(this.entity.getEntityBoundingBox().minX, (double)y, this.entity.getEntityBoundingBox().minZ));
            set.add(new BlockPos(this.entity.getEntityBoundingBox().minX, (double)y, this.entity.getEntityBoundingBox().maxZ));
            set.add(new BlockPos(this.entity.getEntityBoundingBox().maxX, (double)y, this.entity.getEntityBoundingBox().minZ));
            set.add(new BlockPos(this.entity.getEntityBoundingBox().maxX, (double)y, this.entity.getEntityBoundingBox().maxZ));
            for (BlockPos posInSet : set) {
                PathNodeType pathnodetype = this.getPathNodeType(this.entity, posInSet);
                if (this.entity.getPathPriority(pathnodetype) >= 0.0F) {
                    return this.openPoint(posInSet.getX(), posInSet.getY(), posInSet.getZ());
                }
            }
        }
        return this.openPoint(pos.getX(), y, pos.getZ());
    }

    public PathPoint getEndPoint(double x, double y, double z)
    {
        return this.openPoint(MathHelper.floor_double(x - (double)(this.entity.width / 2.0F)), MathHelper.floor_double(y), MathHelper.floor_double(z - (double)(this.entity.width / 2.0F)));
    }

    /**
     * Fills an array with PathPoints based on the current path point and the target
     * @param pathOptions the options to determine
     * @param current the current path point
     * @param target the target path point
     * @param range the search range
     * @return the index of the last path point option
     */
    public int getPathPointOptions(PathPoint[] pathOptions, PathPoint current, PathPoint target, float range) {
        int index = 0;
        int j = 0;
        PathNodeType pathnodetype = this.getPathNodeType(this.entity, current.xCoord, current.yCoord + 1, current.zCoord);
        if (this.entity.getPathPriority(pathnodetype) >= 0.0F) {
            j = 1;
        }
        BlockPos blockpos = (new BlockPos(current.xCoord, current.yCoord, current.zCoord)).down();
        double deltaY = (double)current.yCoord - (1.0D - this.world.getBlockState(blockpos).getBoundingBox(this.world, blockpos).maxY);
        PathPoint south = this.getPathPointForDirection(current.xCoord, current.yCoord, current.zCoord + 1, j, deltaY, EnumFacing.SOUTH);
        PathPoint west = this.getPathPointForDirection(current.xCoord - 1, current.yCoord, current.zCoord, j, deltaY, EnumFacing.WEST);
        PathPoint east = this.getPathPointForDirection(current.xCoord + 1, current.yCoord, current.zCoord, j, deltaY, EnumFacing.EAST);
        PathPoint north = this.getPathPointForDirection(current.xCoord, current.yCoord, current.zCoord - 1, j, deltaY, EnumFacing.NORTH);
        if (south != null && !south.visited && south.distanceTo(target) < range) {
            pathOptions[index++] = south;
        }
        if (west != null && !west.visited && west.distanceTo(target) < range) {
            pathOptions[index++] = west;
        }
        if (east != null && !east.visited && east.distanceTo(target) < range) {
            pathOptions[index++] = east;
        }
        if (north != null && !north.visited && north.distanceTo(target) < range) {
            pathOptions[index++] = north;
        }
        boolean checkNorth = north == null || north.type == PathNodeType.OPEN || north.priority != 0.0F;
        boolean checkSouth = south == null || south.type == PathNodeType.OPEN || south.priority != 0.0F;
        boolean checkEast = east == null || east.type == PathNodeType.OPEN || east.priority != 0.0F;
        boolean checkWest = west == null || west.type == PathNodeType.OPEN || west.priority != 0.0F;
        if (checkNorth && checkWest) {
            PathPoint newPoint = this.getPathPointForDirection(current.xCoord - 1, current.yCoord, current.zCoord - 1, j, deltaY, EnumFacing.NORTH);
            if (newPoint != null && !newPoint.visited && newPoint.distanceTo(target) < range) {
                pathOptions[index++] = newPoint;
            }
        }
        if (checkNorth && checkEast) {
            PathPoint newPoint = this.getPathPointForDirection(current.xCoord + 1, current.yCoord, current.zCoord - 1, j, deltaY, EnumFacing.NORTH);
            if (newPoint != null && !newPoint.visited && newPoint.distanceTo(target) < range) {
                pathOptions[index++] = newPoint;
            }
        }
        if (checkSouth && checkWest) {
            PathPoint newPoint = this.getPathPointForDirection(current.xCoord - 1, current.yCoord, current.zCoord + 1, j, deltaY, EnumFacing.SOUTH);
            if (newPoint != null && !newPoint.visited && newPoint.distanceTo(target) < range) {
                pathOptions[index++] = newPoint;
            }
        }
        if (checkSouth && checkEast) {
            PathPoint newPoint = this.getPathPointForDirection(current.xCoord + 1, current.yCoord, current.zCoord + 1, j, deltaY, EnumFacing.SOUTH);
            if (newPoint != null && !newPoint.visited && newPoint.distanceTo(target) < range) {
                pathOptions[index++] = newPoint;
            }
        }
        return index;
    }

    private PathPoint getPathPointForDirection(int x, int y, int z, int index, double deltaY, EnumFacing direction) {
        PathPoint point = null;
        BlockPos pos = new BlockPos(x, y, z);
        BlockPos below = pos.down();
        double dY_actual = (double)y - (1.0D - this.world.getBlockState(below).getBoundingBox(this.world, below).maxY);

        if (dY_actual - deltaY > 1.0D) {
            return null;
        } else {
            PathNodeType type = this.getPathNodeType(this.entity, x, y, z);
            float priority = this.entity.getPathPriority(type);
            double halfWidth = (double)this.entity.width / 2.0D;
            if (priority >= 0.0F) {
                point = this.openPoint(x, y, z);
                point.type = type;
                point.priority = Math.max(point.priority, priority);
            }
            if (type == PathNodeType.WALKABLE) {
                return point;
            }
            else {
                if (point == null && index > 0 && type != PathNodeType.FENCE && type != PathNodeType.TRAPDOOR) {
                    point = this.getPathPointForDirection(x, y + 1, z, index - 1, deltaY, direction);
                    if (point != null && (point.type == PathNodeType.OPEN || point.type == PathNodeType.WALKABLE)) {
                        double deltaX = (double)(x - direction.getFrontOffsetX()) + 0.5D;
                        double deltaZ = (double)(z - direction.getFrontOffsetZ()) + 0.5D;
                        AxisAlignedBB entityBox = new AxisAlignedBB(deltaX - halfWidth, (double)y + 0.001D, deltaZ - halfWidth, deltaX + halfWidth, (double)((float)y + this.entity.height), deltaZ + halfWidth);
                        AxisAlignedBB blockBox = this.world.getBlockState(pos).getBoundingBox(this.world, pos);
                        entityBox = entityBox.addCoord(0.0D, blockBox.maxY - 0.002D, 0.0D);
                        if (this.entity.worldObj.collidesWithAnyBlock(entityBox)) {
                            point = null;
                        }
                    }
                }
                if (type == PathNodeType.OPEN) {
                    AxisAlignedBB entityBox = new AxisAlignedBB((double)x - halfWidth + 0.5D, (double)y + 0.001D, (double)z - halfWidth + 0.5D, (double)x + halfWidth + 0.5D, (double)((float)y + this.entity.height), (double)z + halfWidth + 0.5D);
                    if (this.entity.worldObj.collidesWithAnyBlock(entityBox)) {
                        return null;
                    }
                    int i = 0;
                    while (y > 0 && type == PathNodeType.OPEN) {
                        --y;
                        if (i++ >= this.entity.getMaxFallHeight()) {
                            return null;
                        }
                        type = this.getPathNodeType(this.entity, x, y, z);
                        priority = this.entity.getPathPriority(type);
                        if (type != PathNodeType.OPEN && priority >= 0.0F) {
                            point = this.openPoint(x, y, z);
                            point.type = type;
                            point.priority = Math.max(point.priority, priority);
                            break;
                        }
                        if (priority < 0.0F) {
                            return null;
                        }
                    }
                }
                return point;
            }
        }
    }

    public PathNodeType getPathNodeType(IBlockAccess world, int x, int y, int z, EntityLiving entity, int sizeX, int sizeY, int sizeZ, boolean breakDoors, boolean enterDoors) {
        //get a set of all path node types for the entity's bounding box
        EnumSet<PathNodeType> set = EnumSet.noneOf(PathNodeType.class);
        PathNodeType pathnodetype = PathNodeType.BLOCKED;
        double halfWidth = (double)entity.width / 2.0D;
        BlockPos pos = new BlockPos(entity);
        for (int indexX = x; indexX < x + sizeX; ++indexX) {
            for (int indexY = y; indexY < y + sizeY; ++indexY) {
                for (int indexZ = z; indexZ < z + sizeZ; ++indexZ) {
                    PathNodeType type = getPathNodeType(world, indexX, indexY, indexZ);
                    if (type == PathNodeType.DOOR_WOOD_CLOSED && breakDoors && enterDoors) {
                        type = PathNodeType.WALKABLE;
                    }
                    if (type == PathNodeType.DOOR_OPEN && !enterDoors) {
                        type = PathNodeType.BLOCKED;
                    }
                    if (type == PathNodeType.RAIL && !(world.getBlockState(pos).getBlock() instanceof BlockRailBase) && !(world.getBlockState(pos.down()).getBlock() instanceof BlockRailBase)) {
                        type = PathNodeType.FENCE;
                    }
                    if (indexX == x && indexY == y && indexZ == z) {
                        pathnodetype = type;
                    }
                    if (indexY > y && type != PathNodeType.OPEN) {
                        AxisAlignedBB axisalignedbb = new AxisAlignedBB((double)indexX - halfWidth + 0.5D, (double)y + 0.001D, (double)indexZ - halfWidth + 0.5D, (double)indexX + halfWidth + 0.5D, (double)((float)y + entity.height), (double)indexZ + halfWidth + 0.5D);
                        if (!entity.worldObj.collidesWithAnyBlock(axisalignedbb)) {
                            type = PathNodeType.OPEN;
                        }
                    }
                    set.add(type);
                }
            }
        }
        if (set.contains(PathNodeType.FENCE)) {
            return PathNodeType.FENCE;
        } else {
            PathNodeType type = PathNodeType.BLOCKED;
            for (PathNodeType typeInSet : set) {
                if (entity.getPathPriority(typeInSet) < 0.0F) {
                    return typeInSet;
                }
                if (entity.getPathPriority(typeInSet) >= entity.getPathPriority(type)) {
                    type = typeInSet;
                }
            }
            if (pathnodetype == PathNodeType.OPEN && entity.getPathPriority(type) == 0.0F) {
                return PathNodeType.OPEN;
            } else {
                return type;
            }
        }
    }

    private PathNodeType getPathNodeType(EntityLiving entity, BlockPos pos)
    {
        return this.getPathNodeType(this.world, pos.getX(), pos.getY(), pos.getZ(), entity, this.entitySizeX, this.entitySizeY, this.entitySizeZ, this.getCanBreakDoors(), this.getCanEnterDoors());
    }

    private PathNodeType getPathNodeType(EntityLiving entity, int x, int y, int z) {
        return this.getPathNodeType(this.world, x, y, z, entity, this.entitySizeX, this.entitySizeY, this.entitySizeZ, this.getCanBreakDoors(), this.getCanEnterDoors());
    }

    public static PathNodeType getPathNodeType(IBlockAccess world, int x, int y, int z) {
        BlockPos blockpos = new BlockPos(x, y, z);
        IBlockState iblockstate = world.getBlockState(blockpos);
        Block block = iblockstate.getBlock();
        Material material = iblockstate.getMaterial();
        PathNodeType pathnodetype = PathNodeType.BLOCKED;
        if (block != Blocks.TRAPDOOR && block != Blocks.IRON_TRAPDOOR && block != Blocks.WATERLILY) {
            if (block instanceof BlockFire) {
                return PathNodeType.DAMAGE_FIRE;
            } else if (block instanceof BlockCactus) {
                return PathNodeType.DAMAGE_CACTUS;
            } else if (block instanceof BlockDoor && material == Material.WOOD && !iblockstate.getValue(BlockDoor.OPEN)) {
                return PathNodeType.DOOR_WOOD_CLOSED;
            } else if (block instanceof BlockDoor && material == Material.IRON && !iblockstate.getValue(BlockDoor.OPEN)) {
                return PathNodeType.DOOR_IRON_CLOSED;
            } else if (block instanceof BlockDoor && iblockstate.getValue(BlockDoor.OPEN)) {
                return PathNodeType.DOOR_OPEN;
            } else if (block instanceof BlockRailBase) {
                return PathNodeType.RAIL;
            } else if (!(block instanceof BlockFence) && !(block instanceof BlockWall) && (!(block instanceof BlockFenceGate) || iblockstate.getValue(BlockFenceGate.OPEN))) {
                if (material == Material.AIR) {
                    pathnodetype = PathNodeType.OPEN;
                } else {
                    if (material == Material.WATER) {
                        return PathNodeType.WATER;
                    }
                    if (material == Material.LAVA) {
                        return PathNodeType.LAVA;
                    }
                }
                if (block.isPassable(world, blockpos) && pathnodetype == PathNodeType.BLOCKED) {
                    pathnodetype = PathNodeType.OPEN;
                }
                if (pathnodetype == PathNodeType.OPEN && y >= 1) {
                    PathNodeType type = getPathNodeType(world, x, y - 1, z);
                    pathnodetype = type != PathNodeType.WALKABLE && type != PathNodeType.OPEN && type != PathNodeType.WATER && type != PathNodeType.LAVA ? PathNodeType.WALKABLE : PathNodeType.OPEN;
                }
                if (pathnodetype == PathNodeType.WALKABLE) {
                    for (int j = x - 1; j <= x + 1; ++j) {
                        for (int i = z - 1; i <= z + 1; ++i) {
                            if (j != x || i != z) {
                                Block block1 = world.getBlockState(new BlockPos(j, y, i)).getBlock();
                                if (block1 == Blocks.CACTUS) {
                                    pathnodetype = PathNodeType.DANGER_CACTUS;
                                } else if (block1 == Blocks.FIRE) {
                                    pathnodetype = PathNodeType.DANGER_FIRE;
                                }
                            }
                        }
                    }
                }
                return pathnodetype;
            } else {
                return PathNodeType.FENCE;
            }
        } else {
            return PathNodeType.TRAPDOOR;
        }
    }
}