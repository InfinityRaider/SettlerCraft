package com.infinityraider.settlercraft.settlement.settler.ai.routines;

import com.infinityraider.settlercraft.api.v1.IInventorySettler;
import com.infinityraider.settlercraft.api.v1.ISettler;
import com.infinityraider.settlercraft.api.v1.ITask;
import com.infinityraider.settlercraft.settlement.settler.EntitySettler;
import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class SettlerAIRoutinePerformTask extends SettlerAIRoutine implements IInventorySettler.IListener {
    private final Deque<ITask> tasks;
    private List<ITask> cachedTaskList;
    private boolean startedTask;

    public SettlerAIRoutinePerformTask(EntitySettler settler) {
        super(settler, ISettler.SettlerStatus.PERFORMING_TASK);
        this.tasks = new ArrayDeque<>();
        this.startedTask = false;
        this.updateCachedList();
        settler.getSettlerInventory().registerInventoryListener(this);
    }

    public List<ITask> getTasks() {
        return cachedTaskList;
    }

    public ITask getCurrentTask() {
        return tasks.peekFirst();
    }

    public ITask addTask(ITask task) {
        ITask current = getCurrentTask();
        if(current != null && !current.isInterrupted()) {
            current.onTaskInterrupted(task);
        }
        tasks.push(task);
        updateCachedList();
        return task;
    }

    public ITask queueTask(ITask task) {
        if(tasks.offerLast(task)) {
            updateCachedList();
        }
        return task;
    }

    public ITask cancelTask(ITask task) {
        if(tasks.contains(task)) {
            if(getCurrentTask() == task) {
                startedTask = false;
            }
            task.onTaskCancelled();
            tasks.remove(task);
            updateCachedList();
        }
        return task;
    }

    @Override
    public boolean shouldExecuteRoutine() {
        return getCurrentTask() != null;
    }

    @Override
    public boolean continueExecutingRoutine() {
        return shouldExecuteRoutine();
    }

    @Override
    public void startExecutingRoutine() {
    }

    @Override
    public void interruptRoutine() {
        this.startedTask = false;
        ITask task = this.getCurrentTask();
        if(task != null && !task.isInterrupted()) {
            task.onTaskInterrupted(null);
        }
    }

    @Override
    public void updateRoutine() {
        ITask task = this.getCurrentTask();
        if(!startedTask) {
            startedTask = true;
            if(task.isInterrupted()) {
                task.onTaskResumed();
            } else {
                task.onTaskStarted();
            }
        }
        task.onTaskUpdated();
        if(task.isCompleted()) {
            task.onTaskCompleted();
            tasks.pop();
            updateCachedList();
            this.startedTask = false;
        }
    }

    private void updateCachedList() {
        this.cachedTaskList = ImmutableList.copyOf(tasks);
    }

    @Override
    public void onInventorySlotChange(ISettler settler, int slot, ItemStack stack) {
        for(ITask task : this.getTasks()) {
            task.onSettlerInventorySlotChanged(slot, stack);
        }
    }
}
