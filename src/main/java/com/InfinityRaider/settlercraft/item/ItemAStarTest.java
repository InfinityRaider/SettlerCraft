package com.InfinityRaider.settlercraft.item;

import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import com.InfinityRaider.settlercraft.settlement.settler.ai.pathfinding.astar.AStarTest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemAStarTest extends ItemBase {
    public ItemAStarTest() {
        super("AStarTest");
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(world.isRemote) {
            if (player.isSneaking()) {
                AStarTest.getInstance().reset();
            } else {
                AStarTest.getInstance().onRightClick(world, pos.offset(side), new EntitySettler(world));
            }
        }
        return EnumActionResult.PASS;
    }
}
