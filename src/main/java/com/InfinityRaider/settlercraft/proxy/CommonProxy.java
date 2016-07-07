package com.InfinityRaider.settlercraft.proxy;

import com.InfinityRaider.settlercraft.SettlerCraft;
import com.InfinityRaider.settlercraft.apiimpl.APISelector;
import com.InfinityRaider.settlercraft.handler.*;
import com.InfinityRaider.settlercraft.network.NetWorkWrapper;
import com.InfinityRaider.settlercraft.registry.BlockRegistry;
import com.InfinityRaider.settlercraft.registry.EntityRegistry;
import com.InfinityRaider.settlercraft.registry.ItemRegistry;
import com.InfinityRaider.settlercraft.settlement.SettlementHandler;
import com.InfinityRaider.settlercraft.settlement.building.BuildingTypeRegistry;
import com.InfinityRaider.settlercraft.settlement.settler.profession.ProfessionRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@SuppressWarnings("unused")
public abstract class CommonProxy implements IProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        this.initConfiguration(event);
        this.registerEventHandlers();
        NetworkRegistry.INSTANCE.registerGuiHandler(SettlerCraft.instance, GuiHandlerSettler.getInstance());
        NetWorkWrapper.getInstance().init();
        ItemRegistry.getInstance().init();
        BlockRegistry.getInstance().init();
        ProfessionRegistry.getInstance();
        APISelector.init();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        this.registerRenderers();
        EntityRegistry.getInstance().init();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        ItemRegistry.getInstance().initRecipes();
        BlockRegistry.getInstance().initRecipes();
        BuildingTypeRegistry.getInstance().postInit();
    }

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
        MinecraftForge.EVENT_BUS.register(BlockEventHandler.getInstance());
        MinecraftForge.EVENT_BUS.register(PlayerTickHandler.getInstance());
        MinecraftForge.EVENT_BUS.register(SettlementHandler.getInstanceServer());
    }

    @Override
    public void registerRenderers() {}
}
