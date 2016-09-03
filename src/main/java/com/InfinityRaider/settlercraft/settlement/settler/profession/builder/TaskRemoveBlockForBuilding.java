package com.InfinityRaider.settlercraft.settlement.settler.profession.builder;

import com.InfinityRaider.settlercraft.settlement.settler.ai.task.TaskWithParentBase;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;

public class TaskRemoveBlockForBuilding extends TaskWithParentBase<TaskBuildBuilding>  {
        private final BlockPos pos;

        public TaskRemoveBlockForBuilding(TaskBuildBuilding parentTask, BlockPos pos) {
            super(parentTask);
            this.pos = pos;
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
            return getSettler().getWorld().getBlockState(this.pos).getMaterial() == Material.AIR;
        }

        @Override
        public void onTaskCompleted() {
            this.getParentTask().onSubTaskCompleted();
        }
}
