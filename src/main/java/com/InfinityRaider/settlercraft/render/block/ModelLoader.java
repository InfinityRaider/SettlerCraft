package com.InfinityRaider.settlercraft.render.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

public class ModelLoader implements ICustomModelLoader {
    private static final ModelLoader INSTANCE = new ModelLoader();

    public static ModelLoader getInstance() {
        return INSTANCE;
    }

    private final Map<Block, RenderBlock<? extends TileEntity>> customBlockRenderers;

    private ModelLoader() {
        customBlockRenderers = new HashMap<>();
    }

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return false;
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws Exception {
        return null;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }

    @SideOnly(Side.CLIENT)
    public void registerCustomBlockRenderer(RenderBlock<? extends TileEntity> renderer) {
        if(renderer != null) {
            customBlockRenderers.put(renderer.getBlock(), renderer);
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onModelBakeEvent(ModelBakeEvent event) {
        for(Map.Entry<Block, RenderBlock<? extends TileEntity>> entry : customBlockRenderers.entrySet()) {
            ModelResourceLocation loc = entry.getValue().getModelResourceLocation();
            IBakedModel before = event.getModelRegistry().getObject(loc);
            event.getModelRegistry().putObject(loc, entry.getValue());
            IBakedModel after = event.getModelRegistry().getObject(loc);
            boolean flag = false;
        }
    }
}
