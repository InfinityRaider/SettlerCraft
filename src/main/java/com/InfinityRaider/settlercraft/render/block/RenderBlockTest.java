package com.InfinityRaider.settlercraft.render.block;

import com.InfinityRaider.settlercraft.block.BlockTest;
import com.InfinityRaider.settlercraft.block.tile.TileEntityTest;
import com.infinityraider.infinitylib.render.tessellation.ITessellator;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class RenderBlockTest extends RenderBlockBase<TileEntityTest> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "blocks/planks_oak");

    public RenderBlockTest(BlockTest block) {
        super(block, new TileEntityTest(), true, true, true);
    }

    @Override
    public void renderWorldBlock(ITessellator tessellator, World world, BlockPos pos, double x, double y, double z, IBlockState state, Block block,
                                 @Nullable TileEntityTest tile, boolean dynamicRender, float partialTick, int destroyStage) {
        this.doRender(tessellator, dynamicRender);
    }

    @Override
    public void renderInventoryBlock(ITessellator tessellator, World world, IBlockState state, Block block,
                                     @Nullable TileEntityTest tile, ItemStack stack, EntityLivingBase entity, ItemCameraTransforms.TransformType type) {
        this.doRender(tessellator, true);
        this.doRender(tessellator, false);
    }

    private void doRender(ITessellator tessellator, boolean dynamicRender) {
        tessellator.setApplyDiffuseLighting(true);
        if(!dynamicRender) {
            tessellator.setApplyDiffuseLighting(true);
            //tessellator.drawScaledFace(0, 0, 16, 16, EnumFacing.WEST, getIcon(), 0);
            tessellator.drawScaledPrism(0, 0, 0, 16, 16, 16, getIcon());
        } else {
            tessellator.setBrightness(15 << 24);
            //tessellator.drawScaledFace(0, 0, 16, 16, EnumFacing.WEST, getIcon(), 0);
            //tessellator.drawScaledPrism(0, 0, 0, 16, 16, 16, getIcon());
        }
    }

    @Override
    public TextureAtlasSprite getIcon() {
        return this.getIcon(TEXTURE);
    }
}
