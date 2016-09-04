package com.InfinityRaider.settlercraft.settlement.settler.ai.task;

import com.InfinityRaider.settlercraft.api.v1.IDialogueOption;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class TaskDummy extends TaskBase {
    public TaskDummy(String taskName, ISettler settler) {
        super(taskName, settler);
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
