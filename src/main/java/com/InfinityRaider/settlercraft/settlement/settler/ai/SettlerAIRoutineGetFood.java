package com.InfinityRaider.settlercraft.settlement.settler.ai;

import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;

public class SettlerAIRoutineGetFood extends SettlerAIRoutine {
    private boolean hasFood;
    private boolean isFindingFood;

    protected SettlerAIRoutineGetFood(EntitySettler settler) {
        super(settler, ISettler.SettlerStatus.GETTING_FOOD);
        this.checkSettlerForFood();
    }

    @Override
    public boolean shouldExecuteRoutine() {
        return !getSettler().getHungerStatus().shouldHeal();
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

    }

    @Override
    public void updateRoutine() {

    }

    private void checkSettlerForFood() {

    }
}
