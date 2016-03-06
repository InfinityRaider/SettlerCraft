package com.InfinityRaider.settlercraft.settlement;

import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class SettlementHandlerClient extends SettlementHandler {
    private Map<Integer, List<ISettler>> inhabitantBuffer;

    protected SettlementHandlerClient() {
        super(true);
        inhabitantBuffer = new HashMap<>();
    }

    @Override
    public void addSettlerToBuffer(int settlementId, ISettler settler) {
        if(this.inhabitantBuffer == null) {
            this.inhabitantBuffer = new HashMap<>();
        }
        if(!this.inhabitantBuffer.containsKey(settlementId)) {
            this.inhabitantBuffer.put(settlementId, new ArrayList<>());
        }
        this.inhabitantBuffer.get(settlementId).add(settler);
    }

    @Override
    public void processBuffers(ISettlement settlement) {
        if(settlement == null ||inhabitantBuffer == null) {
            return;
        }
        List<ISettler> settlers = inhabitantBuffer.get(settlement.id());
        if(settlers == null) {
            return;
        }
        super.processBuffers(settlement);
        settlers.forEach(settlement::addInhabitant);
        inhabitantBuffer.put(settlement.id(), null);
    }

    @Override
    protected void reset() {
        super.reset();
        this.inhabitantBuffer = new HashMap<>();
    }
}
