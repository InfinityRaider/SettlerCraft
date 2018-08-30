package com.infinityraider.settlercraft.settlement.settler.ai.task;

import com.infinityraider.settlercraft.api.v1.ISettler;
import com.infinityraider.settlercraft.api.v1.ITask;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.ArrayList;
import java.util.List;

public abstract class TaskBase implements ITask {
    private final String name;
    private final ISettler settler;
    private boolean interrupted;
    private boolean cancelled;

    public TaskBase(String taskName, ISettler settler) {
        this.name = taskName;
        this.settler = settler;
        this.interrupted = false;
        this.cancelled = false;
    }

    @Override
    public ISettler getSettler() {
        return this.settler;
    }

    public EntityAgeable getEntitySettler() {
        return getSettler().getEntityImplementation();
    }

    @Override
    public final void onTaskCancelled() {
        this.cancelled = true;
        this.onTaskCancel();
    }

    protected void onTaskCancel() {}

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
        return this.interrupted;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
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
