package com.InfinityRaider.settlercraft.utility.debug;

import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.api.v1.ISettlementBuilding;
import com.InfinityRaider.settlercraft.settlement.SettlementBuilding;
import com.InfinityRaider.settlercraft.settlement.SettlementHandler;
import com.InfinityRaider.settlercraft.settlement.building.StructureBuildProgress;
import com.infinityraider.infinitylib.utility.debug.DebugMode;
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
    public void debugAction(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(world.isRemote) {
            return;
        }
        ISettlement settlement = SettlementHandler.getInstance().getSettlementForPosition(world, pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ);
        if(settlement == null) {
            return;
        }
        ISettlementBuilding building = settlement.getBuildingForLocation(pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ);
        if(building == null || building.isComplete() || !(building instanceof SettlementBuilding)) {
            return;
        }
        StructureBuildProgress progress = ((SettlementBuilding) building).getBuildProgress();
        progress.autoComplete();
    }
}
