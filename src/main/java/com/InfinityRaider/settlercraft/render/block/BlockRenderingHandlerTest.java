package com.InfinityRaider.settlercraft.render.block;

import com.InfinityRaider.settlercraft.block.BlockTest;
import com.InfinityRaider.settlercraft.block.ICustomRenderedBlock;
import com.InfinityRaider.settlercraft.render.tessellation.ITessellator;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockRenderingHandlerTest implements IBlockRenderingHandler<TileEntity> {
    private final BlockTest block;

    public BlockRenderingHandlerTest(BlockTest block) {
        this.block = block;
    }

    @Override
    public ICustomRenderedBlock<TileEntity> getCustomRenderedBlock() {
        return block;
    }

    @Override
    public BlockTest getBlock() {
        return block;
    }

    @Nullable
    @Override
    public TileEntity getTileEntity() {
        return null;
    }

    @Override
    public void renderWorldBlock(ITessellator tessellator, World world, BlockPos pos, double x, double y, double z, IBlockState state, Block block, @Nullable TileEntity tile, boolean dynamicRender, float partialTick, int destroyStage) {

    }

    @Override
    public TextureAtlasSprite getIcon() {
        return Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
    }

    @Override
    public boolean doInventoryRendering() {
        return false;
    }

    @Override
    public boolean hasDynamicRendering() {
        return true;
    }

    @Override
    public boolean hasStaticRendering() {
        return true;
    }
}
