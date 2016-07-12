package com.InfinityRaider.settlercraft.utility.debug;

import com.InfinityRaider.settlercraft.api.v1.IBoundingBox;
import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.api.v1.ISettlementBuilding;
import com.InfinityRaider.settlercraft.settlement.SettlementHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class DebugModeSettlementInfo extends DebugMode {
    @Override
    public String debugName() {
        return "settlement info";
    }

    @Override
    public void debugAction(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        int amount = SettlementHandler.getInstance().getSettlementsForWorld(world).size();
        String server = world.isRemote ? "CLIENT" : "SERVER";
        player.addChatComponentMessage(new TextComponentString("Settlement debug mode for side: " + server));
        player.addChatComponentMessage(new TextComponentString("There are " + amount + " settlements in this world"));
        ISettlement settlement = SettlementHandler.getInstance().getSettlementForPosition(world, pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ);
        if(settlement == null) {
            player.addChatComponentMessage(new TextComponentString("There is no settlement on this position"));
        } else {
            player.addChatComponentMessage(new TextComponentString("Current settlement is: " + settlement.name()));
            player.addChatComponentMessage(new TextComponentString("The mayor is: " + (settlement.mayor() == null ? "NULL" : settlement.mayor().getDisplayNameString())));
            player.addChatComponentMessage(new TextComponentString("There are " + settlement.population() + " inhabitants"));
            player.addChatComponentMessage(new TextComponentString("There are " + settlement.getBuildings().size() + " buildings"));
            player.addChatComponentMessage(new TextComponentString("The settlement is currently tier " + settlement.tier()));
            IBoundingBox box = settlement.getBoundingBox();
            player.addChatComponentMessage(new TextComponentString("The settlement boundaries range from ("
                    + box.minX() + ", " + box.minY() + ", " + box.minZ() + ") to ("
                    + box.maxX() + ", " + box.maxY() + ", " + box.maxZ() + ")"));
            ISettlementBuilding building = settlement.getBuildingForLocation(pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ);
            if(building == null) {
                player.addChatComponentMessage(new TextComponentString("There is no building on this position"));
            } else {
                player.addChatComponentMessage(new TextComponentString("Building at this position is: " + building.building().name()));
                if(building.isComplete()) {
                    player.addChatComponentMessage(new TextComponentString("The building is completed"));
                } else {
                    player.addChatComponentMessage(new TextComponentString("The building is under construction"));
                }
                player.addChatComponentMessage(new TextComponentString("There are " + building.inhabitantCount() + " settlers living in this building"));
                player.addChatComponentMessage(new TextComponentString("There are " + building.workerCount() + " settlers working in this building"));
            }
        }
    }
}
