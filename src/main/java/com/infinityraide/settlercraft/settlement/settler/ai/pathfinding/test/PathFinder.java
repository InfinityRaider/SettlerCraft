package com.infinityraide.settlercraft.settlement.settler.ai.pathfinding.test;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class PathFinder {
    /** The path being generated */
    private final Path path = new Path();
    /** Selection of path points to add to the path */
    private final PathPoint[] pathOptions = new PathPoint[32];
    private final NodeProcessor nodeProcessor;

    public PathFinder(NodeProcessor nodeProcessor) {
        this.nodeProcessor = nodeProcessor;
    }

    public PathEntity findPathToEntity(IBlockAccess world, EntityLiving from, Entity to, float range) {
        return this.findPath(world, from, to.posX, to.getEntityBoundingBox().minY, to.posZ, range);
    }

    public PathEntity findPathToPos(IBlockAccess world, EntityLiving from, BlockPos to, float range) {
        return this.findPath(world, from, (double) ((float) to.getX() + 0.5F), (double) ((float) to.getY() + 0.5F), (double) ((float) to.getZ() + 0.5F), range);
    }

    private PathEntity findPath(IBlockAccess world, EntityLiving entity, double x, double y, double z, float range) {
        this.path.clearPath();
        this.nodeProcessor.setEntity(world, entity);
        PathPoint start = this.nodeProcessor.getStartingPoint();
        PathPoint stop = this.nodeProcessor.getEndPoint(x, y, z);
        PathEntity pathEntity = this.createPath(start, stop, range);
        this.nodeProcessor.postProcess();
        return pathEntity;
    }

    private PathEntity createPath(PathPoint start, PathPoint stop, float range) {
        start.totalPathDistance = 0.0F;
        start.distanceToNext = start.manhattanDistanceToPoint(stop);
        start.distanceToTarget = start.distanceToNext;
        this.path.clearPath();
        this.path.addPoint(start);
        PathPoint pathpoint = start;
        int i = 0;
        while (!this.path.isPathEmpty()) {
            ++i;
            if (i >= 2000) {
                break;
            }
            PathPoint pathPoint = this.path.dequeue();
            if (pathPoint.equals(stop)) {
                pathpoint = stop;
                break;
            }
            if (pathPoint.manhattanDistanceToPoint(stop) < pathpoint.manhattanDistanceToPoint(stop)) {
                pathpoint = pathPoint;
            }
            pathPoint.visited = true;
            int j = this.nodeProcessor.getPathPointOptions(this.pathOptions, pathPoint, stop, range);
            for (int k = 0; k < j; ++k) {
                PathPoint newPoint = this.pathOptions[k];
                float f = pathPoint.manhattanDistanceToPoint(newPoint);
                newPoint.field_186284_j = pathPoint.field_186284_j + f;
                newPoint.field_186285_k = f + newPoint.priority;
                float f1 = pathPoint.totalPathDistance + newPoint.field_186285_k;
                if (newPoint.field_186284_j < range && (!newPoint.isAssigned() || f1 < newPoint.totalPathDistance)) {
                    newPoint.previous = pathPoint;
                    newPoint.totalPathDistance = f1;
                    newPoint.distanceToNext = newPoint.manhattanDistanceToPoint(stop) + newPoint.priority;
                    if (newPoint.isAssigned()) {
                        this.path.changeDistance(newPoint, newPoint.totalPathDistance + newPoint.distanceToNext);
                    } else {
                        newPoint.distanceToTarget = newPoint.totalPathDistance + newPoint.distanceToNext;
                        this.path.addPoint(newPoint);
                    }
                }
            }
        }
        if (pathpoint == start) {
            return null;
        } else {
            return this.createEntityPath(start, pathpoint);
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
        PathPoint[] pathPoints = new PathPoint[i];
        PathPoint endPoint = end;
        --i;
        for (pathPoints[i] = end; endPoint.previous != null; pathPoints[i] = endPoint) {
            endPoint = endPoint.previous;
            --i;
        }
        return new PathEntity(pathPoints);
    }
}