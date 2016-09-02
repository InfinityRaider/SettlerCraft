package com.InfinityRaider.settlercraft.settlement.settler.ai.task;

import com.InfinityRaider.settlercraft.api.v1.IDialogueOption;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.api.v1.ITask;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;

import java.util.Collections;
import java.util.List;

public class TaskUseItem extends TaskBase {
    private final int slot;
    private final int priority;
    private final int usageTicks;

    private int tickCounter;

    public TaskUseItem(String taskName, ISettler settler, int slot, int priority) {
        this(taskName, settler, slot, priority, 0);
    }

    public TaskUseItem(String taskName, ISettler settler, int slot, int priority, int usageTicks) {
        super(taskName, settler);
        this.slot = MathHelper.clamp_int(slot, 0, settler.getSettlerInventory().getSizeInventory());
        this.priority = priority;
        this.usageTicks = usageTicks;
        this.tickCounter = 0;
    }

    @Override
    public int priority() {
        return this.priority;
    }

    @Override
    public void startTask() {
        this.swapAndReset();
    }

    @Override
    public void updateTask() {
        this.getSettler().useRightClick();
        this.tickCounter++;
    }

    @Override
    public void cancelTask() {
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
    protected void onTaskInterrupted(ITask interrupt) {
        this.swapAndReset();
    }

    @Override
    protected void onTaskResumed() {
        this.swapAndReset();
    }

    @Override
    public List<IDialogueOption> getTaskSpecificDialogueOptions(EntityPlayer player) {
        return Collections.emptyList();
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
}
