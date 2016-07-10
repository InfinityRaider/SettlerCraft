package com.InfinityRaider.settlercraft.settlement;

import com.InfinityRaider.settlercraft.SettlerCraft;
import com.InfinityRaider.settlercraft.api.v1.IBuildingStyle;
import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
public class SettlementHandlerClient extends SettlementHandler {
    private static final SettlementWorldData DUMMY = new SettlementWorldDataDummy();

    private SettlementWorldData data;

    protected SettlementHandlerClient() {
        super();
        this.data = DUMMY;
    }

    @Override
    protected SettlementWorldData getSettlementData(World world) {
        return this.getSettlementData();
    }

    public SettlementWorldData getSettlementData() {
        return this.data;
    }

    @Override
    public ISettlement startNewSettlement(EntityPlayer player, IBuildingStyle style) {
        return null;
    }

    public void syncSettlementFromNBT(NBTTagCompound tag) {
        if(!isAssigned()) {
            this.data = new SettlementWorldData(SettlerCraft.proxy.getClientWorld());
        }
        this.getSettlementData().readSettlementFromNBT(tag);
    }

    private boolean isAssigned() {
        return this.getSettlementData() != DUMMY;
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onPlayerDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
            this.data = DUMMY;
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onPlayerDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        this.data = DUMMY;
    }

    private static final class SettlementWorldDataDummy extends SettlementWorldData {
        public SettlementWorldDataDummy() {
            super((World) null);
        }

        @Override
        public ISettlement getNewSettlement(World world, EntityPlayer player, BlockPos center, String name, IBuildingStyle style) {
            return null;
        }

        public ISettlement getSettlementFromId(int id) {
            return null;
        }

        @Override
        public ISettlement getSettlementFromChunk(Chunk chunk) {
            return null;
        }

        @Override
        public List<ISettlement> getSettlements() {
            return Collections.emptyList();
        }

        @Override
        public void readFromNBT(NBTTagCompound nbt) {}

        @Override
        public void writeToNBT(NBTTagCompound nbt) {}
    }
}
