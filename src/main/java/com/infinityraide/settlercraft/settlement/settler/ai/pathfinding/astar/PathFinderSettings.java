package com.infinityraide.settlercraft.settlement.settler.ai.pathfinding.astar;

import net.minecraft.entity.EntityLiving;

public class PathFinderSettings {
    private final EntityLiving entity;

    private boolean canSwim;
    private boolean canClimb;
    private boolean canOpenDoors;

    private int maxFallHeight;

    public PathFinderSettings(EntityLiving entity) {
        this.entity = entity;
        this.canSwim = false;
        this.canClimb = false;
         this.canOpenDoors = false;
        this.maxFallHeight = 1;
    }

    public PathFinderSettings setCanSwim(boolean status) {
        this.canSwim = status;
        return this;
    }

    public PathFinderSettings setCanClimb(boolean status) {
        this.canClimb = status;
        return this;
    }

    public PathFinderSettings setCanOpenDoors(boolean status) {
        this.canOpenDoors = status;
        return this;
    }

    public PathFinderSettings setMaxFallHeight(int height) {
        this.maxFallHeight = height;
        return this;
    }

    public EntityLiving getEntity() {
        return this.entity;
    }

    public boolean canSwim() {
        return this.canSwim;
    }

    public boolean canClimb() {
        return this.canClimb;
    }

    public boolean canOpenDoors() {
        return this.canOpenDoors;
    }

    public int maxFallHeight() {
        return this.maxFallHeight;
    }
}
