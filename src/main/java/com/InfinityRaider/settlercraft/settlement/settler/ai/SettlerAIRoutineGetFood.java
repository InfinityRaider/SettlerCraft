package com.InfinityRaider.settlercraft.settlement.settler.ai;

import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.api.v1.ITask;
import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;

public class SettlerAIRoutineGetFood extends SettlerAIRoutine {

    protected SettlerAIRoutineGetFood(EntitySettler settler) {
        super(settler, ISettler.SettlerStatus.GETTING_FOOD);
    }

    @Override
    public ITask getActiveTask() {
        return null;
    }

    @Override
    public boolean shouldExecuteRoutine() {
        return false;
    }

    @Override
    public boolean continueExecutingRoutine() {
        return false;
    }

    @Override
    public void startExecutingRoutine() {

    }

    @Override
    public void resetRoutine() {

    }

    @Override
    public void updateRoutine() {

    }
}
