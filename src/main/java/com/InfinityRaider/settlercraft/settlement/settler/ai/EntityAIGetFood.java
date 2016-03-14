package com.InfinityRaider.settlercraft.settlement.settler.ai;

import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;

public class EntityAIGetFood extends EntityAISettler {
    public EntityAIGetFood(EntitySettler settler) {
        super(settler);
    }

    @Override
    public void startExecutingRoutine() {}

    @Override
    public ISettler.SettlerStatus getStatusForRoutine() {
        return ISettler.SettlerStatus.GETTING_FOOD;
    }

    @Override
    public boolean shouldExecuteRoutine() {
        return false;
    }

}
