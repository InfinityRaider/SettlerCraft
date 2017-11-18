package com.InfinityRaider.settlercraft.settlement;

import com.InfinityRaider.settlercraft.api.v1.IBuildingStyle;
import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.network.MessageSyncSettlementsToClient;
import com.InfinityRaider.settlercraft.settlement.building.BuildingStyleRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

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

    @Override
    public ISettlement startNewSettlement(EntityPlayer player, IBuildingStyle style) {
        if(!canCreateSettlementAtCurrentPosition(player)) {
            return null;
        }
        if(style == null) {
            style = BuildingStyleRegistry.getInstance().defaultStyle();
        }
        World world = player.getEntityWorld();
        int x = (int) player.posX;
        int y = (int) player.posY;
        int z = (int) player.posZ;
        ISettlement settlement =  getSettlementData(world).getNewSettlement(
                world, player, new BlockPos(x, y, z), player.getDisplayName().getFormattedText() + "'s Settlement", style);
        new MessageSyncSettlementsToClient(settlement).sendToDimension(world);
        return settlement;
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onPlayerJoined(PlayerEvent.PlayerLoggedInEvent event) {
        new MessageSyncSettlementsToClient(this.getSettlementsForWorld(event.player.getEntityWorld())).sendTo((EntityPlayerMP) event.player);
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onPlayerDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        new MessageSyncSettlementsToClient(this.getSettlementsForWorld(event.player.getEntityWorld())).sendTo((EntityPlayerMP) event.player);
    }
}
