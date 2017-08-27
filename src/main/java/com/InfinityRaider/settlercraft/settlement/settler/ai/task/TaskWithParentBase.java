package com.InfinityRaider.settlercraft.settlement.settler.ai.task;

import com.InfinityRaider.settlercraft.api.v1.IDialogueOption;
import com.InfinityRaider.settlercraft.api.v1.ITask;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class TaskWithParentBase<T extends ITask> extends TaskBase {
    private final T parentTask;

    public TaskWithParentBase(T parentTask) {
        super(parentTask.toString(), parentTask.getSettler());
        this.parentTask = parentTask;
    }

    protected T getParentTask() {
        return parentTask;
    }

    @Override
    public int priority() {
        return this.getParentTask().priority();
    }

    @Override
    public List<IDialogueOption> getTaskSpecificDialogueOptions(EntityPlayer player) {
        List<IDialogueOption> list = new ArrayList<>();
        list.addAll(getParentTask().getTaskSpecificDialogueOptions(player));
        list.addAll(this.getChildTaskDialogueOptions(player));
        return list;
    }

    protected List<IDialogueOption> getChildTaskDialogueOptions(EntityPlayer player) {
        return Collections.emptyList();
    }

    @Override
    public List<ITextComponent> getTaskDescription() {
        List<ITextComponent> list = new ArrayList<>();
        list.addAll(getParentTask().getTaskDescription());
        list.addAll(this.getChildTaskDescription());
        return list;
    }

    protected List<ITextComponent> getChildTaskDescription() {
        return Collections.emptyList();
    }
}
