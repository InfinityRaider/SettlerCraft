package com.InfinityRaider.settlercraft.settlement.settler.profession.builder;

import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.settlement.settler.ai.task.TaskWithParentBase;

public class TaskRemoveBlockForBuilding extends TaskWithParentBase<TaskBuildBuilding>  {
        public TaskRemoveBlockForBuilding(TaskBuildBuilding parentTask, ISettler settler) {
            super(parentTask, settler);
        }

        @Override
        public void startTask() {

        }

        @Override
        public void updateTask() {

        }

        @Override
        public void cancelTask() {

        }

        @Override
        public boolean isCompleted() {
            return false;
        }

        @Override
        public void onTaskCompleted() {

        }
}
