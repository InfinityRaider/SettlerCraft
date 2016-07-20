package com.InfinityRaider.settlercraft.registry;

import com.InfinityRaider.settlercraft.api.v1.ISettlerCraftBlockRegistry;
import com.InfinityRaider.settlercraft.block.BlockTest;
import com.InfinityRaider.settlercraft.utility.LogHelper;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;

import java.util.ArrayList;
import java.util.List;

public class BlockRegistry implements ISettlerCraftBlockRegistry {
    private static final BlockRegistry INSTANCE = new BlockRegistry();

    public static BlockRegistry getInstance() {
        return INSTANCE;
    }

    private BlockRegistry() {
        settlerCraftTab = ItemRegistry.getInstance().settlerCraftTab;
        settlerCraftBlocks = new ArrayList<>();
        this.init();
    }

    private final CreativeTabs settlerCraftTab;
    private final List<Block> settlerCraftBlocks;

    public Block testBlock;

    private void init() {
        testBlock = new BlockTest().setCreativeTab(creativeTabSettlerCraft());
        settlerCraftBlocks.add(testBlock);

        LogHelper.debug("Registered blocks:");
        for(Block block : settlerCraftBlocks()) {
            LogHelper.debug(" - "+block.getRegistryName());
        }
    }

    @Override
    public CreativeTabs creativeTabSettlerCraft() {
        return settlerCraftTab;
    }

    @Override
    public List<Block> settlerCraftBlocks() {
        return ImmutableList.copyOf(settlerCraftBlocks);
    }
}
