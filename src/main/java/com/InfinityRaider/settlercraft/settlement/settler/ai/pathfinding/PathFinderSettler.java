package com.InfinityRaider.settlercraft.settlement.settler.ai.pathfinding;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class PathFinderSettler extends PathFinder {
    /** The path being generated */
    private final Path path = new Path();
    /** Selection of path points to add to the path */
    private final PathPoint[] pathOptions = new PathPoint[32];
    private final NodeProcessor nodeProcessor;

    public PathFinderSettler(NodeProcessor nodeProcessor) {
        super(nodeProcessor);
        this.nodeProcessor = nodeProcessor;
    }

    public PathEntity func_186333_a(IBlockAccess world, EntityLiving entityFrom, Entity entityTo, float range) {
        return this.getPathTo(world, entityFrom, entityTo.posX, entityTo.getEntityBoundingBox().minY, entityTo.posZ, range);
    }

    public PathEntity func_186336_a(IBlockAccess world, EntityLiving entityFrom, BlockPos posTo, float range) {
        return this.getPathTo(world, entityFrom, (double) ((float) posTo.getX() + 0.5F), (double) ((float) posTo.getY() + 0.5F), (double) ((float) posTo.getZ() + 0.5F), range);
    }

    private PathEntity getPathTo(IBlockAccess world, EntityLiving entity, double x, double y, double z, float range) {
        this.path.clearPath();
        this.nodeProcessor.func_186315_a(world, entity);
        PathPoint pathpoint = this.nodeProcessor.func_186318_b();
        PathPoint pathpoint1 = this.nodeProcessor.func_186325_a(x, y, z);
        PathEntity pathentity = this.func_186335_a(pathpoint, pathpoint1, range);
        this.nodeProcessor.postProcess();
        return pathentity;
    }

    private PathEntity func_186335_a(PathPoint pointFrom, PathPoint pointTo, float range) {
        pointFrom.totalPathDistance = 0.0F;
        pointFrom.distanceToNext = pointFrom.func_186281_c(pointTo);
        pointFrom.distanceToTarget = pointFrom.distanceToNext;
        this.path.clearPath();
        this.path.addPoint(pointFrom);
        PathPoint pathPoint1 = pointFrom;
        int i = 0;
        while (!this.path.isPathEmpty()) {
            ++i;
            if (i >= 2000) {
                break;
            }
            PathPoint pathPoint = this.path.dequeue();
            if (pathPoint.equals(pointTo)) {
                pathPoint1 = pointTo;
                break;
            }
            if (pathPoint.func_186281_c(pointTo) < pathPoint1.func_186281_c(pointTo)) {
                pathPoint1 = pathPoint;
            }
            pathPoint.visited = true;
            int j = this.nodeProcessor.func_186320_a(this.pathOptions, pathPoint, pointTo, range);
            for (int k = 0; k < j; ++k) {
                PathPoint pathPoint2 = this.pathOptions[k];
                float f = pathPoint.func_186281_c(pathPoint2);
                pathPoint2.field_186284_j = pathPoint.field_186284_j + f;
                pathPoint2.field_186285_k = f + pathPoint2.field_186286_l;
                float f1 = pathPoint.totalPathDistance + pathPoint2.field_186285_k;
                if (pathPoint2.field_186284_j < range && (!pathPoint2.isAssigned() || f1 < pathPoint2.totalPathDistance)) {
                    pathPoint2.previous = pathPoint;
                    pathPoint2.totalPathDistance = f1;
                    pathPoint2.distanceToNext = pathPoint2.func_186281_c(pointTo) + pathPoint2.field_186286_l;
                    if (pathPoint2.isAssigned()) {
                        this.path.changeDistance(pathPoint2, pathPoint2.totalPathDistance + pathPoint2.distanceToNext);
                    } else {
                        pathPoint2.distanceToTarget = pathPoint2.totalPathDistance + pathPoint2.distanceToNext;
                        this.path.addPoint(pathPoint2);
                    }
                }
            }
        }
        if (pathPoint1 == pointFrom) {
            return null;
        }
        else {
            return this.createEntityPath(pointFrom, pathPoint1);
        }
    }

    /**
     * Returns a new PathEntity for a given start and end point
     */
    private PathEntity createEntityPath(PathPoint start, PathPoint end) {
        int i = 1;
        for (PathPoint pathpoint = end; pathpoint.previous != null; pathpoint = pathpoint.previous) {
            ++i;
        }

        PathPoint[] aPathPoint = new PathPoint[i];
        PathPoint pathPoint1 = end;
        --i;
        for (aPathPoint[i] = end; pathPoint1.previous != null; aPathPoint[i] = pathPoint1) {
            pathPoint1 = pathPoint1.previous;
            --i;
        }
        return new PathEntity(aPathPoint);
    }
}
