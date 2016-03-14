package com.InfinityRaider.settlercraft.settlement.settler.ai;

import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import net.minecraft.entity.ai.EntityAIBase;

public abstract class EntityAISettler extends EntityAIBase {
    private final EntitySettler settler;

    public EntityAISettler(EntitySettler settler) {
        super();
        this.settler = settler;
    }

    public EntitySettler getSettler() {
        return settler;
    }

    @Override
    public final boolean continueExecuting() {
        boolean result = this.continueExecutingRoutine();
        if(result) {
            getSettler().setSettlerStatus(getStatusForRoutine());
        }
        return result;
    }

    /**
     * sub-delegated method to enforce correct method call chain
     */
    public boolean continueExecutingRoutine() {
        return super.continueExecuting();
    }

    @Override
    public final void startExecuting() {
        getSettler().setSettlerStatus(getStatusForRoutine());
        startExecutingRoutine();
    }

    /**
     * sub-delegated method to enforce correct method call chain
     */
    public abstract void startExecutingRoutine();

    @Override
    public final boolean shouldExecute() {
        boolean result = this.shouldExecuteRoutine();
        if(result) {
            getSettler().setSettlerStatus(getStatusForRoutine());
        }
        return result;
    }

    /**
     * sub-delegated method to enforce correct method call chain
     */
    public abstract boolean shouldExecuteRoutine();

    public abstract ISettler.SettlerStatus getStatusForRoutine();
}
