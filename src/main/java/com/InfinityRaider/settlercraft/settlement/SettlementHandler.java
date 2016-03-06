package com.InfinityRaider.settlercraft.settlement;

import com.InfinityRaider.settlercraft.SettlerCraft;
import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.api.v1.ISettlementBuilding;
import com.InfinityRaider.settlercraft.api.v1.ISettlementHandler;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.handler.GuiHandler;
import com.InfinityRaider.settlercraft.utility.ChunkCoordinates;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;
import java.util.stream.Collectors;

public class SettlementHandler implements ISettlementHandler {
    private static SettlementHandler INSTANCE_SERVER;
    @SideOnly(Side.CLIENT)
    private static SettlementHandler INSTANCE_CLIENT;

    public static SettlementHandler getInstance() {
        return SettlerCraft.proxy.getSettlementHandler();
    }

    @SideOnly(Side.CLIENT)
    public static SettlementHandler getClientInstance() {
        if(INSTANCE_CLIENT == null) {
            INSTANCE_CLIENT = new SettlementHandlerClient();
        }
        return INSTANCE_CLIENT;
    }

    public static SettlementHandler getServerInstance() {
        if(INSTANCE_SERVER == null) {
            INSTANCE_SERVER = new SettlementHandler(false);
        }
        return INSTANCE_SERVER;
    }

    private Map<Integer, ISettlement> settlementsById;
    private Map<ChunkCoordinates, ISettlement> settlementsByChunk;
    private Map<Integer, List<ISettlementBuilding>> buildingBuffer;
    private Map<UUID, ISettler> interacts;
    private final boolean client;

    protected SettlementHandler(boolean client) {
        this.client = client;
        this.reset();
    }

    protected void reset() {
        settlementsById = new HashMap<>();
        settlementsByChunk = new HashMap<>();
        buildingBuffer = new HashMap<>();
        interacts = new HashMap<>();
    }

    @Override
    public Side getEffectiveSide() {
        return client ? Side.CLIENT : Side.SERVER;
    }

    @Override
    public ISettlement getSettlement(int id) {
        ISettlement settlement = settlementsById.get(id);
        if(settlement != null) {
            this.processBuffers(settlement);
        }
        return settlement;
    }

    @Override
    public ISettlement getSettlementForPosition(World world, double x, double y, double z) {
        int dim = world.provider.getDimensionId();
        ISettlement settlement = null;
        for(Map.Entry<ChunkCoordinates, ISettlement> entry : settlementsByChunk.entrySet()) {
            if(entry.getKey().dim() != dim) {
                continue;
            }
            settlement = entry.getValue();
            if(settlement.isWithinSettlementBounds(x, y, z)) {
                break;
            }
        }
        if(settlement != null) {
            processBuffers(settlement);
        }
        return settlement;
    }

    @Override
    public ISettlement getSettlementForChunk(Chunk chunk) {
        ISettlement settlement = settlementsByChunk.get(new ChunkCoordinates(chunk));
        if(settlement != null) {
            processBuffers(settlement);
        }
        return settlement;
    }

    public ISettlement getNearestSettlement(World world, BlockPos pos) {
        double minDist = Double.MAX_VALUE;
        ISettlement nearest = null;
        for(ISettlement settlement : getSettlementsForWorld(world)) {
            if(nearest == null) {
                nearest = settlement;
                minDist = settlement.calculateDistanceSquaredToSettlement(pos);
            } else {
                double distance = settlement.calculateDistanceSquaredToSettlement(pos);
                if(distance < minDist) {
                    minDist = distance;
                    nearest = settlement;
                }
            }
        }
        if(nearest != null) {
            processBuffers(nearest);
        }
        return nearest;
    }

    @Override
    public List<ISettlement> getSettlementsForWorld(World world) {
        return settlementsByChunk.entrySet().stream().filter(entry -> entry.getKey().dim() == world.provider.getDimensionId()).map(Map.Entry::getValue).collect(Collectors.toList());
    }

    @Override
    public boolean canCreateSettlementAtCurrentPosition(EntityPlayer player) {
        BlockPos pos = new BlockPos((int) player.posX, (int) player.posY, (int) player.posZ);
        Chunk chunk = player.getEntityWorld().getChunkFromBlockCoords(pos);
        for(int x = -8; x <= 8; x++) {
            for(int z = -8; z <= 8; z++) {
                Chunk chunkAt = player.getEntityWorld().getChunkFromChunkCoords(chunk.xPosition + x, chunk.zPosition + z);
                if(getSettlementForChunk(chunkAt) != null) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public ISettlement startNewSettlement(EntityPlayer player) {
        if(player.getEntityWorld().isRemote) {
            return null;
        }
        if(!canCreateSettlementAtCurrentPosition(player)) {
            return null;
        }
        World world = player.worldObj;
        int x = (int) player.posX;
        int y = (int) player.posY;
        int z = (int) player.posZ;
        Settlement settlement = new Settlement(getNextId(), world, player, new BlockPos(x, y, z), "");
        world.spawnEntityInWorld(settlement);
        settlementsById.put(settlement.id(), settlement);
        settlementsByChunk.put(new ChunkCoordinates(settlement.homeChunk()), settlement);
        return settlement;
    }

    public void onSettlementLoaded(ISettlement settlement) {
        settlementsByChunk.put(new ChunkCoordinates(settlement.homeChunk()), settlement);
        settlementsById.put(settlement.id(), settlement);
        processBuffers(settlement);
    }

    public void addBuildingToBuffer(int settlementId, ISettlementBuilding building) {
        if(!buildingBuffer.containsKey(settlementId)) {
            buildingBuffer.put(settlementId, new ArrayList<>());
        }
        buildingBuffer.get(settlementId).add(building);
    }

    public void addSettlerToBuffer(int settlementId, ISettler settler) {}

    public void processBuffers(ISettlement settlement) {
        if(settlement == null || buildingBuffer == null) {
            return;
        }
        List<ISettlementBuilding> buildings = buildingBuffer.get(settlement.id());
        if(buildings == null) {
            return;
        }
        buildings.forEach(settlement::onBuildingUpdated);
        buildingBuffer.put(settlement.id(), null);
    }

    private int getNextId() {
        if(!settlementsById.containsKey(settlementsById.size())) {
            return settlementsById.size();
        }
        for(int i = 0; i < settlementsById.size(); i++) {
            if(!settlementsById.containsKey(i)) {
                return i;
            }
        }
        return settlementsById.size();
    }

    public void interact(EntityPlayer player, ISettler settler) {
        interacts.put(player.getUniqueID(), settler);
        if(!player.worldObj.isRemote) {
            GuiHandler.getInstance().openSettlerDialogueContainer(player);
        }
    }

    public ISettler getSettlerInteractingWith(EntityPlayer player) {
        return interacts.get(player.getUniqueID());
    }

    public void stopInteractingWithSettler(EntityPlayer player) {
        interacts.remove(player.getUniqueID());
    }

    protected void onClientDisconnected() {
        reset();
    }

    protected void onChunkUnloaded(Chunk chunk, ISettlement settlement) {
        processBuffers(settlement);
        ChunkCoordinates coords = new ChunkCoordinates(chunk);
        settlementsById.remove(settlement.id());
        settlementsByChunk.remove(coords);
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onClientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        onClientDisconnected();
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onChunkUnloadEvent(ChunkEvent.Unload event) {
        ISettlement settlement = getSettlementForChunk(event.getChunk());
        if(settlement != null) {
            onChunkUnloaded(event.getChunk(), settlement);
        }
    }
}
