package com.InfinityRaider.settlercraft.render.entity;

import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderSettler extends RenderBiped<EntitySettler>  {
    public RenderSettler(RenderManager renderManager) {
        super(renderManager, new ModelBiped(), 0.5F, 1.0F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntitySettler entity) {
        return entity.profession().getEntityTexture(entity);
    }
}
