package com.InfinityRaider.settlercraft.settlement.settler.ai.task;

import com.InfinityRaider.settlercraft.api.v1.IBuilding;
import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.api.v1.ISettlementBuilding;
import com.InfinityRaider.settlercraft.api.v1.ISettler;

public abstract class TaskBuildingBase<B extends IBuilding> extends TaskSettlementBase {
    private final ISettlementBuilding settlementBuilding;
    private final B building;

    public TaskBuildingBase(String taskName, ISettler settler, ISettlement settlement, ISettlementBuilding settlementBuilding, B building) {
        super(taskName, settler, settlement);
        this.settlementBuilding = settlementBuilding;
        this.building = building;
    }

    public ISettlementBuilding getSettlementBuilding() {
        return this.settlementBuilding;
    }

    public B getBuilding() {
        return this.building;
    }
}
