package com.InfinityRaider.settlercraft.utility.debug;

import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.api.v1.ISettlementBuilding;
import com.InfinityRaider.settlercraft.settlement.SettlementBuilding;
import com.InfinityRaider.settlercraft.settlement.SettlementHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DebugModeResetBuilding extends DebugMode {
    @Override
    public String debugName() {
        return "reset building";
    }

    @Override
    public void debugAction(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!world.isRemote) {
            ISettlement settlement = SettlementHandler.getInstance().getSettlementForPosition(world, pos.getX(), pos.getY(), pos.getZ());
            if(settlement == null) {
                return;
            }
            ISettlementBuilding building = settlement.getBuildingForLocation(pos.getX(), pos.getY(), pos.getZ());
            if(building == null || !(building instanceof SettlementBuilding)) {
                return;
            }
            ((SettlementBuilding) building).resetWorkersAndInhabitants();
        }
    }
}
