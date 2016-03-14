package com.InfinityRaider.settlercraft.api.v1;

import net.minecraft.entity.player.EntityPlayer;

/**
 * This class can be used if you want to add some default SettlerCraft dialogue options as answers to your own dialogue option.
 * Instance of this class can be retrieved via api.getDialogueOptionCreator()
 */
public interface IDialogueOptionCreator {
    /**
     * Gets a default dialogue option for the player requesting the name of the settler
     * @param player the player
     * @param settler the settler
     * @param previous the current dialogue option (the conversation returns to this after asking the name)
     * @return the dialogue option
     */
    IDialogueOption dialogueOptionRequestName(EntityPlayer player, ISettler settler, IDialogueOption previous);

    /**
     * Gets a default dialogue option to end the conversation
     * @param player the player
     * @param settler the settler
     * @return the dialogue option
     */
    IDialogueOption dialogueOptionEndConversation(EntityPlayer player, ISettler settler);

    /**
     * Gets a default dialogue option to show the settler's inventory to the player
     * @param player the player
     * @param settler the settler
     * @return the dialogue option
     */
    IDialogueOption dialogueOptionShowInventory(EntityPlayer player, ISettler settler);

    /**
     * Gets a default dialogue option to describe the settler's current task and its status
     * @param player the player
     * @param settler the settler
     * @param previous the current dialogue option (the conversation returns to this after asking the name)
     * @return the dialogue option
     */
    IDialogueOption dialogueOptionDescribeTask(EntityPlayer player, ISettler settler, IDialogueOption previous);
}
