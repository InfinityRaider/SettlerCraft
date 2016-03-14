package com.InfinityRaider.settlercraft.api.v1;

import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public interface ITask {
    void startTask();

    void updateTask();

    void cancelTask();

    boolean completed();

    void onInterrupt();

    String getTaskDescription();

    List<IDialogueOption> getTaskSpecificDialogueOptions(EntityPlayer player);
}
