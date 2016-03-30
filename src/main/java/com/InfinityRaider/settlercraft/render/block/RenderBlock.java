package com.InfinityRaider.settlercraft.render.block;

import com.InfinityRaider.settlercraft.block.ICustomRenderedBlock;
import com.InfinityRaider.settlercraft.block.blockstate.IBlockStateSpecial;
import com.InfinityRaider.settlercraft.render.tessellation.CustomTessellator;
import com.InfinityRaider.settlercraft.render.tessellation.ITessellator;
import com.InfinityRaider.settlercraft.render.tessellation.VertexCreator;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.opengl.GL11;

import java.util.List;

public final class RenderBlock<T extends TileEntity> extends TileEntitySpecialRenderer<TileEntity> implements IBakedModel {
    private final IBlockRenderingHandler<T> renderer;
    private final Block block;
    private final boolean inventory;

    public RenderBlock(IBlockRenderingHandler<T> renderer) {
        super();
        this.renderer = renderer;
        this.block = renderer.getBlock();
        this.inventory = renderer.doInventoryRendering();
        T tile = renderer.getTileEntity();
        if(renderer.hasDynamicRendering() && tile != null) {
            ClientRegistry.bindTileEntitySpecialRenderer(tile.getClass(), this);
        }
        if(renderer.hasStaticRendering()) {
            ModelLoader.getInstance().registerCustomBlockRenderer(this);
        }
    }

    public Block getBlock() {
        return block;
    }

    public IBlockRenderingHandler<T> getRenderer() {
        return renderer;
    }

    public ICustomRenderedBlock<T> getCustomRenderedBlock() {
        return getRenderer().getCustomRenderedBlock();
    }

    public ModelResourceLocation getModelResourceLocation() {
        return getCustomRenderedBlock().getBlockModelResourceLocation();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage) {
        ITessellator tessellator = CustomTessellator.getInstance(Tessellator.getInstance());
        World world = te.getWorld();
        BlockPos pos = te.getPos();
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);

        tessellator.startDrawingQuads(DefaultVertexFormats.BLOCK);
        this.renderer.renderWorldBlock(tessellator, world, pos, x, y, z, state, block, (T) te, true, partialTicks, destroyStage);
        tessellator.draw();

        GL11.glTranslated(-x, -y, -z);
        GL11.glPopMatrix();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
        List<BakedQuad> list;
        if(side == null && (state instanceof IBlockStateSpecial)) {
            World world = Minecraft.getMinecraft().theWorld;
            T tile = ((IBlockStateSpecial<T>) state).getTileEntity(world);
            BlockPos pos = ((IBlockStateSpecial<T>) state).getPos();
            Block block = state.getBlock();
            ITessellator tessellator = VertexCreator.getInstance();

            tessellator.startDrawingQuads(DefaultVertexFormats.BLOCK);
            tessellator.translate(pos);

            this.renderer.renderWorldBlock(tessellator, world, pos, pos.getX(), pos.getY(), pos.getZ(), state, block, tile, false, 1, 0);

            list = tessellator.getQuads();
            tessellator.draw();
        } else {
            list = ImmutableList.of();
        }
        return list;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return inventory;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return renderer.getIcon();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return null;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return null;
    }
}
