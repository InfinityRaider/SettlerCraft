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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class DebugModeSettlerPathing extends DebugMode {
    private EntitySettler settler;

    @Override
    public String debugName() {
        return "settler pathing";
    }

    @Override
    public void debugActionBlockClicked(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!world.isRemote && this.settler != null) {
            ITask task = new TaskMoveToPosition<>(new TaskDummy("debug_pathing", this.settler), new Vec3d(hitX, hitY, hitZ));
            this.settler.assignTask(task);
        }
    }

    @Override
    public void debugActionClicked(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {

    }

    @Override
    public void debugActionEntityClicked(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
        if(!player.getEntityWorld().isRemote && target instanceof EntitySettler) {
            this.settler = (EntitySettler) target;
        }
    }
}
