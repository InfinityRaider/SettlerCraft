package com.infinityraider.settlercraft.utility.debug;

import com.infinityraider.settlercraft.api.v1.IBoundingBox;
import com.infinityraider.settlercraft.api.v1.ISettlement;
import com.infinityraider.settlercraft.api.v1.ISettlementBuilding;
import com.infinityraider.settlercraft.settlement.SettlementHandler;
import com.infinityraider.infinitylib.utility.debug.DebugMode;
import net.minecraft.entity.EntityLivingBase;
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
    public void debugActionBlockClicked(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {}

    @Override
    public void debugActionClicked(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        int amount = SettlementHandler.getInstance().getSettlementsForWorld(world).size();
        String server = world.isRemote ? "CLIENT" : "SERVER";
        player.sendMessage(new TextComponentString("Settlement debug mode for side: " + server));
        player.sendMessage(new TextComponentString("There are " + amount + " settlements in this world"));
        ISettlement settlement = SettlementHandler.getInstance().getSettlementForPosition(world, player.posX, player.posY + player.getEyeHeight(), player.posZ);
        if(settlement == null) {
            player.sendMessage(new TextComponentString("There is no settlement on this position"));
        } else {
            player.sendMessage(new TextComponentString("Current settlement is: " + settlement.name()));
            player.sendMessage(new TextComponentString("The mayor is: " + (settlement.mayor() == null ? "NULL" : settlement.mayor().getDisplayNameString())));
            player.sendMessage(new TextComponentString("There are " + settlement.population() + " inhabitants"));
            player.sendMessage(new TextComponentString("There are " + settlement.getBuildings().size() + " buildings"));
            player.sendMessage(new TextComponentString("The settlement is currently tier " + settlement.tier()));
            IBoundingBox box = settlement.getBoundingBox();
            player.sendMessage(new TextComponentString("The settlement boundaries range from ("
                    + box.minX() + ", " + box.minY() + ", " + box.minZ() + ") to ("
                    + box.maxX() + ", " + box.maxY() + ", " + box.maxZ() + ")"));
            ISettlementBuilding building = settlement.getBuildingForLocation(player.posX, player.posY + player.getEyeHeight(), player.posZ);
            if(building == null) {
                player.sendMessage(new TextComponentString("There is no building on this position"));
            } else {
                player.sendMessage(new TextComponentString("Building at this position is: " + building.building().name()));
                if(building.isComplete()) {
                    player.sendMessage(new TextComponentString("The building is completed"));
                } else {
                    player.sendMessage(new TextComponentString("The building is under construction"));
                }
                player.sendMessage(new TextComponentString("There are " + building.inhabitantCount() + " settlers living in this building"));
                player.sendMessage(new TextComponentString("There are " + building.workerCount() + " settlers working in this building"));
            }
        }
    }

    @Override
    public void debugActionEntityClicked(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {}
}
