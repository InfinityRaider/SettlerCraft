package com.InfinityRaider.settlercraft.settlement.settler.ai.task;

import com.InfinityRaider.settlercraft.api.v1.ISettler;
import net.minecraft.util.math.Vec3d;

public class TaskUseItemOnTarget extends TaskUseItem {
    private final Vec3d target;

    public TaskUseItemOnTarget(String taskName, ISettler settler, Vec3d target, int slot, int priority) {
        this(taskName, settler, target, slot, priority, 0);
    }

    public TaskUseItemOnTarget(String taskName, ISettler settler,  Vec3d target, int slot, int priority, int useTicks) {
        super(taskName, settler, slot, priority, useTicks);
        this.target = target;
    }

    @Override
    public void updateTask() {
        if(isInReach()) {
            super.updateTask();
        } else {
            this.resetTimer();
            this.moveToPosition();
        }
    }

    protected void moveToPosition() {
        if(!getSettler().getLookTarget().equals(this.target)) {
            getSettler().setLookTarget(this.target);
        }
    }

    protected boolean isInReach() {
        return this.getSettler().getEntityImplementation().getDistanceSq(this.target.xCoord, this.target.yCoord, this.target.zCoord) <= 4;
    }
}
