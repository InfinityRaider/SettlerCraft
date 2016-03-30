package com.InfinityRaider.settlercraft.block.blockstate;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IBlockStateSpecial<T extends TileEntity> extends IBlockState {
    T getTileEntity(IBlockAccess world);

    BlockPos getPos();
}
