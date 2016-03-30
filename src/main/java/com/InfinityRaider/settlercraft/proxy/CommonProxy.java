package com.InfinityRaider.settlercraft.proxy;

import com.InfinityRaider.settlercraft.handler.ConfigurationHandler;
import com.InfinityRaider.settlercraft.handler.SettlerTargetingHandler;
import com.InfinityRaider.settlercraft.render.block.ModelLoader;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@SuppressWarnings("unused")
public abstract class CommonProxy implements IProxy {
    @Override
    public void initConfiguration(FMLPreInitializationEvent event) {
        ConfigurationHandler.getInstance().init(event);
    }

    @Override
    public Entity getEntityById(int dimension, int id) {
        return getEntityById(getWorldByDimensionId(dimension), id);
    }

    @Override
    public Entity getEntityById(World world, int id) {
        return world.getEntityByID(id);
    }

    @Override
    public void registerEventHandlers() {
        MinecraftForge.EVENT_BUS.register(SettlerTargetingHandler.getInstance());
    }

    @Override
    public void registerRenderers() {}
}
