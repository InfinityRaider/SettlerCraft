package com.InfinityRaider.settlercraft.utility.debug;

import com.InfinityRaider.settlercraft.api.v1.ITask;
import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import com.InfinityRaider.settlercraft.settlement.settler.ai.task.TaskDummy;
import com.InfinityRaider.settlercraft.settlement.settler.ai.task.TaskMoveToPosition;
import com.infinityraider.infinitylib.utility.debug.DebugMode;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.List;

public class DebugModeSettlerPathing extends DebugMode {
    private EntitySettler settler;

    @Override
    public String debugName() {
        return "settler pathing";
    }

    @Override
    public void debugActionBlockClicked(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!world.isRemote && this.settler != null) {
            ITask task = new TaskMoveToPosition<>(new TaskDummy(1, "debug_pathing", this.settler), new Vec3d(pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ));
            this.settler.assignTask(task);
        }
    }

    @Override
    public void debugActionClicked(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if(!player.getEntityWorld().isRemote) {
            List<EntitySettler> settlerList = world.getEntitiesWithinAABB(EntitySettler.class,
                    new AxisAlignedBB(player.posX - 2, player.posY - 2, player.chasingPosZ - 2, player.posX + 2, player.posY + 2, player.chasingPosZ + 2));
            double dist = Double.MAX_VALUE;
            for(EntitySettler settler : settlerList) {
                double d = player.getDistanceSqToEntity(settler);
                if(d < dist) {
                    dist = d;
                    this.settler = settler;
                }
            }
            if(this.settler != null) {
                player.sendMessage(new TextComponentString("Set settler to " + this.settler.toString()));
            }
        }
    }

    @Override
    public void debugActionEntityClicked(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
        if(!player.getEntityWorld().isDaytime() && target instanceof EntitySettler) {
            this.settler = (EntitySettler) target;
        }
    }
}
