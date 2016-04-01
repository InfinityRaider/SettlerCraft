package com.InfinityRaider.settlercraft.registry;

import com.InfinityRaider.settlercraft.api.v1.ISettlerCraftBlockRegistry;
import com.InfinityRaider.settlercraft.block.BlockTest;
import com.InfinityRaider.settlercraft.block.ICustomRenderedBlock;
import com.InfinityRaider.settlercraft.item.*;
import com.InfinityRaider.settlercraft.render.block.BlockRendererRegistry;
import com.InfinityRaider.settlercraft.utility.LogHelper;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
    }

    public final CreativeTabs settlerCraftTab;

    public final List<Block> settlerCraftBlocks;

    public Block testBlock;


    public void init() {
        testBlock = new BlockTest();

        LogHelper.debug("Registered blocks:");
        for(Block block : settlerCraftBlocks()) {
            LogHelper.debug(" - "+block.getRegistryName());
        }
    }

    public void initRecipes() {
        settlerCraftBlocks.stream().filter(block -> block instanceof IItemWithRecipe).forEach(block ->
                ((IItemWithRecipe) block).getRecipes().forEach(GameRegistry::addRecipe));
    }

    @SideOnly(Side.CLIENT)
    public void registerRenderers() {
        settlerCraftBlocks.stream().filter(block -> block instanceof ICustomRenderedBlock).forEach(
                block -> BlockRendererRegistry.getInstance().registerCustomBlockRenderer((ICustomRenderedBlock<? extends TileEntity>) block));

        for (ICustomRenderedBlock block : BlockRendererRegistry.getInstance().getRegisteredBlocks()) {
            LogHelper.debug("Registered custom renderer for " + block.getBlockModelResourceLocation());
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
