package com.InfinityRaider.settlercraft.render.block;

import com.InfinityRaider.settlercraft.block.ICustomRenderedBlock;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenderBlockRegistry implements ICustomModelLoader {
    private static final RenderBlockRegistry INSTANCE = new RenderBlockRegistry();

    public static RenderBlockRegistry getInstance() {
        return INSTANCE;
    }

    private final Map<ResourceLocation, RenderBlockBase<? extends TileEntity>> renderers;
    private final List<ICustomRenderedBlock<? extends TileEntity>> blocks;

    private RenderBlockRegistry() {
        this.renderers = new HashMap<>();
        this.blocks = new ArrayList<>();
        ModelLoaderRegistry.registerLoader(this);
    }

    @Override
    public boolean accepts(ResourceLocation loc) {
        return renderers.containsKey(loc);
    }

    @Override
    public IModel loadModel(ResourceLocation loc) throws Exception {
        return renderers.get(loc);
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {}

    public List<ICustomRenderedBlock<? extends TileEntity>> getRegisteredBlocks() {
        return ImmutableList.copyOf(blocks);
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void registerCustomBlockRenderer(ICustomRenderedBlock block) {
        IBlockRenderingHandler renderer = block.getRenderer();
        if(renderer != null) {
            RenderBlockBase instance = new RenderBlockBase<>(renderer);
            if(renderer.hasStaticRendering()) {
                renderers.put(block.getBlockModelResourceLocation(), instance);
            }
            TileEntity tile = renderer.getTileEntity();
            if(renderer.hasDynamicRendering() && tile != null) {
                ClientRegistry.bindTileEntitySpecialRenderer(tile.getClass(), instance);
            }
            blocks.add(block);
        }
    }
}
