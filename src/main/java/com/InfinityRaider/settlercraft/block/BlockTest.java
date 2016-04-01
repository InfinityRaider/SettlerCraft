package com.InfinityRaider.settlercraft.block;

import com.InfinityRaider.settlercraft.block.tile.TileEntityTest;
import com.InfinityRaider.settlercraft.render.block.BlockRenderingHandlerTest;
import com.InfinityRaider.settlercraft.render.block.IBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTest extends BlockBaseTile<TileEntityTest> {
    public BlockTest() {
        super("testBlock", "test", Material.ground, MapColor.adobeColor);
    }

    @Override
    protected IProperty[] getPropertyArray() {
        return new IProperty[0];
    }

    @Override
    protected Class<? extends ItemBlock> getItemBlockClass() {
        return null;
    }

    @Override
    public AxisAlignedBB getDefaultBoundingBox() {
        return Block.FULL_BLOCK_AABB;
    }

    @Override
    public TileEntityTest getTileEntity(IBlockAccess world, BlockPos pos) {
        return (TileEntityTest) world.getTileEntity(pos);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IBlockRenderingHandler<TileEntityTest> getRenderer() {
        return new BlockRenderingHandlerTest(this);
    }

    @Override
    public TileEntityTest createNewTileEntity(World world, int meta) {
        return new TileEntityTest();
    }
}
