package com.infinityraide.settlercraft.utility.debug;

import com.infinityraide.settlercraft.api.v1.ISettlement;
import com.infinityraide.settlercraft.api.v1.ISettlementBuilding;
import com.infinityraide.settlercraft.settlement.SettlementBuilding;
import com.infinityraide.settlercraft.settlement.SettlementHandler;
import com.infinityraide.settlercraft.settlement.building.StructureBuildProgress;
import com.infinityraider.infinitylib.utility.debug.DebugMode;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DebugModeFinishBuilding extends DebugMode {
    @Override
    public String debugName() {
        return "finish building";
    }

    @Override
    public void debugActionBlockClicked(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {

    }

    @Override
    public void debugActionClicked(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if(world.isRemote) {
            return;
        }
        ISettlement settlement = SettlementHandler.getInstance().getSettlementForPosition(world, player.posX, player.posY + player.getEyeHeight(), player.posZ);
        if(settlement == null) {
            return;
        }
        ISettlementBuilding building = settlement.getBuildingForLocation(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        if(building == null || building.isComplete() || !(building instanceof SettlementBuilding)) {
            return;
        }
        StructureBuildProgress progress = ((SettlementBuilding) building).getBuildProgress();
        progress.autoComplete();
    }

    @Override
    public void debugActionEntityClicked(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {}
}
