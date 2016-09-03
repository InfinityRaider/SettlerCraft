package com.InfinityRaider.settlercraft.settlement.settler.profession.builder;

import com.InfinityRaider.settlercraft.api.v1.ITask;
import com.InfinityRaider.settlercraft.settlement.building.StructureBuildPosition;
import com.InfinityRaider.settlercraft.settlement.settler.ai.task.TaskWithParentBase;

public class TaskPlaceBlockForBuilding extends TaskWithParentBase<TaskBuildBuilding> {
    private final StructureBuildPosition buildPosition;

    private ITask blockPlaceTask;

    public TaskPlaceBlockForBuilding(TaskBuildBuilding parentTask, StructureBuildPosition buildPosition) {
        super(parentTask);
        this.buildPosition = buildPosition;
    }

    @Override
    public void onTaskStarted() {
        //TODO
    }

    @Override
    public void onTaskUpdated() {
        //TODO
    }

    @Override
    public void onTaskCancelled() {
        if(this.blockPlaceTask != null) {
            this.getSettler().cancelTask(this.blockPlaceTask);
            this.blockPlaceTask = null;
        }
        this.getParentTask().onSubTaskCancelled();
    }

    @Override
    public boolean isCompleted() {
        return buildPosition.isAllowedState(getSettler().getWorld().getBlockState(buildPosition.getPos()));
    }

    @Override
    public void onTaskCompleted() {
        if(this.blockPlaceTask != null) {
            this.getSettler().cancelTask(this.blockPlaceTask);
            this.blockPlaceTask = null;
        }
        this.getParentTask().onSubTaskCompleted();
    }
}
