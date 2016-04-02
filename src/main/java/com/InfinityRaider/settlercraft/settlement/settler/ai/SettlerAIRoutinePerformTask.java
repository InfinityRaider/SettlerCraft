package com.InfinityRaider.settlercraft.settlement.settler.ai;

import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.api.v1.ITask;
import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;

public class SettlerAIRoutinePerformTask extends SettlerAIRoutine {
    private ITask task;

    protected SettlerAIRoutinePerformTask(EntitySettler settler) {
        super(settler, ISettler.SettlerStatus.PERFORMING_TASK);
    }

    @Override
    public ITask getActiveTask() {
        return task;
    }

    @Override
    public void setTask(ITask task) {
        this.task = task;
    }

    @Override
    public boolean shouldExecuteRoutine() {
        return task != null;
    }

    @Override
    public boolean continueExecutingRoutine() {
        return shouldExecuteRoutine();
    }

    @Override
    public void startExecutingRoutine() {
        this.task.startTask();
    }

    @Override
    public void resetRoutine() {
        this.task.cancelTask();
        this.task.startTask();
    }

    @Override
    public void updateRoutine() {
        this.task.updateTask();
        if(this.task.completed()) {
            this.task = null;
        }
    }
}
