package com.InfinityRaider.settlercraft.settlement.settler.ai.routines;

import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.api.v1.ITask;
import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import com.google.common.collect.ImmutableList;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

public class SettlerAIRoutinePerformTask extends SettlerAIRoutine {
    private final Deque<ITask> tasks;
    private List<ITask> cachedTaskList;
    private boolean startedTask;

    public SettlerAIRoutinePerformTask(EntitySettler settler) {
        super(settler, ISettler.SettlerStatus.PERFORMING_TASK);
        this.tasks = new ArrayDeque<>();
        this.startedTask = false;
        this.updateCachedList();
    }

    public List<ITask> getTasks() {
        return cachedTaskList;
    }

    public ITask getCurrentTask() {
        return tasks.peekFirst();
    }

    public ITask addTask(ITask task) {
        getCurrentTask().interruptTask();
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
            task.cancelTask();
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
    public void resetRoutine() {
        this.startedTask = false;
        Iterator<ITask> iterator = tasks.iterator();
        while(iterator.hasNext()) {
            iterator.next().cancelTask();
            iterator.remove();
        }
    }

    @Override
    public void updateRoutine() {
        ITask task = this.getCurrentTask();
        if(!startedTask) {
            startedTask = true;
            if(task.isInterrupted()) {
                task.resumeTask();
            } else {
                task.startTask();
            }
        }
        task.updateTask();
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
}
