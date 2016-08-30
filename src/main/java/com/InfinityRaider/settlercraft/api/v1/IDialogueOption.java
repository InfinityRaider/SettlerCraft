package com.InfinityRaider.settlercraft.api.v1;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

/**
 * Class used for dialogues between the player and a settler.
 * Method names have the following convention:
 * something the player says to a settler is called a question (even if semantically it isn't a question)
 * and something a settler says to the player is an answer.
 *
 * These options only exist on the server thread, the text components are synced to the client automatically
 */
public interface IDialogueOption {
    /**
     * @return Gets a list of dialogue options when this dialogue option is selected, can be empty, but should never be null
     */
    List<IDialogueOption> getDialogueOptions(EntityPlayer player, ISettler settler);

    /**
     * This method is called when this dialogue option is selected,
     * can be used to perform certain operations or set certain variables.
     * Return true from this method to close the Container and end the dialogue.
     * After the Container is closed the onContainerClosed() method will be called
     * *
     * @param player the player having the dialogue with the settler
     * @param settler the settler having the dialogue with the player
     * @return true if the container should be closed (end of dialogue)
     */
    boolean onDialogueOptionSelected(EntityPlayer player, ISettler settler);

    /**
     * Called when the container for this dialogue is closed, this is called when the previous Container is fully closed,
     * so it is safe to open a new Container from within this method.
     * @param player the player having the dialogue with the settler
     * @param settler the settler having the dialogue with the player
     */
    void onContainerClosed(EntityPlayer player, ISettler settler);

    /**
     * Called last when this dialogue option is selected and the container has been closed.
     * When returning false, the interaction between the settler and the player continues,
     * when returning true, the interaction between the settler and the player ends.
     * An example for when you want the interaction to continue is when you want to open a new container with this settler
     * @return true to end the interaction, false to continue the interaction
     */
    boolean shouldEndInteraction();

    /**
     * @return a list of text components for the answer to this dialogue option (this is what the settler replies to the player selecting this option)
     * every entry in the list is a separate line
     */
    List<ITextComponent> getSettlerText();

    /**
     * @return a list of text components for this dialogue option (this is what a player says to the settler when selecting this option)
     * every entry in the list is a separate line
     */
    List<ITextComponent> getPlayerText();
}
