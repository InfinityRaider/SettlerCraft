package com.InfinityRaider.settlercraft.settlement;

import com.InfinityRaider.settlercraft.SettlerCraft;
import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.api.v1.ISettlementHandler;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.settlement.settler.EntitySettlerFakePlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SettlementHandler implements ISettlementHandler {
    private static SettlementHandlerServer INSTANCE_SERVER;
    @SideOnly(Side.CLIENT)
    private static SettlementHandlerClient INSTANCE_CLIENT;

    public static ISettlementHandler getInstance() {
        return SettlerCraft.proxy.getSettlementHandler();
    }

    public static SettlementHandlerServer getInstanceServer() {
        if(INSTANCE_SERVER == null) {
            INSTANCE_SERVER = new SettlementHandlerServer();
        }
        return INSTANCE_SERVER;
    }

    @SideOnly(Side.CLIENT)
    public static SettlementHandlerClient getInstanceClient() {
        if(INSTANCE_CLIENT == null) {
            INSTANCE_CLIENT = new SettlementHandlerClient();
        }
        return INSTANCE_CLIENT;
    }

    private Map<ISettler, FakePlayer> fakePlayers;

    protected abstract SettlementWorldData getSettlementData(World world);

    protected SettlementHandler() {
        this.fakePlayers = new HashMap<>();
    }

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
    public FakePlayer getFakePlayerForSettler(ISettler settler) {
        if((settler.getWorld() instanceof WorldServer) && !fakePlayers.containsKey(settler)) {
            fakePlayers.put(settler, new EntitySettlerFakePlayer((WorldServer) settler.getWorld(), settler));
        }
        return fakePlayers.get(settler);
    }
}
