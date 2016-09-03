package com.InfinityRaider.settlercraft.api.v1;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

/**
 * Interface to perform tasks, this is interface is used in a wrapped EntityAI class.
 * It can be used to control the settler's behaviour when he is performing its task.
 *
 * Tasks are only ran on the server thread
 */
public interface ITask {
    /**
     * Gets the priority of the task, this determines what actions this task has priority over.
     * A settler without any tasks will perform the following actions in this order:
     *  priority 0: follow a player (if the settler has a player to follow)
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
     * @return The settler performing this task
     */
    ISettler getSettler();

    /**
     * Called when the settler starts on this task
     */
    void onTaskStarted();

    /**
     * Called each tick when the settler is performing this task
     */
    void onTaskUpdated();

    /**
     * Called when this task is cancelled, a cancelled task will never be resumed again
     */
    void onTaskCancelled();

    /**
     * Called when this task is interrupted, for example to do another task first. Interrupted tasks will be continued later
     * @param interrupt the task which interrupted this task, can be null if this task was interrupt by a different AI routine
     */
    void onTaskInterrupted(ITask interrupt);

    /**
     * Called when this task was interrupted but is resumed again
     */
    void onTaskResumed();

    /**
     * Checks if this task has been interrupted, this should return true in between onTaskInterrupted() and onTaskResumed() calls and false any other time.
     * @return true if this task is currently interrupted
     */
    boolean isInterrupted();

    /**
     * Checks if this task is complete, if the task is complete, the settler will start or resume its next task
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

    /**
     * Called when a slot in the settler's inventory changes
     * @param slot the index of the slot which has changed
     * @param stack the new stack in the slot
     */
    void onSettlerInventorySlotChanged(int slot, ItemStack stack);
}
