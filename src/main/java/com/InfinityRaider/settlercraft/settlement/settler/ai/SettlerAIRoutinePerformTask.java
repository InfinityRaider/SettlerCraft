package com.InfinityRaider.settlercraft.settlement.settler.ai;

import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.api.v1.ITask;
import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;

public class SettlerAIRoutinePerformTask extends SettlerAIRoutine {
    protected SettlerAIRoutinePerformTask(EntitySettler settler) {
        super(settler, ISettler.SettlerStatus.PERFORMING_TASK);
    }

    @Override
    public boolean shouldExecuteRoutine() {
        return getSettler().getCurrentTask() != null;
    }

    @Override
    public boolean continueExecutingRoutine() {
        return getSettler().getMissingResource() == null && shouldExecuteRoutine();
    }

    @Override
    public void startExecutingRoutine() {
        this.getSettler().getCurrentTask().startTask();
    }

    @Override
    public void resetRoutine() {
        ITask task = getSettler().getCurrentTask();
        if(task != null) {
            this.getSettler().getCurrentTask().resetTask();
        }
    }

    @Override
    public void updateRoutine() {
        this.getSettler().getCurrentTask().updateTask();
        if(this.getSettler().getCurrentTask().completed()) {
            this.getSettler().assignTask(null);
        }
    }
}
