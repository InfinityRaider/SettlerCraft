package com.infinityraider.settlercraft.proxy;

import com.infinityraider.settlercraft.SettlerCraft;
import com.infinityraider.settlercraft.api.v1.ISettlementHandler;
import com.infinityraider.settlercraft.apiimpl.APISelector;
import com.infinityraider.settlercraft.handler.GuiHandlerSettler;
import com.infinityraider.settlercraft.settlement.SettlementHandler;
import com.infinityraider.settlercraft.settlement.building.BuildingTypeRegistry;
import com.infinityraider.settlercraft.settlement.settler.profession.ProfessionRegistry;
import com.infinityraider.settlercraft.handler.BlockEventHandler;
import com.infinityraider.settlercraft.handler.ConfigurationHandler;
import com.infinityraider.settlercraft.handler.PlayerTickHandler;
import com.infinityraider.infinitylib.proxy.base.IProxyBase;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public interface IProxy extends IProxyBase {
    @Override
    default void preInitStart(FMLPreInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(SettlerCraft.instance, GuiHandlerSettler.getInstance());
        ProfessionRegistry.getInstance();
        APISelector.init();
    }

    @Override
    default void postInitStart(FMLPostInitializationEvent event) {
        BuildingTypeRegistry.getInstance().postInit();
    }

    @Override
    default void initConfiguration(FMLPreInitializationEvent event) {
        ConfigurationHandler.getInstance().init(event);
    }

    @Override
    default void registerCapabilities() {}

    @Override
    default void registerEventHandlers() {
        registerEventHandler(BlockEventHandler.getInstance());
        registerEventHandler(PlayerTickHandler.getInstance());
        registerEventHandler(SettlementHandler.getInstanceServer());
    }

    @Override
    default void activateRequiredModules() {}

    /**
     * @return The settlement handler relevant to the effective side
     */
    ISettlementHandler getSettlementHandler();
}
