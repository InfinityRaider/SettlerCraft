package com.InfinityRaider.settlercraft.block;

import com.InfinityRaider.settlercraft.block.blockstate.IBlockStateSpecial;
import com.InfinityRaider.settlercraft.render.block.IBlockRenderingHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Implemented in a Block class to have special rendering handling for the block
 * @param <T> TileEntity class for this block, can be simple TileEntity if this block doesn't have a tile entity
 */
public interface ICustomRenderedBlock<T extends TileEntity> {
    /**
     * This is here to make sure a block state containing the tile entity and block position of the block are passed in the block's getActualState method
     * @param state the block's in world state
     * @param world the world
     * @param pos the block's position in the world
     * @return a special block state containing the tile entity and the position
     */
    @SuppressWarnings("unused")
    IBlockStateSpecial<T> getActualState(IBlockState state, IBlockAccess world, BlockPos pos);

    /**
     * This is here to make sure a block state containing the tile entity and block position of the block are passed in the block's getExtendedState method
     * @param state the block's in world state
     * @param world the world
     * @param pos the block's position in the world
     * @return a special block state containing the tile entity and the position
     */
    @SuppressWarnings("unused")
    IBlockStateSpecial<T> getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos);

    /**
     * Helper method to get a type specific tile entity, is cleaner than down casting
     * @param world the world
     * @param pos the position
     * @return tile entity for this bock
     */
    T getTileEntity(IBlockAccess world, BlockPos pos);

    /**
     * Gets called to create the IBlockRenderingHandler instance to render this block with
     * @return a new IBlockRenderingHandler object for this block
     */
    @SideOnly(Side.CLIENT)
    IBlockRenderingHandler<T> getRenderer();

    /**
     * Gets an array of ResourceLocations used for the model of this block, all block states for this block will use this as key in the model registry
     * @return a unique ModelResourceLocation for this block
     */
    @SideOnly(Side.CLIENT)
    ModelResourceLocation getBlockModelResourceLocation();
}
