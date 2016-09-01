package com.InfinityRaider.settlercraft.settlement.settler.ai;

import com.InfinityRaider.settlercraft.api.v1.*;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.ArrayList;
import java.util.List;

public abstract class TaskBase implements ITask {
    private final String name;
    private final ISettlement settlement;
    private final ISettler settler;
    private final ISettlementBuilding building;
    private boolean interrupted;

    public TaskBase(String taskName, ISettlement settlement, ISettler settler, ISettlementBuilding building) {
        this.name = taskName;
        this.settlement = settlement;
        this.settler = settler;
        this.building = building;
        this.interrupted = false;
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
    public final void interruptTask() {
        this.interrupted = true;
        this.onTaskInterrupted();
    }

    protected void onTaskInterrupted() {}

    @Override
    public void resumeTask() {
        this.interrupted = false;
        this.onTaskResumed();
    }

    protected void onTaskResumed() {}

    @Override
    public boolean isInterrupted() {
        return interrupted;
    }

    @Override
    public List<ITextComponent> getTaskDescription() {
        List<ITextComponent> text = new ArrayList<>();
        text.add(new TextComponentTranslation("settlercraft.dialogue.task." + name));
        return text;
    }
}
