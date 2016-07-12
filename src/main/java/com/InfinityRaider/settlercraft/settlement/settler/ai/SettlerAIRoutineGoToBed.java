package com.InfinityRaider.settlercraft.settlement.settler.ai;

import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import net.minecraft.block.BlockBed;

public class SettlerAIRoutineGoToBed extends SettlerAIRoutine {
    protected SettlerAIRoutineGoToBed(EntitySettler settler) {
        super(settler, ISettler.SettlerStatus.GOING_TO_BED);
    }

    @Override
    public boolean shouldExecuteRoutine() {
        return !getSettler().getWorld().isDaytime();
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
        if(!getSettler().isSleeping()) {
            //TODO: path to bed and go to sleep
        }
    }
}
