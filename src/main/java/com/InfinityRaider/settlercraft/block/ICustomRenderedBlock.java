package com.InfinityRaider.settlercraft.block;

import com.InfinityRaider.settlercraft.block.blockstate.IBlockStateSpecial;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface ICustomRenderedBlock<T extends TileEntity> {
    IBlockStateSpecial<T> getActualState(IBlockState state, IBlockAccess world, BlockPos pos);

    T getTileEntity(IBlockAccess world, BlockPos pos);
}
