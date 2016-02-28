package com.InfinityRaider.settlercraft.render.entity;

import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderSettler extends RenderBiped<EntitySettler>  {
    public static IRenderFactory<EntitySettler> getFacotry() {
        return Factory.INSTANCE;
    }

    private RenderSettler(RenderManager renderManager) {
        super(renderManager, new ModelBiped(), 0.5F, 1.0F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntitySettler entity) {
        return entity.profession().getEntityTexture(entity);
    }

    private static class Factory implements IRenderFactory<EntitySettler> {
        private static Factory INSTANCE = new Factory();

        @Override
        public Render<? super EntitySettler> createRenderFor(RenderManager manager) {
            return new RenderSettler(manager);
        }
    }
}
