package com.InfinityRaider.settlercraft.api.v1;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

/**
 * Interface used for settlers to find a needed resource
 */
public interface IMissingResource {
    /**
     * When a settler needs a certain item he will search chests for it.
     * if a stack matches this resource, return true
     *
     * @param stack item stack found by the settler, will never be null
     * @return true if the stack matches this resource
     */
    boolean matches(ItemStack stack);

    /**
     * Called when the settler acquires a stack which matches this requirement
     * Return true if the found stack has fulfilled this requirement.
     *
     * @param settler the settler needing this resource
     * @param slot the slot in the settler's inventory where the newly acquired stack has been inserted
     * @param stack the newly acquired stack
     * @return true if this requirement has been fulfilled and the settler does not have to continue looking for resources
     */
    boolean onResourceAcquired(ISettler settler, int slot, ItemStack stack);

    /**
     * If none of the stored item stacks matches this resource, the settler will request new stacks to be produced,
     * if any of the stacks in this list can be crafted in a building in the settlement, the settler will request a crafting job in the respective building.
     * The matches(ItemStack stack) method must return true for every ItemStack object in this list.
     *
     * @return A list holding default stacks matching this resource, may be empty but not null
     */
    List<ItemStack> getDefaultStacks();

    /**
     * Get a description for this missing resource, used in dialogues to provide feedback to the player
     * Each entry in the list is a new line in the dialogue.
     *
     * @param settler the settler needing this resource
     * @return a list with ITextComponents describing this missing requirement, may be empty but not null
     */
    List<ITextComponent> getDescription(ISettler settler);

    /**
     * Gets custom dialogue options for this resource requirement, used for special interactions,
     * the list may be empty but should never be null.
     * This method is only called on the server thread
     *
     * @param settler the settler looking for this resource
     * @param player the player having the dialogue with the settler
     * @return a list of dialogue options
     */
    List<IDialogueOption> getDialogueOptions(ISettler settler, EntityPlayer player);
}
