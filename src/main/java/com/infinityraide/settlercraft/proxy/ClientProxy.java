package com.infinityraide.settlercraft.proxy;

import com.infinityraide.settlercraft.api.v1.ISettlementHandler;
import com.infinityraide.settlercraft.handler.ConfigurationHandler;
import com.infinityraide.settlercraft.registry.IconRegistry;
import com.infinityraide.settlercraft.render.RenderSettlement;
import com.infinityraide.settlercraft.render.schematic.SchematicInWorldPlannerRenderer;
import com.infinityraide.settlercraft.settlement.SettlementHandler;
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
