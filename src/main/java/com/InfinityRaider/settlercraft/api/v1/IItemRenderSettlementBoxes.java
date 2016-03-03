package com.InfinityRaider.settlercraft.api.v1;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Implement this interface in items which will visualize a settlement to the player when holding a stack with this item
 */
public interface IItemRenderSettlementBoxes {
    /**
     * Checks if the wireframe for the bounding boxes of a settlement should be rendered for the player
     * @param settlement the settlement being rendered
     * @param player the player holding the stack
     * @param stack the stack containing this as an item
     * @return true to draw wireframes for the settlement, false to do nothing
     */
    @SideOnly(Side.CLIENT)
    boolean shouldRenderSettlementBoxes(ISettlement settlement, EntityPlayer player, ItemStack stack);
}
