package com.infinityraide.settlercraft.utility.debug;

import com.infinityraide.settlercraft.settlement.settler.EntitySettler;
import com.infinityraide.settlercraft.settlement.settler.ai.pathfinding.astar.AStarTest;
import com.infinityraider.infinitylib.utility.debug.DebugMode;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DebugModePathfinding extends DebugMode {
    @Override
    public String debugName() {
        return "pathfinding";
    }

    @Override
    public void debugActionBlockClicked(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(world.isRemote) {
            if (player.isSneaking()) {
                AStarTest.getInstance().reset();
            } else {
                AStarTest.getInstance().onRightClick(world, pos.offset(side), new EntitySettler(world));
            }
        }
    }

    @Override
    public void debugActionClicked(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {}

    @Override
    public void debugActionEntityClicked(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {}
}
