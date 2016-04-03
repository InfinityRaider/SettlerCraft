package com.InfinityRaider.settlercraft.settlement.settler.ai;

import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;

public class SettlerAIRoutineGoToBed extends SettlerAIRoutine {

    protected SettlerAIRoutineGoToBed(EntitySettler settler) {
        super(settler, ISettler.SettlerStatus.GOING_TO_BED);
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
