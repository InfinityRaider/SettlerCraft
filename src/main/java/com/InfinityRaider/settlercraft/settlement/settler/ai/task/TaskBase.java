package com.InfinityRaider.settlercraft.settlement.settler.ai.task;

import com.InfinityRaider.settlercraft.api.v1.*;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.ArrayList;
import java.util.List;

public abstract class TaskBase implements ITask {
    private final String name;
    private final ISettler settler;
    private boolean interrupted;

    public TaskBase(String taskName, ISettler settler) {
        this.name = taskName;
        this.settler = settler;
        this.interrupted = false;
    }

    @Override
    public ISettler getSettler() {
        return this.settler;
    }

    public EntityAgeable getEntitySettler() {
        return getSettler().getEntityImplementation();
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
    public final void onTaskInterrupted(ITask interrupt) {
        this.interrupted = true;
        this.onTaskInterrupt(interrupt);
    }

    protected void onTaskInterrupt(ITask interrupt) {}

    @Override
    public void onTaskResumed() {
        this.interrupted = false;
        this.onTaskResume();
    }

    protected void onTaskResume() {}

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

    @Override
    public void onSettlerInventorySlotChanged(int slot, ItemStack stack) {}
}
