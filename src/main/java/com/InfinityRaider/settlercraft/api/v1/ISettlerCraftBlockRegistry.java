package com.InfinityRaider.settlercraft.api.v1;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;

import java.util.List;

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
