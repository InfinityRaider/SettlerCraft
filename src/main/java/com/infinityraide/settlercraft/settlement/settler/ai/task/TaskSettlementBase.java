package com.infinityraide.settlercraft.settlement.settler.ai.task;

import com.infinityraide.settlercraft.api.v1.ISettlement;
import com.infinityraide.settlercraft.api.v1.ISettler;

public abstract class TaskSettlementBase extends TaskBase {
    private final ISettlement settlement;

    public TaskSettlementBase(String taskName, ISettler settler, ISettlement settlement) {
        super(taskName, settler);
        this.settlement = settlement;
    }

    public ISettlement getSettlement() {
        return this.settlement;
    }
}
