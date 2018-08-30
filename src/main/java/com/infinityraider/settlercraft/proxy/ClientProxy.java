package com.infinityraider.settlercraft.proxy;

import com.infinityraider.settlercraft.api.v1.ISettlementHandler;
import com.infinityraider.settlercraft.handler.ConfigurationHandler;
import com.infinityraider.settlercraft.registry.IconRegistry;
import com.infinityraider.settlercraft.render.RenderSettlement;
import com.infinityraider.settlercraft.render.schematic.SchematicInWorldPlannerRenderer;
import com.infinityraider.settlercraft.settlement.SettlementHandler;
import com.infinityraider.infinitylib.proxy.base.IClientProxyBase;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("unused")
@SideOnly(Side.CLIENT)
public class ClientProxy implements IProxy, IClientProxyBase {
    @Override
    public void initConfiguration(FMLPreInitializationEvent event) {
        IProxy.super.initConfiguration(event);
        ConfigurationHandler.getInstance().initClientConfigs(event);
    }

    @Override
    public ISettlementHandler getSettlementHandler() {
        return getEffectiveSide() == Side.CLIENT ? SettlementHandler.getInstanceClient() : SettlementHandler.getInstanceServer();
    }

    @Override
    public void registerEventHandlers() {
        IProxy.super.registerEventHandlers();
        registerEventHandler(SettlementHandler.getInstanceClient());
        registerEventHandler(SchematicInWorldPlannerRenderer.getInstance());
        registerEventHandler(RenderSettlement.getInstance());
        registerEventHandler(IconRegistry.getInstance());
    }
}
