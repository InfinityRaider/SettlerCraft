package com.InfinityRaider.settlercraft.settlement.settler.ai;

import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAISettler extends EntityAIBase {
    public final SettlerAIRoutine routineIdle;
    public final SettlerAIRoutine routineFollowPlayer;
    public final SettlerAIRoutine routineFindResource;
    public final SettlerAIRoutine routinePerformTasks;
    public final SettlerAIRoutine routineGetFood;
    public final SettlerAIRoutine routineGoToBed;

    private final SettlerAIRoutine[] routines;
    private int activeRoutine;

    public EntityAISettler(EntitySettler settler) {
        this.routineIdle = new SettlerAIRoutineIdle(settler);
        this.routineFollowPlayer = new SettlerAIRoutineFollowPlayer(settler, 1, 8, 3);
        this.routineFindResource = new SettlerAIRoutineFindMissingResource(settler);
        this.routinePerformTasks = new SettlerAIRoutinePerformTask(settler);
        this.routineGetFood = new SettlerAIRoutineGetFood(settler);
        this.routineGoToBed = new SettlerAIRoutineGoToBed(settler);
        this.routines = new SettlerAIRoutine[] {
                routineFollowPlayer,
                routineGoToBed,
                routineGetFood,
                routineFindResource,
                routinePerformTasks,
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
        SettlerAIRoutine activeRoutine = getActiveRoutine();
        return activeRoutine.continueExecutingRoutine();
    }

    /**
     * Determine if this AI Task is interruptable by a higher (= lower value) priority task. All vanilla AITask have
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
