package com.InfinityRaider.settlercraft.settlement.settler.profession.builder;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockBuildPosition {
    private World world;
    private BlockPos pos;
    private IBlockState state;
    private ItemStack resource;

    public BlockBuildPosition(World world, BlockPos pos, IBlockState state, ItemStack resource) {
        this.world = world;
        this.pos = pos;
        this.state = state;
        this.resource = resource;
    }

    public World getWorld() {
        return world;
    }

    public BlockPos getPos() {
        return pos;
    }

    public IBlockState getState() {
        return state;
    }

    public ItemStack getResource() {
        return resource;
    }

    public void build() {
        if (!getWorld().isRemote) {
            getWorld().setBlockState(getPos(), getState(), 3);
        }
    }

    public static class BlockBuildPositionTileEntity extends BlockBuildPosition {
        private NBTTagCompound tag;

        public BlockBuildPositionTileEntity(World world, BlockPos pos, IBlockState state, ItemStack resource, NBTTagCompound tag) {
            super(world, pos, state, resource);
            this.tag = tag;
        }

        public NBTTagCompound getTag() {
            return tag;
        }

        @Override
        public void build() {
            if (!getWorld().isRemote) {
                super.build();
                TileEntity tile = getWorld().getTileEntity(getPos());
                if (tile != null) {
                    tile.readFromNBT(getTag());
                }
            }
        }
    }
}
