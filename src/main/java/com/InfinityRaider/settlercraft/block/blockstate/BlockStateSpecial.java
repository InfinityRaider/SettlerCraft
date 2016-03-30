package com.InfinityRaider.settlercraft.block.blockstate;

import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockStateSpecial<T extends TileEntity> extends BlockStateContainer.StateImplementation implements IBlockStateSpecial<T> {
    private final T tile;
    private final BlockPos pos;

    public BlockStateSpecial(IBlockState state, BlockPos pos, T tile) {
        super(state.getBlock(), state.getProperties());
        this.tile = tile;
        this.pos = pos;
    }

    @Override
    public T getTileEntity(IBlockAccess world) {
        return tile;
    }

    @Override
    public BlockPos getPos() {
        return pos;
    }
}
