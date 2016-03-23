package com.InfinityRaider.settlercraft.settlement.settler.profession;

import com.InfinityRaider.settlercraft.api.v1.*;
import net.minecraft.util.text.translation.I18n;

public abstract class TaskBase implements ITask {
    private final String name;
    private final ISettlement settlement;
    private final ISettler settler;
    private final ISettlementBuilding building;

    public TaskBase(String taskName, ISettlement settlement, ISettler settler, ISettlementBuilding building) {
        this.name = taskName;
        this.settlement = settlement;
        this.settler = settler;
        this.building = building;
    }

    public ISettlement getSettlement() {
        return this.settlement;
    }

    public ISettler getSettler() {
        return this.settler;
    }

    public ISettlementBuilding getBuilding() {
        return this.building;
    }

    @Override
    public String getTaskDescription() {
        return I18n.translateToLocal("settlercraft.task." + name);
    }
}
