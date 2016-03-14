package com.InfinityRaider.settlercraft.settlement.settler.ai;

import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;

public class EntityAIGoToBed extends EntityAISettler {
    public EntityAIGoToBed(EntitySettler settler) {
        super(settler);
    }

    @Override
    public ISettler.SettlerStatus getStatusForRoutine() {
        return ISettler.SettlerStatus.GOING_TO_BED;
    }

    @Override
    public void startExecutingRoutine() {}

    @Override
    public boolean shouldExecuteRoutine() {
        return false;
    }
}
