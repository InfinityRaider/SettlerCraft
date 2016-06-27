package com.InfinityRaider.settlercraft.settlement;

import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class SettlementHandlerServer extends SettlementHandler {
    private final Map<World, SettlementWorldData> dataMap;

    protected SettlementHandlerServer() {
        super();
        this.dataMap = new HashMap<>();
    }

    @Override
    protected SettlementWorldData getSettlementData(World world) {
        if(!this.dataMap.containsKey(world)) {
            SettlementWorldData data = SettlementWorldData.forWorld(world);
            this.dataMap.put(world, data);
        }
        return this.dataMap.get(world);
    }
}
