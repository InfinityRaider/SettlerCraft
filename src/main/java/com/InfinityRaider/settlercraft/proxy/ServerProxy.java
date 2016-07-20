package com.InfinityRaider.settlercraft.proxy;

import com.InfinityRaider.settlercraft.api.v1.ISettlementHandler;
import com.InfinityRaider.settlercraft.settlement.SettlementHandler;
import com.infinityraider.infinitylib.proxy.IServerProxyBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("unused")
@SideOnly(Side.SERVER)
public class ServerProxy extends CommonProxy implements IServerProxyBase {

    @Override
    public ISettlementHandler getSettlementHandler() {
        return SettlementHandler.getInstanceServer();
    }

    @Override
    public void registerEventHandlers() {
        super.registerEventHandlers();
    }
}
