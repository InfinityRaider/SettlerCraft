package com.InfinityRaider.settlercraft.settlement.settler.ai.task;

import com.InfinityRaider.settlercraft.api.v1.ITask;
import net.minecraft.util.math.Vec3d;

public class TaskUseItemOnTarget<T extends ITask> extends TaskUseItem<T> {
    private final Vec3d target;
    private ITask moveTask;

    public TaskUseItemOnTarget(T task, Vec3d target, int slot, int priority) {
        this(task, target, slot, priority, 0);
    }

    public TaskUseItemOnTarget(T task,  Vec3d target, int slot, int priority, int useTicks) {
        super(task, slot, priority, useTicks);
        this.target = target;
    }

    @Override
    public void onTaskUpdated() {
        if(!this.isInReach()) {
            this.resetTimer();
            this.moveToPosition();
        } else {
            if(!this.isLookingAtTarget()) {
                this.resetTimer();
                this.lookAtTarget();
            } else {
                super.onTaskUpdated();
            }
        }
    }

    protected boolean isInReach() {
        double reach = this.getSettler().getInteractionRangeAttribute().getAttributeValue();
        return this.getSettler().getEntityImplementation().getDistanceSq(this.target.xCoord, this.target.yCoord, this.target.zCoord) <= reach * reach;
    }

    protected void moveToPosition() {
        if(this.moveTask == null || this.moveTask.isCancelled()) {
            this.moveTask = new TaskMoveToPosition<>(this, this.target);
            this.getSettler().assignTask(this.moveTask);
        }
    }

    protected void lookAtTarget() {
        getSettler().setLookTarget(this.target);
    }

    protected boolean isLookingAtTarget() {
        return this.target.equals(getSettler().getLookTarget());
    }

    protected void cancelSubTask() {
        if(this.moveTask != null) {
            if(!this.moveTask.isCancelled()) {
                this.getSettler().cancelTask(this.moveTask);
            }
            this.moveTask = null;
        }
    }

    @Override
    public void onTaskCompleted() {
        this.cancelSubTask();
    }

    @Override
    public void onTaskCancel() {
        this.cancelSubTask();
    }
}
