package com.InfinityRaider.settlercraft.settlement;

import com.InfinityRaider.settlercraft.api.v1.IBuildingStyle;
import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.reference.Names;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.MapStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettlementWorldData extends WorldSavedData {
    private static final String KEY = "SC_WORLD_DATA";

    private final World world;

    private Map<Integer, ISettlement> settlementsById;
    private Map<Chunk, ISettlement> settlementsByChunk;

    public SettlementWorldData(World world) {
        super(KEY);
        this.world = world;
        this.settlementsById = new HashMap<>();
        this.settlementsByChunk = new HashMap<>();
    }

    public ISettlement getNewSettlement(World world, EntityPlayer player, BlockPos center, String name, IBuildingStyle style) {
        ISettlement settlement = new Settlement(world, getNextSettlementId(), player, center, name, style);
        settlementsById.put(settlement.id(), settlement);
        settlementsByChunk.put(settlement.homeChunk(), settlement);
        this.markDirty();
        return settlement;
    }

    private int getNextSettlementId() {
        int id = settlementsById.size();
        if(!settlementsById.containsKey(id)) {
            return id;
        } else {
            for(int i = 0; i < settlementsById.size(); i++) {
                if(!settlementsById.containsKey(i)) {
                    return i;
                }
            }
        }
        return id;
    }

    public ISettlement getSettlementFromId(int id) {
        return this.settlementsById.get(id);
    }

    public ISettlement getSettlementFromChunk(Chunk chunk) {
        return this.settlementsByChunk.get(chunk);
    }

    public List<ISettlement> getSettlements() {
        return ImmutableList.copyOf(settlementsById.values());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList list = new NBTTagList();
        for(ISettlement settlement : this.getSettlements()) {
            NBTTagCompound tag = settlement.readSettlementFromNBT(new NBTTagCompound());
            list.appendTag(tag);
        }
        nbt.setTag(Names.NBT.SETTLEMENT, list);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        this.settlementsById = new HashMap<>();
        this.settlementsByChunk = new HashMap<>();
        if(nbt.hasKey(Names.NBT.SETTLEMENT)) {
            NBTTagList list = nbt.getTagList(Names.NBT.SETTLEMENT, 10);
            for(int i = 0; i < list.tagCount(); i++) {
                ISettlement settlement = new Settlement(this.world);
                settlement.readSettlementFromNBT(list.getCompoundTagAt(i));
                settlementsById.put(settlement.id(), settlement);
                settlementsByChunk.put(settlement.homeChunk(), settlement);
            }
        }
    }

    public static SettlementWorldData forWorld(World world) {
        MapStorage storage = world.getPerWorldStorage();
        SettlementWorldData data = (SettlementWorldData) storage.loadData(SettlementWorldData.class, KEY);
        if(data == null) {
            data = new SettlementWorldData(world);
            storage.setData(KEY, data);
        }
        return data;
    }
}
