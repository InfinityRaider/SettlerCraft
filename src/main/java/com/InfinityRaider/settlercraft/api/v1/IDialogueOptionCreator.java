package com.InfinityRaider.settlercraft.api.v1;

import net.minecraft.entity.player.EntityPlayer;

/**
 * This class can be used if you want to add some default SettlerCraft dialogue options as answers to your own dialogue options.
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
     * Gets a default dialogue option to have the settler follow the player.
     * The third argument is used to toggle the follow state of the settler:
     * passing true will make the settler follow that player, false will make the settler stop following.
     * @param player the player to follow
     * @param settler the settler
     * @param doFollow if the settler should follow the player or stop following
     * @return the dialogue option
     */
    IDialogueOption dialogueOptionFollowPlayer(EntityPlayer player, ISettler settler, boolean doFollow);

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

    /**
     * Gets a default dialogue option to assign a workplace to the settler.
     * Only add this as a dialogue option if the settler can actually live at the building,
     * (meaning building.canLiveHere(settler) returns true)
     * @param player the player
     * @param settler the settler
     * @param building the building
     * @return the dialogue option
     */
    IDialogueOption dialogueOptionAssignHouse(EntityPlayer player, ISettler settler, ISettlementBuilding building);

    /**
     * Gets a default dialogue option to assign a workplace to the settler.
     * Only add this as a dialogue option if the settler can actually work at the building,
     * (meaning building.canDoWorkHere(settler) returns true)
     * @param player the player
     * @param settler the settler
     * @param building the building
     * @return the dialogue option
     */
    IDialogueOption dialogueOptionAssignWorkplace(EntityPlayer player, ISettler settler, ISettlementBuilding building);
}
