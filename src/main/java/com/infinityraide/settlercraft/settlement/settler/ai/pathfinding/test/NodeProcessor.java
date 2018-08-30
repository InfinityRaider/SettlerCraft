package com.infinityraide.settlercraft.settlement.settler.ai.pathfinding.test;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

public abstract class NodeProcessor
{
    protected IBlockAccess world;
    protected EntityLiving entity;
    protected final IntHashMap<PathPoint> pointMap = new IntHashMap<>();
    protected int entitySizeX;
    protected int entitySizeY;
    protected int entitySizeZ;
    protected boolean canEnterDoors;
    protected boolean canBreakDoors;
    protected boolean canSwim;

    public void setEntity(IBlockAccess world, EntityLiving entity)
    {
        this.world = world;
        this.entity = entity;
        this.pointMap.clearMap();
        this.entitySizeX = MathHelper.floor(entity.width + 1.0F);
        this.entitySizeY = MathHelper.floor(entity.height + 1.0F);
        this.entitySizeZ = MathHelper.floor(entity.width + 1.0F);
    }

    /**
     * This method is called when all nodes have been processed and PathEntity is created.
     * WalkNodeProcessor WalkNodeProcessor uses this to change its field
     */
    public void postProcess()
    {
    }

    /**
     * Returns a mapped point or creates and adds one
     */
    protected PathPoint openPoint(int x, int y, int z) {
        int hash = PathPoint.makeHash(x, y, z);
        PathPoint pathpoint = this.pointMap.lookup(hash);
        if (pathpoint == null) {
            pathpoint = new PathPoint(x, y, z);
            this.pointMap.addKey(hash, pathpoint);
        }
        return pathpoint;
    }

    public abstract PathPoint getStartingPoint();

    public abstract PathPoint getEndPoint(double x, double y, double z);

    public abstract int getPathPointOptions(PathPoint[] p_186320_1_, PathPoint p_186320_2_, PathPoint p_186320_3_, float p_186320_4_);

    public abstract PathNodeType getPathNodeType(IBlockAccess p_186319_1_, int p_186319_2_, int p_186319_3_, int p_186319_4_, EntityLiving p_186319_5_, int p_186319_6_, int p_186319_7_, int p_186319_8_, boolean p_186319_9_, boolean p_186319_10_);

    public void setCanEnterDoors(boolean canEnterDoorsIn)
    {
        this.canEnterDoors = canEnterDoorsIn;
    }

    public void setCanBreakDoors(boolean canBreakDoorsIn)
    {
        this.canBreakDoors = canBreakDoorsIn;
    }

    public void setCanSwim(boolean canSwimIn)
    {
        this.canSwim = canSwimIn;
    }

    public boolean getCanEnterDoors()
    {
        return this.canEnterDoors;
    }

    public boolean getCanBreakDoors()
    {
        return this.canBreakDoors;
    }

    public boolean getCanSwim()
    {
        return this.canSwim;
    }
}