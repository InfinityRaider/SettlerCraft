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
     * Gets the priority of the task, this determines what actions this task has priority over.
     * A settler without any tasks will perform the following actions in this order:
     *  priority 0 : follow a player (if the settler has a player to follow)
     *  priority 1: go to bed and sleep (if it is night time)
     *  priority 2: find and/or eat food (if the settler is hungry)
     *  priority 3: find a missing resource (if the settler needs a certain item)
     *  priority 4: idle state (if the settler has nothing else to do)
     *
     *  For example a task with priority 2 will be executed before the settler searches for food,
     *  but will not be executed as long as the settler is following a player or if it is night time.
     *
     *  Tasks related to the settler's profession (which is most tasks)  should have priority 4
     *
     * @return the task priority
     */
    default int priority() {
        return 4;
    }

    /**
     * Called when the settler starts on this task
     */
    void startTask();

    /**
     * Called each tick when the settler is performing this task
     */
    void updateTask();

    /**
     * Called when this task is cancelled, for example when another task is assigned before this task was finished
     */
    void cancelTask();

    /**
     * Called when this task is interrupted, for example to do another task first. Interrupted tasks will be continued later
     */
    void interruptTask();

    /**
     * Called when this task was interrupted but is resumed again
     */
    void resumeTask();

    /**
     * Checks if this task has been interrupted, this should return true in between interruptTask() and resumeTask() calls and false any other time.
     * @return true if this task is currently interrupted
     */
    boolean isInterrupted();

    /**
     * Checks if this task is complete, if the task is complete, the settler will look for a new task
     * @return true if this task is complete, false if not
     */
    boolean isCompleted();

    /**
     * Gets called when the settler completes this task
     */
    void onTaskCompleted();

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
