package com.InfinityRaider.settlercraft.settlement.settler.ai;

import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.api.v1.ITask;
import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;

public abstract class SettlerAIRoutine {
    private final EntitySettler settler;
    private final ISettler.SettlerStatus status;

    protected SettlerAIRoutine(EntitySettler settler, ISettler.SettlerStatus status) {
        this.settler = settler;
        this.status = status;
    }

    public final EntitySettler getSettler() {
        return settler;
    }

    public final ISettler.SettlerStatus getStatus() {
        return status;
    }

    public abstract ITask getActiveTask();

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public abstract boolean shouldExecuteRoutine();

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public abstract boolean continueExecutingRoutine();

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public abstract void startExecutingRoutine();

    /**
     * Resets the task
     */
    public abstract void resetRoutine();

    /**
     * Updates the task
     */
    public abstract void updateRoutine();
}
