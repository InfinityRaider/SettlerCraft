package com.InfinityRaider.settlercraft.settlement.settler.ai;

import com.InfinityRaider.settlercraft.api.v1.ITask;
import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import net.minecraft.entity.ai.EntityAIBase;

import java.util.List;

public class EntityAISettler extends EntityAIBase {
    private final SettlerAIRoutineFollowPlayer routineFollowPlayer;
    private final SettlerAIRoutineGoToBed routineGoToBed;
    private final SettlerAIRoutineGetFood routineGetFood;
    private final SettlerAIRoutineFindMissingResource routineFindResource;
    private final SettlerAIRoutineIdle routineIdle;

    private final SettlerAIRoutine[] routines;
    private final SettlerAIRoutinePerformTask[] taskRoutines;
    private int activeRoutine;

    public EntityAISettler(EntitySettler settler) {
        this.routineFollowPlayer = new SettlerAIRoutineFollowPlayer(settler, 1, 8, 3);
        this.routineGoToBed = new SettlerAIRoutineGoToBed(settler);
        this.routineGetFood = new SettlerAIRoutineGetFood(settler);
        this.routineFindResource = new SettlerAIRoutineFindMissingResource(settler);
        this.routineIdle = new SettlerAIRoutineIdle(settler);
        this.taskRoutines = new SettlerAIRoutinePerformTask[] {
                new SettlerAIRoutinePerformTask(settler),
                new SettlerAIRoutinePerformTask(settler),
                new SettlerAIRoutinePerformTask(settler),
                new SettlerAIRoutinePerformTask(settler),
                new SettlerAIRoutinePerformTask(settler)
        };
        this.routines = new SettlerAIRoutine[] {
                getRoutinePerformTask(0),
                routineFollowPlayer,
                getRoutinePerformTask(1),
                routineGoToBed,
                getRoutinePerformTask(2),
                routineGetFood,
                getRoutinePerformTask(3),
                routineFindResource,
                getRoutinePerformTask(4),
                routineIdle
        };
        this.activeRoutine = routines.length - 1;
        this.setMutexBits(3);
    }

    public EntitySettler getSettler() {
        return getActiveRoutine().getSettler();
    }

    public SettlerAIRoutine getActiveRoutine() {
        return routines[activeRoutine];
    }

    public SettlerAIRoutineFollowPlayer getRoutineFollowPlayer() {
        return routineFollowPlayer;
    }

    public SettlerAIRoutineGoToBed getRoutineGoToBed() {
        return routineGoToBed;
    }

    public SettlerAIRoutineGetFood getRoutineGetFood() {
        return routineGetFood;
    }

    public SettlerAIRoutineFindMissingResource getRoutineFindResource() {
        return routineFindResource;
    }

    public SettlerAIRoutineIdle getRoutineIdle() {
        return routineIdle;
    }

    public SettlerAIRoutinePerformTask getRoutinePerformTask(int priority) {
        if(priority < 0) {
            priority = 0;
        } else if(priority >= taskRoutines.length) {
            priority = taskRoutines.length - 1;
        }
        return taskRoutines[priority];
    }

    public List<ITask> getTasks(int priority) {
        return getRoutinePerformTask(priority).getTasks();
    }

    public ITask getCurrentTask() {
        int priority = activeRoutine - (activeRoutine % 2 == 0 ? 0 : 1);
        return getRoutinePerformTask(priority).getCurrentTask();
    }

    public ITask addTask(ITask task) {
        return getRoutinePerformTask(task.priority()).addTask(task);
    }

    public ITask queueTask(ITask task) {
        return getRoutinePerformTask(task.priority()).queueTask(task);
    }

    public ITask cancelTask(ITask task) {
        return getRoutinePerformTask(task.priority()).cancelTask(task);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        for(int i = 0; i < routines.length; i++) {
            if(routines[i].shouldExecuteRoutine()) {
                activeRoutine = i;
                getSettler().setSettlerStatus(routines[i].getStatus());
                break;
            }
        }
        return true;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting() {
        for(int i = 0; i < activeRoutine; i ++) {
            if(routines[i].shouldExecuteRoutine()) {
                return false;
            }
        }
        SettlerAIRoutine activeRoutine = getActiveRoutine();
        return activeRoutine.continueExecutingRoutine();
    }

    /**
     * Determine if this AI Task is interruptible by a higher (= lower value) priority task. All vanilla AITasks have
     * this value set to true.
     */
    @Override
    public boolean isInterruptible() {
        return true;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting() {
        getActiveRoutine().startExecutingRoutine();
    }

    /**
     * Resets the task
     */
    @Override
    public void resetTask() {
        getActiveRoutine().resetRoutine();
    }

    /**
     * Updates the task
     */
    @Override
    public void updateTask() {
        getActiveRoutine().updateRoutine();
    }
}
