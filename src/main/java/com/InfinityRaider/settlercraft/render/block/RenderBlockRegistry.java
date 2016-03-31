package com.InfinityRaider.settlercraft.render.block;

import com.InfinityRaider.settlercraft.block.ICustomRenderedBlock;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

public class RenderBlockRegistry implements ICustomModelLoader {
    private static final RenderBlockRegistry INSTANCE = new RenderBlockRegistry();

    public static RenderBlockRegistry getInstance() {
        return INSTANCE;
    }

    private final Map<ResourceLocation, RenderBlockBase<? extends TileEntity>> renderers;

    private RenderBlockRegistry() {
        this.renderers = new HashMap<>();
        ModelLoaderRegistry.registerLoader(this);
    }

    @Override
    public boolean accepts(ResourceLocation loc) {
        if(loc.getResourceDomain().equalsIgnoreCase("settlercraft")) {
            //for breakpoint purposes
            boolean flag = true;
        }
        return renderers.containsKey(loc);
    }

    @Override
    public IModel loadModel(ResourceLocation loc) throws Exception {
        return renderers.get(loc);
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) { }

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
        }
    }

    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) {
        IRegistry<ModelResourceLocation, IBakedModel> registry =  event.getModelRegistry();
        for(Map.Entry<ResourceLocation, RenderBlockBase<? extends TileEntity>> entry : this.renderers.entrySet()) {
            if(entry.getKey() instanceof ModelResourceLocation) {
                IBakedModel model = registry.getObject((ModelResourceLocation) entry.getKey());
                boolean flag = false;
            }
        }
    }
}
