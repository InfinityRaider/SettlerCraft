package com.infinityraider.settlercraft.settlement.settler.ai.pathfinding.test;

import net.minecraft.network.PacketBuffer;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PathPoint {
    /** The x coordinate of this point */
    public final int xCoord;
    /** The y coordinate of this point */
    public final int yCoord;
    /** The z coordinate of this point */
    public final int zCoord;
    /** A hash of the coordinates used to identify this point */
    private final int hash;
    /** The index of this point in its assigned path */
    public int index = -1;
    /** The distance along the path to this point */
    public float totalPathDistance;
    /** The linear distance to the next point */
    public float distanceToNext;
    /** The distance to the target */
    public float distanceToTarget;
    /** The point preceding this in its assigned path */
    public PathPoint previous;
    /** True if the pathfinder has already visited this point */
    public boolean visited;
    public float field_186284_j = 0.0F;
    public float field_186285_k = 0.0F;
    public float priority = 0.0F;
    public PathNodeType type = PathNodeType.BLOCKED;

    public PathPoint(int x, int y, int z) {
        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
        this.hash = makeHash(x, y, z);
    }

    public PathPoint copyToNewPathPointWithCoordinates(int x, int y, int z) {
        PathPoint pathpoint = new PathPoint(x, y, z);
        pathpoint.index = this.index;
        pathpoint.totalPathDistance = this.totalPathDistance;
        pathpoint.distanceToNext = this.distanceToNext;
        pathpoint.distanceToTarget = this.distanceToTarget;
        pathpoint.previous = this.previous;
        pathpoint.visited = this.visited;
        pathpoint.field_186284_j = this.field_186284_j;
        pathpoint.field_186285_k = this.field_186285_k;
        pathpoint.priority = this.priority;
        pathpoint.type = this.type;
        return pathpoint;
    }

    public static int makeHash(int x, int y, int z)
    {
        return y & 255 | (x & 32767) << 8 | (z & 32767) << 24 | (x < 0 ? Integer.MIN_VALUE : 0) | (z < 0 ? 32768 : 0);
    }

    /**
     * Returns the linear distance to another path point
     */
    public float distanceTo(PathPoint pathpointIn)
    {
        float f = (float)(pathpointIn.xCoord - this.xCoord);
        float f1 = (float)(pathpointIn.yCoord - this.yCoord);
        float f2 = (float)(pathpointIn.zCoord - this.zCoord);
        return MathHelper.sqrt(f * f + f1 * f1 + f2 * f2);
    }

    /**
     * Returns the squared distance to another path point
     */
    public float squaredDistanceTo(PathPoint point) {
        float dx = (float)(point.xCoord - this.xCoord);
        float dy = (float)(point.yCoord - this.yCoord);
        float dz = (float)(point.zCoord - this.zCoord);
        return dx * dx + dy * dy + dz * dz;
    }

    /** Calculates manhattan distance */
    public float manhattanDistanceToPoint(PathPoint point) {
        float dx = (float)Math.abs(point.xCoord - this.xCoord);
        float dy = (float)Math.abs(point.yCoord - this.yCoord);
        float dz = (float)Math.abs(point.zCoord - this.zCoord);
        return dx + dy + dz;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof PathPoint)) {
            return false;
        } else {
            PathPoint pathpoint = (PathPoint)obj;
            return this.hash == pathpoint.hash && this.xCoord == pathpoint.xCoord && this.yCoord == pathpoint.yCoord && this.zCoord == pathpoint.zCoord;
        }
    }

    public int hashCode() {
        return this.hash;
    }

    /**
     * Returns true if this point has already been assigned to a path
     */
    public boolean isAssigned()
    {
        return this.index >= 0;
    }

    public String toString()
    {
        return this.xCoord + ", " + this.yCoord + ", " + this.zCoord;
    }

    @SideOnly(Side.CLIENT)
    public static PathPoint func_186282_b(PacketBuffer buf) {
        PathPoint pathpoint = new PathPoint(buf.readInt(), buf.readInt(), buf.readInt());
        pathpoint.field_186284_j = buf.readFloat();
        pathpoint.field_186285_k = buf.readFloat();
        pathpoint.priority = buf.readFloat();
        pathpoint.visited = buf.readBoolean();
        pathpoint.type = PathNodeType.values()[buf.readInt()];
        pathpoint.distanceToTarget = buf.readFloat();
        return pathpoint;
    }
}
