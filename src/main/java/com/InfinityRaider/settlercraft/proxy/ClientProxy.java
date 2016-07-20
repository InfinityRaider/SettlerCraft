package com.InfinityRaider.settlercraft.proxy;

import com.InfinityRaider.settlercraft.api.v1.ISettlementHandler;
import com.InfinityRaider.settlercraft.handler.ConfigurationHandler;
import com.InfinityRaider.settlercraft.registry.IconRegistry;
import com.InfinityRaider.settlercraft.render.RenderSettlement;
import com.InfinityRaider.settlercraft.render.schematic.SchematicInWorldPlannerRenderer;
import com.InfinityRaider.settlercraft.settlement.SettlementHandler;
import com.infinityraider.infinitylib.proxy.IClientProxyBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("unused")
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy implements IClientProxyBase {
    @Override
    public void initConfiguration(FMLPreInitializationEvent event) {
        super.initConfiguration(event);
        ConfigurationHandler.getInstance().initClientConfigs(event);
    }

    @Override
    public ISettlementHandler getSettlementHandler() {
        return getEffectiveSide() == Side.CLIENT ? SettlementHandler.getInstanceClient() : SettlementHandler.getInstanceServer();
    }

    @Override
    public void registerEventHandlers() {
        super.registerEventHandlers();
        MinecraftForge.EVENT_BUS.register(SettlementHandler.getInstanceClient());
        MinecraftForge.EVENT_BUS.register(SchematicInWorldPlannerRenderer.getInstance());
        MinecraftForge.EVENT_BUS.register(RenderSettlement.getInstance());
        MinecraftForge.EVENT_BUS.register(IconRegistry.getInstance());
    }
}
