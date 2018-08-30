package com.infinityraide.settlercraft.handler;
import com.infinityraide.settlercraft.api.v1.ISettlement;
import com.infinityraide.settlercraft.api.v1.ISettlementBuilding;
import com.infinityraide.settlercraft.settlement.SettlementHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockEventHandler {
    private static final BlockEventHandler INSTANCE = new BlockEventHandler();

    public static BlockEventHandler getInstance() {
        return INSTANCE;
    }

    private BlockEventHandler() {}

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @SuppressWarnings("unused")
    public void onBlockPlaced(BlockEvent.PlaceEvent event) {
        if(event.getWorld().isRemote) {
            return;
        }
        ISettlementBuilding building = getBuildingForPosition(event.getWorld(), event.getPos());
        if(building != null) {
            building.onBlockPlaced(event.getPlayer(), event.getPos(), event.getState());
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @SuppressWarnings("unused")
    public void onBlockBroken(BlockEvent.BreakEvent event) {
        if(event.getWorld().isRemote) {
            return;
        }
        ISettlementBuilding building = getBuildingForPosition(event.getWorld(), event.getPos());
        if(building != null) {
            building.onBlockBroken(event.getPlayer(), event.getPos(), event.getState());
        }
    }

    private ISettlementBuilding getBuildingForPosition(World world, BlockPos pos) {
        ISettlement settlement = SettlementHandler.getInstance().getSettlementForPosition(world, pos.getX(), pos.getY(), pos.getZ());
        if(settlement == null) {
            return null;
        }
        for(ISettlementBuilding building : settlement.getBuildings()) {
            if(building.getBoundingBox().isWithinBounds(pos)) {
                return building;
            }
        }
        return null;
    }
}
