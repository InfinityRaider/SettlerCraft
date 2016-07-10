package com.InfinityRaider.settlercraft.utility.debug;

import com.InfinityRaider.settlercraft.utility.DebugHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DebugModeFeedback extends DebugMode {
    @Override
    public String debugName() {
        return "feedback";
    }

    @Override
    public void debugAction(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        DebugHelper.getInstance().debug(player, world, pos);
    }
}
