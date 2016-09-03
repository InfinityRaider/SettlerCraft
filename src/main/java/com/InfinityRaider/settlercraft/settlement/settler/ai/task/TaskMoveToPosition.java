package com.InfinityRaider.settlercraft.settlement.settler.ai.task;

import com.InfinityRaider.settlercraft.api.v1.ITask;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.Vec3d;

public class TaskMoveToPosition<T extends ITask> extends TaskWithParentBase<T> {
    private final Vec3d target;
    private final PathNavigate petPathfinder;

    private float waterPriority;
    private int pathingTimer;

    public TaskMoveToPosition(T parentTask, Vec3d position) {
        super(parentTask);
        this.target = position;
        this.petPathfinder = this.getEntitySettler().getNavigator();
    }

    @Override
    public void onTaskStarted() {
        this.setSettlerPathingProperties();
    }

    @Override
    public void onTaskUpdated() {
        if(this.target == null || this.getEntitySettler().getLeashed()) {
            return;
        }
        if (--this.pathingTimer <= 0) {
            this.pathingTimer = 10;
            if (!this.getEntitySettler().getLeashed()) {
                double moveSpeed = getSettler().getEntityImplementation().getAIMoveSpeed();
                this.petPathfinder.tryMoveToXYZ(this.target.xCoord, this.target.yCoord, this.target.zCoord, moveSpeed);
            }
        }
    }

    protected void onTaskInterrupt(ITask interrupt) {
        this.resetSettlerPathingProperties();
    }

    protected void onTaskResume() {
        this.setSettlerPathingProperties();
    }

    @Override
    public void onTaskCancel() {
        this.resetSettlerPathingProperties();
    }

    protected void setSettlerPathingProperties() {
        this.pathingTimer = 0;
        this.waterPriority = getEntitySettler().getPathPriority(PathNodeType.WATER);
        this.getSettler().getEntityImplementation().setPathPriority(PathNodeType.WATER, 0.0F);
    }

    protected void resetSettlerPathingProperties() {
        this.petPathfinder.clearPathEntity();
        this.getEntitySettler().setPathPriority(PathNodeType.WATER, this.waterPriority);
    }

    @Override
    public boolean isCompleted() {
        double reach = this.getSettler().getInteractionRangeAttribute().getAttributeValue();
        return this.target == null ||
                this.getEntitySettler().getDistanceSq(this.target.xCoord, this.target.yCoord, this.target.zCoord) <= reach * reach;
    }

    @Override
    public void onTaskCompleted() {
        this.resetSettlerPathingProperties();
    }
}
