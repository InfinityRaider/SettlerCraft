package com.infinityraide.settlercraft.settlement.settler.ai.pathfinding;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.*;
import net.minecraft.world.World;

public class PathNavigateSettler extends PathNavigateGround {
    public PathNavigateSettler(EntityLiving entity, World world) {
        super(entity, world);
    }

    @Override
    protected PathFinder getPathFinder() {
        this.nodeProcessor = new NodeProcessorSettler();
        return new PathFinderSettler(this.nodeProcessor);
    }
}
