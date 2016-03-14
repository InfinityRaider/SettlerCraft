package com.InfinityRaider.settlercraft.settlement.settler.ai;

import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.api.v1.ITask;
import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;

import java.util.ArrayList;
import java.util.List;

public class EntityAIPerformTasks extends EntityAISettler {
    private List<ITask> tasks;

    public EntityAIPerformTasks(EntitySettler settler) {
        super(settler);
        this.tasks = new ArrayList<>();
    }

    @Override
    public ISettler.SettlerStatus getStatusForRoutine() {
        return ISettler.SettlerStatus.PERFORMING_TASK;
    }

    @Override
    public void startExecutingRoutine() {}

    public ITask currentTask() {
        return tasks.size() > 0 ? tasks.get(0) : null;
    }

    public List<ITask> getTasks() {
        return tasks;
    }

    @Override
    public boolean shouldExecuteRoutine() {
        return tasks.size() > 0;
    }
}
