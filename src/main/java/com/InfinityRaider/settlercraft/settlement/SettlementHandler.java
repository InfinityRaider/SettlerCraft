package com.InfinityRaider.settlercraft.settlement;

import com.InfinityRaider.settlercraft.SettlerCraft;
import com.InfinityRaider.settlercraft.api.v1.IBuildingStyle;
import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.api.v1.ISettlementHandler;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.network.MessageCreateSettlement;
import com.InfinityRaider.settlercraft.network.NetWorkWrapper;
import com.InfinityRaider.settlercraft.settlement.building.BuildingStyleRegistry;
import com.InfinityRaider.settlercraft.settlement.settler.EntitySettlerFakePlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.FakePlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SettlementHandler implements ISettlementHandler {
    private static final ISettlementHandler INSTANCE_CLIENT = new SettlementHandlerClient();
    private static final ISettlementHandler INSTANCE_SERVER = new SettlementHandlerServer();

    public static ISettlementHandler getInstance() {
        return SettlerCraft.proxy.getSettlementHandler();
    }

    public static ISettlementHandler getInstanceClient() {
        return INSTANCE_CLIENT;
    }

    public static ISettlementHandler getInstanceServer() {
        return INSTANCE_SERVER;
    }

    private Map<ISettler, FakePlayer> fakePlayers;

    protected SettlementHandler() {
        this.fakePlayers = new HashMap<>();
    }

    protected abstract SettlementWorldData getSettlementData(World world);

    @Override
    public ISettlement getSettlement(World world, int id) {
        return getSettlementData(world).getSettlementFromId(id);
    }

    @Override
    public ISettlement getSettlementForPosition(World world, double x, double y, double z) {
        for(ISettlement settlement : getSettlementsForWorld(world)) {
            if(settlement.isWithinSettlementBounds(x, y, z)) {
                return settlement;
            }
        }
        return null;
    }

    @Override
    public ISettlement getSettlementForChunk(Chunk chunk) {
        return getSettlementData(chunk.getWorld()).getSettlementFromChunk(chunk);
    }

    @Override
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
        return nearest;
    }

    @Override
    public List<ISettlement> getSettlementsForWorld(World world) {
        return getSettlementData(world).getSettlements();
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
        ISettlement settlement =  getSettlementData(world).getNewSettlement(
                world, player, new BlockPos(x, y, z), player.getDisplayName().getFormattedText() + "'s Settlement", style);
        MessageCreateSettlement message = new MessageCreateSettlement(settlement);
        NetWorkWrapper.getInstance().sendToDimension(message, world);
        return settlement;
    }

    @Override
    public FakePlayer getFakePlayerForSettler(ISettler settler) {
        if((settler.getWorld() instanceof WorldServer) && !fakePlayers.containsKey(settler)) {
            fakePlayers.put(settler, new EntitySettlerFakePlayer((WorldServer) settler.getWorld(), settler));
        }
        return fakePlayers.get(settler);
    }
}
