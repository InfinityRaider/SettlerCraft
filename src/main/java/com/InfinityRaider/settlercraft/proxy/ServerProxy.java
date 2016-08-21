package com.InfinityRaider.settlercraft.proxy;

import com.InfinityRaider.settlercraft.api.v1.ISettlementHandler;
import com.InfinityRaider.settlercraft.settlement.SettlementHandler;
import com.infinityraider.infinitylib.proxy.base.IServerProxyBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("unused")
@SideOnly(Side.SERVER)
public class ServerProxy implements IProxy, IServerProxyBase {
    @Override
    public ISettlementHandler getSettlementHandler() {
        return SettlementHandler.getInstanceServer();
    }
}
