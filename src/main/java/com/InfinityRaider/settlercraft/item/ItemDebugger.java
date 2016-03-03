package com.InfinityRaider.settlercraft.item;

import com.InfinityRaider.settlercraft.api.v1.IItemRenderSettlementBoxes;
import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.utility.DebugHelper;
import com.InfinityRaider.settlercraft.utility.schematic.SchematicReader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDebugger extends ItemBase implements IItemRenderSettlementBoxes {
    public ItemDebugger() {
        super("debugger");
        this.setMaxStackSize(1);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(world.isRemote) {
            if (!player.isSneaking()) {
                DebugHelper.getInstance().debug(player, world, pos);
            }
        } else {
            if(player.isSneaking()) {
                int rotation;
                if(player.posX < hitX + pos.getX()) {
                    if(player.posZ < hitZ + pos.getZ()) {
                        rotation = 0;
                    } else {
                        rotation = 3;
                    }
                } else {
                    if(player.posZ < hitZ + pos.getZ()) {
                        rotation = 1;
                    } else {
                        rotation = 2;
                    }
                }
                SchematicReader.getInstance().buildStoredSchematic(world, pos.add(side.getFrontOffsetX(), side.getFrontOffsetY(), side.getFrontOffsetZ()), rotation);
            }
        }
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldRenderSettlementBoxes(ISettlement settlement, EntityPlayer player, ItemStack stack) {
        return true;
    }
}
