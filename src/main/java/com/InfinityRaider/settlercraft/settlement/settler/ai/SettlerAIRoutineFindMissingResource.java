package com.InfinityRaider.settlercraft.settlement.settler.ai;

import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;

public class SettlerAIRoutineFindMissingResource extends SettlerAIRoutine {
    protected SettlerAIRoutineFindMissingResource(EntitySettler settler) {
        super(settler, ISettler.SettlerStatus.FINDING_RESOURCE);
    }

    @Override
    public boolean shouldExecuteRoutine() {
        return getSettler().getMissingResource() != null;
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
}
