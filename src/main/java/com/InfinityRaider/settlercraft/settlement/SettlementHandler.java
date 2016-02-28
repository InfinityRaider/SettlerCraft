package com.InfinityRaider.settlercraft.settlement;

import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.api.v1.ISettlementHandler;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.handler.GuiHandler;
import com.InfinityRaider.settlercraft.reference.Names;
import com.InfinityRaider.settlercraft.utility.ChunkCoordinates;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;

public class SettlementHandler implements ISettlementHandler {
    private static final SettlementHandler INSTANCE = new SettlementHandler();

    public static SettlementHandler getInstance() {
        return INSTANCE;
    }

    private final Map<Integer, ISettlement> settlementsById;
    private final Map<ChunkCoordinates, ISettlement> settlementsByChunk;
    private final Map<UUID, ISettler> interacts;

    private SettlementHandler() {
        settlementsById = new HashMap<>();
        settlementsByChunk = new HashMap<>();
        interacts = new HashMap<>();
    }

    @Override
    public ISettlement getSettlement(int id) {
        return settlementsById.get(id);
    }

    @Override
    public ISettlement getSettlementForPosition(World world, double x, double y, double z) {
        int dim = world.provider.getDimensionId();
        for(Map.Entry<ChunkCoordinates, ISettlement> entry : settlementsByChunk.entrySet()) {
            if(entry.getKey().dim() != dim) {
                continue;
            }
            ISettlement settlement = entry.getValue();
            if(settlement.isWithinSettlementBounds(x, y, z)) {
                return settlement;
            }
        }
        return null;
    }

    @Override
    public ISettlement getSettlementForChunk(Chunk chunk) {
        return settlementsByChunk.get(new ChunkCoordinates(chunk));
    }

    @Override
    public boolean canCreateSettlementAtCurrentPosition(EntityPlayer player) {
        return true;
    }

    @Override
    public ISettlement startNewSettlement(EntityPlayer player, ISettler settler) {
        if(!canCreateSettlementAtCurrentPosition(player)) {
            return null;
        }
        World world = player.worldObj;
        int x = (int) player.posX;
        int y = (int) player.posY;
        int z = (int) player.posZ;
        Settlement settlement = new Settlement(getNextId(), world, player, new BlockPos(x, y, z), "");
        settlementsByChunk.put(new ChunkCoordinates(settlement.homeChunk()), settlement);
        settlementsById.put(settlement.id(), settlement);
        settlement.addInhabitant(settler);
        return settlement;
    }

    private int getNextId() {
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

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onChunkSave(ChunkDataEvent.Save event) {
        ISettlement settlement = getSettlementForChunk(event.getChunk());
        if(settlement != null) {
            event.getData().setTag(Names.NBT.SETTLEMENT, settlement.writeToNBT());
            boolean test = true;
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onChunkLoad(ChunkDataEvent.Load event) {
        if(event.getData().hasKey(Names.NBT.SETTLEMENT)) {
            NBTTagCompound settlementTag = event.getData().getCompoundTag(Names.NBT.SETTLEMENT);
            ISettlement settlement = getSettlementForChunk(event.getChunk());
            if(settlement == null) {
                settlement = new Settlement(event.world);
            }
            settlement.readFromNBT(settlementTag);
        }
    }
}
