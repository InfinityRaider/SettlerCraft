package com.InfinityRaider.settlercraft.block;

import com.InfinityRaider.settlercraft.block.tile.TileEntityTest;
import com.InfinityRaider.settlercraft.reference.Reference;
import com.InfinityRaider.settlercraft.render.block.RenderBlockTest;
import com.infinityraider.infinitylib.block.BlockTileCustomRenderedBase;
import com.infinityraider.infinitylib.render.block.IBlockRenderingHandler;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.List;

public class BlockTest extends BlockTileCustomRenderedBase<TileEntityTest> {
    public BlockTest() {
        super("testBlock", Material.GROUND);
    }

    @Override
    public List<String> getOreTags() {
        return Collections.emptyList();
    }

    @Override
    protected IProperty[] getPropertyArray() {
        return new IProperty[0];
    }

    @Override
    public Class<? extends ItemBlock> getItemBlockClass() {
        return null;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IBlockRenderingHandler<TileEntityTest> getRenderer() {
        return new RenderBlockTest(this);
    }

    @Override
    public ModelResourceLocation getBlockModelResourceLocation() {
        return new ModelResourceLocation(Reference.MOD_ID.toLowerCase() + ":" + getInternalName());
    }

    @Override
    public TileEntityTest createNewTileEntity(World world, int meta) {
        return new TileEntityTest();
    }
}
