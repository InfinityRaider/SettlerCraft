package com.infinityraider.settlercraft.settlement.settler.profession.builder;

import com.infinityraider.settlercraft.api.v1.IMissingResource;
import com.infinityraider.settlercraft.api.v1.ITask;
import com.infinityraider.settlercraft.settlement.building.StructureBuildPosition;
import com.infinityraider.settlercraft.settlement.settler.MissingResourceStack;
import com.infinityraider.settlercraft.settlement.settler.ai.task.TaskUseItemOnTarget;
import com.infinityraider.settlercraft.settlement.settler.ai.task.TaskWithParentBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public class TaskPlaceBlockForBuilding extends TaskWithParentBase<TaskBuildBuilding> {
    private final StructureBuildPosition buildPosition;
    /** The target the settler has to look at when using the ItemBlock */
    private Vec3d target;
    /** The slot the stack with the ItemBlock is in */
    private int slot = -1;
    /** Sub-task to perform this task */
    private ITask useTask;

    public TaskPlaceBlockForBuilding(TaskBuildBuilding parentTask, StructureBuildPosition buildPosition) {
        super(parentTask);
        this.buildPosition = buildPosition;
        this.target = initTargetPosition();
    }

    @Override
    public void onTaskStarted() {}

    @Override
    public void onTaskUpdated() {
        if(this.slot < 0) {
            this.findRequiredStack();
        } else {
            this.placeBlock();
        }
    }

    protected Vec3d initTargetPosition() {
        //TODO
        BlockPos pos = this.buildPosition.getPos();
        return new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
    }

    protected void findRequiredStack() {
        ItemStack requiredStack = this.buildPosition.getResource();
        if(getSettler().getSettlerInventory().hasStack(requiredStack)) {
            this.slot = this.getSettler().getSettlerInventory().getSlotForStack(requiredStack);
        } else {
            Optional<IMissingResource> current = this.getSettler().getMissingResource();
            if( (!current.isPresent()) || !(current.get().matches(requiredStack)) ) {
                this.getSettler().setMissingResource(new MissingResourceStack(requiredStack));
            }
        }
    }

    protected void placeBlock() {
        if(useTask == null ||this.useTask.isCancelled()) {
            this.useTask = new TaskUseItemOnTarget<>(this, this.target, this.slot, this.priority());
            this.getSettler().assignTask(this.useTask);
        }
    }

    @Override
    public void onTaskCancel() {
        this.cancelSubTask();
        this.getParentTask().onSubTaskCancelled();
    }

    protected void cancelSubTask() {
        if(this.useTask != null) {
            if(!this.useTask.isCancelled()) {
                this.getSettler().cancelTask(this.useTask);
            }
            this.useTask = null;
        }
    }

    @Override
    public boolean isCompleted() {
        return buildPosition.isAllowedState(getSettler().getWorld().getBlockState(buildPosition.getPos()));
    }

    @Override
    public void onTaskCompleted() {
        this.cancelSubTask();
        this.getParentTask().onSubTaskCompleted();
    }

    @Override
    public void onSettlerInventorySlotChanged(int slot, ItemStack stack) {
        ItemStack requiredStack = this.buildPosition.getResource();
        if(this.slot < 0) {
            if (ItemStack.areItemsEqual(stack, requiredStack) && ItemStack.areItemStackTagsEqual(stack, requiredStack)) {
                this.slot = slot;
            }
        } else if(this.slot == slot) {
            if ( (!ItemStack.areItemsEqual(stack, requiredStack)) || (!ItemStack.areItemStackTagsEqual(stack, requiredStack)) ) {
                this.slot = -1;
            }
        }
    }
}
