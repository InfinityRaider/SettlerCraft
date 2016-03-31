package com.InfinityRaider.settlercraft.render.block;

import com.InfinityRaider.settlercraft.block.BlockTest;
import com.InfinityRaider.settlercraft.block.tile.TileEntityTest;
import com.InfinityRaider.settlercraft.render.tessellation.ITessellator;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockRenderingHandlerTest implements IBlockRenderingHandler<TileEntityTest> {
    private final BlockTest block;

    public BlockRenderingHandlerTest(BlockTest block) {
        this.block = block;
    }

    @Override
    public BlockTest getBlock() {
        return block;
    }

    @Nullable
    @Override
    public TileEntityTest getTileEntity() {
        return new TileEntityTest();
    }

    @Override
    public void renderWorldBlock(ITessellator tessellator, World world, BlockPos pos, double x, double y, double z, IBlockState state, Block block, @Nullable TileEntityTest tile, boolean dynamicRender, float partialTick, int destroyStage) {
        if(!dynamicRender) {
            tessellator.drawScaledPrism(3, 2, 3, 9, 16, 9, null);
            //tessellator.drawScaledPrism(1, 1, 1, 16, 16, 32, null);
            tessellator.drawScaledFaceDouble(1, 1, 15, 15, EnumFacing.NORTH, null, -5);
        }
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
