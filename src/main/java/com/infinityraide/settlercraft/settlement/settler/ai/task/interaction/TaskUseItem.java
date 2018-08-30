package com.infinityraide.settlercraft.settlement.settler.ai.task.interaction;

import com.infinityraide.settlercraft.api.v1.ITask;
import com.infinityraide.settlercraft.settlement.settler.ai.task.TaskWithParentBase;
import net.minecraft.item.ItemStack;

public class TaskUseItem<T extends ITask> extends TaskWithParentBase<T> {
    private final int priority;
    private final int usageTicks;

    private int slot;
    private ItemStack stack;

    private int tickCounter;

    public TaskUseItem(T task, ItemStack stack, int priority) {
        this(task, stack, priority, 0);
    }

    public TaskUseItem(T task, ItemStack stack, int priority, int usageTicks) {
        super(task);
        this.stack = stack;
        this.priority = priority;
        this.usageTicks = usageTicks;
        this.tickCounter = 0;
        this.slot = this.getSettler().getSettlerInventory().getSlotForStack(stack);
    }

    @Override
    public int priority() {
        return this.priority;
    }

    @Override
    public void onTaskStarted() {

    }

    @Override
    public void onTaskUpdated() {
        if( ItemStack.areItemsEqual(this.getSettler().getSettlerInventory().getStackInSlot(this.slot), this.stack)) {
            if(this.slot == 0) {
                //this.getSettler().useRightClick(); //TODO
                this.tickCounter++;
            } else {

            }
        } else {
            this.slot = this.getSettler().getSettlerInventory().getSlotForStack(stack);
        }
    }

    @Override
    public void onTaskCompleted() {

    }

    @Override
    public boolean isCompleted() {
        return this.tickCounter > usageTicks;
    }
}
