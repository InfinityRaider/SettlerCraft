package com.InfinityRaider.settlercraft.block;

import com.InfinityRaider.settlercraft.render.block.BlockRenderingHandlerTest;
import com.InfinityRaider.settlercraft.render.block.IBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTest extends BlockBase<TileEntity> {
    public BlockTest() {
        super("testBlock", Material.ground, MapColor.adobeColor);
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
    public TileEntity getTileEntity(IBlockAccess world, BlockPos pos) {
        return null;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IBlockRenderingHandler<TileEntity> getRenderer() {
        return new BlockRenderingHandlerTest(this);
    }
}
