package com.InfinityRaider.settlercraft.utility;

import com.InfinityRaider.settlercraft.api.v1.IDebuggable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to aid in the management of debug data.
 */
public class DebugHelper {
    private static final DebugHelper INSTANCE = new DebugHelper();

    public static DebugHelper getInstance() {
        return INSTANCE;
    }

    private DebugHelper() {}

    /**
     * Retrieves the debug data for a location, and displays it in a chat message to the specified player in conjunction with the log.
     *
     * @param player the player requesting the debug data.
     * @param world the world object
     * @param pos the block position
     */
    public void debug(EntityPlayer player, World world, BlockPos pos) {
        for(String dataLine:getDebugData(world, pos)) {
            LogHelper.debug(dataLine);
            player.addChatComponentMessage(new TextComponentString(dataLine));
        }
    }

    /**
     * Constructs a list of strings representing the debug information for the provided location.
     *
     * @param world the world object
     * @param pos the block position
     * @return a list of strings representing the requested debug data.
     */
    private List<String> getDebugData(World world, BlockPos pos) {
        List<String> debugData = new ArrayList<>();

        if (!world.isRemote) {
            debugData.add("------------------");
            debugData.add("Server debug info:");
            debugData.add("------------------");
        } else {
            debugData.add("------------------");
            debugData.add("Client debug info:");
            debugData.add("------------------");
        }

        TileEntity tile = world.getTileEntity(pos);

        debugData.add("Clicked block at (" + pos.getX() +", " + pos.getY() + ", " + pos.getZ() +")");

        if(tile!=null && tile instanceof IDebuggable) {
            ((IDebuggable) tile).addDebugInfo(debugData);
        }
        else {
            IBlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            debugData.add("Block: " + Block.blockRegistry.getNameForObject(block));
            debugData.add("Meta: " + block.getMetaFromState(state));
        }

        debugData.add(" ");

        return debugData;
    }
}

