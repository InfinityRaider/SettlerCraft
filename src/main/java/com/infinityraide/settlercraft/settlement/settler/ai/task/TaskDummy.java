package com.infinityraide.settlercraft.settlement.settler.ai.task;

import com.infinityraide.settlercraft.api.v1.IDialogueOption;
import com.infinityraide.settlercraft.api.v1.ISettler;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class TaskDummy extends TaskBase {
    private final int priority;

    public TaskDummy(int priority, String taskName, ISettler settler) {
        super(taskName, settler);
        this.priority = priority;
    }

    @Override
    public int priority() {
        return this.priority;
    }


    @Override
    public void onTaskStarted() {

    }

    @Override
    public void onTaskUpdated() {

    }

    @Override
    public void onTaskCompleted() {

    }

    @Override
    public boolean isCompleted() {
        return true;
    }

    @Override
    public List<IDialogueOption> getTaskSpecificDialogueOptions(EntityPlayer player) {
        return null;
    }
}
