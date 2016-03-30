package com.InfinityRaider.settlercraft.block;

import com.InfinityRaider.settlercraft.block.blockstate.BlockStateSpecial;
import com.InfinityRaider.settlercraft.block.blockstate.IBlockStateSpecial;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public abstract class BlockBase<T extends TileEntity> extends Block implements ICustomRenderedBlock<T> {
    private final String internalName;

    public BlockBase(String name, Material blockMaterial, MapColor blockMapColor) {
        super(blockMaterial, blockMapColor);
        this.internalName = name;
    }

    public IBlockStateSpecial<T> getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        return new BlockStateSpecial<>(state, getTileEntity(world, pos));
    }
}
