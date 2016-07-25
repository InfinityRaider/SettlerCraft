package com.InfinityRaider.settlercraft.handler;

import com.InfinityRaider.settlercraft.network.MessageSettlerSleeping;
import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import com.infinityraider.infinitylib.network.NetworkWrapper;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SleepHandler {
    private static final SleepHandler INSTANCE = new SleepHandler();

    private Map<World, List<BlockPos>> occupiedBeds;

    public static SleepHandler getInstance() {
        return INSTANCE;
    }

    private SleepHandler() {
        occupiedBeds = new HashMap<>();
    }

    public boolean putSettlerToSleep(World world, BlockPos pos, IBlockState bed, EntitySettler settler) {
        BlockPos head = getHeadPos(pos, bed);
        if(isBedOccupied(world, head, bed)) {
            return false;
        }
        if(!occupiedBeds.containsKey(settler.getWorld())) {
            occupiedBeds.put(world, new ArrayList<>());
        }
        List<BlockPos> list = occupiedBeds.get(world);
        if(!list.contains(head)) {
            list.add(head);
        }
        if(!world.isRemote) {
            NetworkWrapper.getInstance().sendToAll(new MessageSettlerSleeping(settler, pos, true));
        }
        return true;
    }

    public void wakeSettlerUp(EntitySettler settler) {
        if(!occupiedBeds.containsKey(settler.getWorld())) {
            return;
        }
        BlockPos pos = settler.getPosition();
        occupiedBeds.get(settler.getWorld()).remove(pos);
        if(!settler.getWorld().isRemote) {
            NetworkWrapper.getInstance().sendToAll(new MessageSettlerSleeping(settler, pos, false));
        }
    }

    public boolean isBedOccupied(World world, BlockPos pos, IBlockState bed) {
        if(bed.getBlock() instanceof BlockBed) {
            if (bed.getValue(BlockBed.OCCUPIED)) {
                return true;
            }
            BlockPos head = getHeadPos(pos, bed);
            return occupiedBeds.containsKey(world) && occupiedBeds.get(world).contains(head);
        }
        return false;
    }

    private BlockPos getHeadPos(BlockPos pos, IBlockState bed) {
        BlockBed.EnumPartType part = bed.getValue(BlockBed.PART);
        if(part == BlockBed.EnumPartType.FOOT) {
            EnumFacing facing = bed.getValue(BlockBed.FACING);
            return pos.offset(facing);
        }
        return pos;
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onPlayerTryToSleep(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        IBlockState state = world.getBlockState(pos);
        if(!(state.getBlock() instanceof BlockBed)) {
            return;
        }
        if(isBedOccupied(world, pos, state)) {
            event.setCanceled(true);
            event.setUseBlock(Event.Result.DENY);
            event.setUseItem(Event.Result.DENY);
            event.getEntityPlayer().addChatComponentMessage(new TextComponentString("Occupied"));
        }
    }
}
