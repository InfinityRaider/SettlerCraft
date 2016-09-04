package com.InfinityRaider.settlercraft.settlement.settler.ai.task;

import com.InfinityRaider.settlercraft.api.v1.ITask;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class TaskUseItem<T extends ITask> extends TaskWithParentBase<T> {
    private final int slot;
    private final int priority;
    private final int usageTicks;

    private ItemStack activeStack;
    private ItemStack useStack;

    private int tickCounter;

    public TaskUseItem(T task, int slot, int priority) {
        this(task, slot, priority, 0);
    }

    public TaskUseItem(T task, int slot, int priority, int usageTicks) {
        super(task);
        this.slot = MathHelper.clamp_int(slot, 0, this.getSettler().getSettlerInventory().getSizeInventory());
        this.priority = priority;
        this.usageTicks = usageTicks;
        this.tickCounter = 0;
        this.activeStack = getSettler().getSettlerInventory().getStackInSlot(0);
        this.useStack = getSettler().getSettlerInventory().getStackInSlot(this.slot);
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
        this.getSettler().useRightClick();
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
        if(slot != 0) {
            getSettler().getSettlerInventory().switchStacksInSlots(0, slot);
        }
        resetTimer();
    }

    protected void resetTimer() {
        this.tickCounter = 0;
    }

    @Override
    public void onSettlerInventorySlotChanged(int slot, ItemStack stack) {
        if(this.isInterrupted() || this.isCancelled()) {
            //previous active item should be in slot '0'
            //item to use should be in slot 'slot'
        } else {
            //previous active item is in slot '0'
            //item to use is in slot 'slot'
        }
        /*
        if(slot == 0) {
            if(this.isInterrupted() || this.isCancelled()) {
                if(!this.isSameItem(stack, this.activeStack)) {
                    this.getSettler().cancelTask(this);
                }
            } else {
                if(!this.isSameItem(stack, this.useStack)) {
                    this.getSettler().cancelTask(this);
                }
            }
        } else if(slot == this.slot) {
            if(this.isInterrupted() || this.isCancelled()) {
                if(!this.isSameItem(stack, this.useStack)) {
                    this.getSettler().cancelTask(this);
                }
            } else {
                if(!this.isSameItem(stack, this.activeStack)) {
                    this.getSettler().cancelTask(this);
                }
            }
        }
        */
    }

    protected boolean isSameItem(ItemStack a, ItemStack b) {
        return ItemStack.areItemsEqual(a, b) && ItemStack.areItemStackTagsEqual(a, b);
    }
}
