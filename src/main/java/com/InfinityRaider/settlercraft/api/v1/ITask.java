package com.InfinityRaider.settlercraft.api.v1;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

/**
 * Interface to perform tasks, this is interface is used in a wrapped EntityAI class.
 * It can be used to control the settler's behaviour when he is performing its task.
 */
public interface ITask {
    /**
     * Called when the settler starts on this task
     */
    void startTask();

    /**
     * Called each tick when the settler is performing this task
     */
    void updateTask();

    /**
     * Called when the task is reset, for example when the settler interrupts the task to go to sleep or fetch food
     */
    void resetTask();

    /**
     * Called when this task is cancelled, for example when another task is assigned before this task was finished
     */
    void cancelTask();

    /**
     * Checks if this task is complete, if the task is complete, the settler will look for a new task
     * @return true if this task is complete, false if not
     */
    boolean completed();

    /**
     * Gets a list of text components describing this task, used in dialogues.
     * Each entry in the list is a new line
     * @return a task description
     */
    List<ITextComponent> getTaskDescription();

    /**
     * It is possible to have specific dialogue options for the task.
     * This method is called when the player asks the settler about its task.
     * The dialogue options in the returned list can be used to allow for player input.
     *
     * @param player the player currently in conversation with the settler
     * @return a list containing dialogue options specific to this task, can be empty, but should never be null.
     */
    List<IDialogueOption> getTaskSpecificDialogueOptions(EntityPlayer player);
}
