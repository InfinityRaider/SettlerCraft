package com.InfinityRaider.settlercraft.settlement;

import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
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

    public ISettlement getSettlement(int id) {
        ISettlement settlement = super.getSettlement(id);
        processInhabitantBuffer(settlement);
        return settlement;
    }

    @Override
    public ISettlement getSettlementForPosition(World world, double x, double y, double z) {
        ISettlement settlement = super.getSettlementForPosition(world, x, y, z);
        processInhabitantBuffer(settlement);
        return settlement;
    }

    @Override
     public ISettlement getSettlementForChunk(Chunk chunk) {
        ISettlement settlement = super.getSettlementForChunk(chunk);
        processInhabitantBuffer(settlement);
        return settlement;
    }

    @Override
    public ISettlement getNearestSettlement(World world, BlockPos pos) {
        ISettlement settlement = super.getNearestSettlement(world, pos);
        processInhabitantBuffer(settlement);
        return settlement;
    }

    @Override
    public void addSettlerToInhabitantBuffer(int settlementId, ISettler settler) {
        if(this.inhabitantBuffer == null) {
            this.inhabitantBuffer = new HashMap<>();
        }
        if(!this.inhabitantBuffer.containsKey(settlementId)) {
            this.inhabitantBuffer.put(settlementId, new ArrayList<>());
        }
        this.inhabitantBuffer.get(settlementId).add(settler);
    }

    @Override
    public void processInhabitantBuffer(ISettlement settlement) {
        if(settlement == null ||inhabitantBuffer == null) {
            return;
        }
        List<ISettler> settlers = inhabitantBuffer.get(settlement.id());
        if(settlers == null) {
            return;
        }
        settlers.forEach(settlement::addInhabitant);
        inhabitantBuffer.put(settlement.id(), null);
    }

    @Override
    protected void onClientDisconnected() {
        this.inhabitantBuffer = new HashMap<>();
        super.onClientDisconnected();
    }

    @Override
    protected void onChunkUnloaded(Chunk chunk, ISettlement settlement) {
        processInhabitantBuffer(settlement);
        super.onChunkUnloaded(chunk, settlement);
    }
}
