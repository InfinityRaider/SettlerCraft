package com.infinityraide.settlercraft.render.entity;

import com.infinityraide.settlercraft.settlement.settler.EntitySettler;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.util.ResourceLocation;

public class RenderSettler extends RenderLivingBase<EntitySettler> {
    public RenderSettler(RenderManager renderManager) {
        super(renderManager, new ModelBiped(), 0.5F);
        this.addLayer(new LayerBipedArmor(this));
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerArrow(this));
    }

    @Override
    protected ResourceLocation getEntityTexture(EntitySettler entity) {
        return entity.profession().getEntityTexture(entity);
    }

    @Override
    protected boolean canRenderName(EntitySettler settler) {
        return false;
    }
}
