package com.InfinityRaider.settlercraft.settlement;

import com.InfinityRaider.settlercraft.SettlerCraft;
import com.InfinityRaider.settlercraft.api.v1.*;
import com.InfinityRaider.settlercraft.settlement.building.BuildingStyleRegistry;
import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import com.InfinityRaider.settlercraft.settlement.settler.EntitySettlerFakePlayer;
import com.InfinityRaider.settlercraft.settlement.settler.container.ContainerSettler;
import com.InfinityRaider.settlercraft.utility.ChunkCoordinates;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
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
    private Map<UUID, ContainerSettler> containersToClose;
    private Map<EntitySettler, FakePlayer> fakePlayers = new HashMap<>();
    private final boolean client;

    protected SettlementHandler(boolean client) {
        this.client = client;
        this.reset();
    }

    protected void reset() {
        settlementsById = new HashMap<>();
        settlementsByChunk = new HashMap<>();
        buildingBuffer = new HashMap<>();
        this.containersToClose = new HashMap<>();
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
        int dim = world.provider.getDimension();
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
        return settlementsByChunk.entrySet().stream().filter(entry -> entry.getKey().dim() == world.provider.getDimension()).map(Map.Entry::getValue).collect(Collectors.toList());
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
    public ISettlement startNewSettlement(EntityPlayer player, IBuildingStyle style) {
        if(player.getEntityWorld().isRemote) {
            return null;
        }
        if(!canCreateSettlementAtCurrentPosition(player)) {
            return null;
        }
        if(style == null) {
            style = BuildingStyleRegistry.getInstance().defaultStyle();
        }
        World world = player.worldObj;
        int x = (int) player.posX;
        int y = (int) player.posY;
        int z = (int) player.posZ;
        Settlement settlement = new Settlement(getNextId(), world, player, new BlockPos(x, y, z), player.getDisplayName().getFormattedText() + "'s Settlement", style);
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
        Iterator<ISettlementBuilding> it = buildings.iterator();
        while(it.hasNext()) {
            if(settlement.onBuildingUpdated(it.next())) {
                it.remove();
            }
        }
        if(buildings.size() <= 0) {
            buildingBuffer.put(settlement.id(), null);
        }
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

    protected void onClientDisconnected() {
        reset();
    }

    protected void onChunkUnloaded(Chunk chunk, ISettlement settlement) {
        processBuffers(settlement);
        ChunkCoordinates coords = new ChunkCoordinates(chunk);
        settlementsById.remove(settlement.id());
        settlementsByChunk.remove(coords);
    }

    public void onContainerClosed(ContainerSettler closedContainer) {
        this.containersToClose.put(closedContainer.getPlayer().getUniqueID(), closedContainer);
    }

    public FakePlayer getFakePlayerForSettler(EntitySettler settler) {
        if((settler.getWorld() instanceof WorldServer) && !fakePlayers.containsKey(settler)) {
            fakePlayers.put(settler, new EntitySettlerFakePlayer((WorldServer) settler.getWorld(), settler));
        }
        return fakePlayers.get(settler);
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onTickEvent(TickEvent.PlayerTickEvent event) {
        if(containersToClose.containsKey(event.player.getUniqueID())) {
            containersToClose.get(event.player.getUniqueID()).afterContainerClosed();
            containersToClose.remove(event.player.getUniqueID());
        }
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

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onWorldUnload(WorldEvent.Unload event) {
        if(event.getWorld() instanceof WorldServer) {
            WorldServer world = (WorldServer) event.getWorld();
            Iterator<Map.Entry<EntitySettler, FakePlayer>> it = fakePlayers.entrySet().iterator();
            while(it.hasNext()) {
                Map.Entry<EntitySettler, FakePlayer> entry = it.next();
                if(entry.getValue().worldObj == world) {
                    it.remove();
                }
            }
        }
    }
}
