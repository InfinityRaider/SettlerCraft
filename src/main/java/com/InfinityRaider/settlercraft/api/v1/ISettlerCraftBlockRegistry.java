package com.InfinityRaider.settlercraft.api.v1;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;

import java.util.List;

/**
 * Helper interface to access the blocks added to Minecraft by SettlerCraft
 * The instance of this can be retrieved via APIv1.getBlockRegistry()
 */
@SuppressWarnings("unused")
public interface ISettlerCraftBlockRegistry {
    /**
     * @return the Creative Tab for SettlerCraft
     */
    CreativeTabs creativeTabSettlerCraft();

    /**
     * @return a List containing all SettlerCraft blocks
     */
    List<Block> settlerCraftBlocks();
}
