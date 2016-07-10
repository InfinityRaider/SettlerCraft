package com.InfinityRaider.settlercraft.utility.debug;

import com.InfinityRaider.settlercraft.utility.schematic.SchematicReader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DebugModeBuildSchematic extends DebugMode {
    @Override
    public String debugName() {
        return "build schematic";
    }

    @Override
    public void debugAction(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!world.isRemote) {
            int rotation;
            if (player.posX < hitX + pos.getX()) {
                if (player.posZ < hitZ + pos.getZ()) {
                    rotation = 0;
                } else {
                    rotation = 3;
                }
            } else {
                if (player.posZ < hitZ + pos.getZ()) {
                    rotation = 1;
                } else {
                    rotation = 2;
                }
            }
            SchematicReader.getInstance().buildStoredSchematic(world, pos.add(side.getFrontOffsetX(), side.getFrontOffsetY(), side.getFrontOffsetZ()), rotation);
        }
    }
}
