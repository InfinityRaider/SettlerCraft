package com.InfinityRaider.settlercraft.settlement.settler.profession;

import com.InfinityRaider.settlercraft.api.v1.*;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.util.math.BlockPos;
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

    public EntityAgeable getEntitySettler() {
        return getSettler().getEntityImplementation();
    }

    public ISettlementBuilding getBuilding() {
        return this.building;
    }

    public double getDistanceFromPositionSquared(BlockPos pos) {
        if(pos == null) {
            return -1;
        }
        double dx = (getEntitySettler().posX - (pos.getX() + 0.5D));
        double dy = (getEntitySettler().posY + getEntitySettler().getEyeHeight() - (pos.getY() + 0.5D));
        double dz = (getEntitySettler().posZ - (pos.getZ() + 0.5D));
        return  dx*dx + dy*dy + dz*dz;
    }

    @Override
    public String getTaskDescription() {
        return I18n.translateToLocal("settlercraft.dialogue.task." + name);
    }
}
