package com.InfinityRaider.settlercraft.proxy;

import com.InfinityRaider.settlercraft.SettlerCraft;
import com.InfinityRaider.settlercraft.apiimpl.APISelector;
import com.InfinityRaider.settlercraft.handler.*;
import com.InfinityRaider.settlercraft.registry.EntityRegistry;
import com.InfinityRaider.settlercraft.settlement.SettlementHandler;
import com.InfinityRaider.settlercraft.settlement.building.BuildingTypeRegistry;
import com.InfinityRaider.settlercraft.settlement.settler.profession.ProfessionRegistry;
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
        ProfessionRegistry.getInstance();
        APISelector.init();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        EntityRegistry.getInstance().init();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        BuildingTypeRegistry.getInstance().postInit();
    }

    @Override
    public void initConfiguration(FMLPreInitializationEvent event) {
        ConfigurationHandler.getInstance().init(event);
    }

    @Override
    public void registerEventHandlers() {
        MinecraftForge.EVENT_BUS.register(SettlerTargetingHandler.getInstance());
        MinecraftForge.EVENT_BUS.register(BlockEventHandler.getInstance());
        MinecraftForge.EVENT_BUS.register(PlayerTickHandler.getInstance());
        MinecraftForge.EVENT_BUS.register(SettlementHandler.getInstanceServer());
    }
}
