package com.infinityraider.settlercraft.settlement.settler.ai.task;

import com.infinityraider.settlercraft.api.v1.ITask;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class TaskUseItem<T extends ITask> extends TaskWithParentBase<T> {
    private int slot;
    private final int priority;
    private final int usageTicks;

    private boolean swapping;
    private ItemStack activeStack;

    private int tickCounter;

    public TaskUseItem(T task, int slot, int priority) {
        this(task, slot, priority, 0);
    }

    public TaskUseItem(T task, int slot, int priority, int usageTicks) {
        super(task);
        this.slot = MathHelper.clamp(slot, 0, this.getSettler().getSettlerInventory().getSizeInventory());
        this.priority = priority;
        this.usageTicks = usageTicks;
        this.tickCounter = 0;
        this.activeStack = getSettler().getSettlerInventory().getStackInSlot(0);
    }

    @Override
    public int priority() {
        return this.priority;
    }

    @Override
    public void onTaskStarted() {
        this.swapAndReset();
    }

    @Override
    public void onTaskUpdated() {
        //this.getSettler().useRightClick();
        //TODO
        this.tickCounter++;
    }

    @Override
    public void onTaskCancel() {
        this.swapAndReset();
    }

    @Override
    public boolean isCompleted() {
        return this.tickCounter > usageTicks;
    }

    @Override
    public void onTaskCompleted() {
        this.swapAndReset();
    }

    @Override
    protected void onTaskInterrupt(ITask interrupt) {
        this.swapAndReset();
    }

    @Override
    protected void onTaskResume() {
        this.swapAndReset();
    }

    private void swapAndReset() {
        this.swapping = true;
        if(slot != 0) {
            getSettler().getSettlerInventory().switchStacksInSlots(0, slot);
        }
        this.swapping = false;
        resetTimer();
    }

    protected void resetTimer() {
        this.tickCounter = 0;
    }

    @Override
    public void onSettlerInventorySlotChanged(int slot, ItemStack stack) {
        if(!this.swapping) {
            //Potentially not thread safe, find something better
            return;
        }
        if(this.isInterrupted() || this.isCancelled()) {
            //previous active item should be in slot '0'
            //item to use should be in slot 'slot'
            if(slot == this.slot) {
                if(!isSameItem(stack, this.activeStack)) {
                    int index = this.getSettler().getSettlerInventory().getSlotForStack(this.activeStack);
                    if(index >= 0) {
                        this.slot = index;
                    } else {
                        this.getSettler().cancelTask(this);
                    }
                }
            }
        } else {
            //previous active item is in slot '0'
            //item to use is in slot 'slot'
            if(slot == 0) {
                if(!isSameItem(stack, this.activeStack)) {
                    int index = this.getSettler().getSettlerInventory().getSlotForStack(this.activeStack);
                    if(index >= 0) {
                        this.slot = index;
                        this.swapAndReset();
                    } else {
                        this.getSettler().cancelTask(this);
                    }
                }
            }
        }
    }

    protected boolean isSameItem(ItemStack a, ItemStack b) {
        return ItemStack.areItemsEqual(a, b) && ItemStack.areItemStackTagsEqual(a, b);
    }
}
