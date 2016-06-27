package com.InfinityRaider.settlercraft.settlement;

import com.InfinityRaider.settlercraft.SettlerCraft;
import net.minecraft.world.World;

public class SettlementHandlerClient extends SettlementHandler {
    private SettlementWorldData data;

    protected SettlementHandlerClient() {
        super();
        this.data = new SettlementWorldData(SettlerCraft.proxy.getClientWorld());
    }

    @Override
    protected SettlementWorldData getSettlementData(World world) {
        return data;
    }
}
