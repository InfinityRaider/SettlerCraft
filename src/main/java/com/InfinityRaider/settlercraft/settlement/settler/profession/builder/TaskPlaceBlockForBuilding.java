package com.InfinityRaider.settlercraft.settlement.settler.profession.builder;

import com.InfinityRaider.settlercraft.settlement.building.StructureBuildPosition;
import com.InfinityRaider.settlercraft.settlement.settler.ai.task.TaskWithParentBase;

public class TaskPlaceBlockForBuilding extends TaskWithParentBase<TaskBuildBuilding> {
    private final StructureBuildPosition buildPosition;

    public TaskPlaceBlockForBuilding(TaskBuildBuilding parentTask, StructureBuildPosition buildPosition) {
        super(parentTask);
        this.buildPosition = buildPosition;
    }

    @Override
    public void startTask() {
        //TODO
    }

    @Override
    public void updateTask() {
        //TODO
    }

    @Override
    public void cancelTask() {
        this.getParentTask().onSubTaskCancelled();
    }

    @Override
    public boolean isCompleted() {
        return buildPosition.isAllowedState(getSettler().getWorld().getBlockState(buildPosition.getPos()));
    }

    @Override
    public void onTaskCompleted() {
        this.getParentTask().onSubTaskCompleted();
    }
}
