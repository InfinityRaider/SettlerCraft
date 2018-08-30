package com.infinityraider.settlercraft.proxy;

import com.infinityraider.settlercraft.api.v1.ISettlementHandler;
import com.infinityraider.settlercraft.settlement.SettlementHandler;
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
