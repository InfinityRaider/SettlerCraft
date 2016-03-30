package com.InfinityRaider.settlercraft.render.block;

import com.InfinityRaider.settlercraft.block.BlockBase;
import com.InfinityRaider.settlercraft.block.blockstate.IBlockStateSpecial;
import com.InfinityRaider.settlercraft.render.tessellation.CustomTessellator;
import com.InfinityRaider.settlercraft.render.tessellation.ITessellator;
import com.InfinityRaider.settlercraft.render.tessellation.VertexCreator;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class RenderBlockBase<T extends TileEntity> extends TileEntitySpecialRenderer<TileEntity> implements IBakedModel {
    protected RenderBlockBase(BlockBase<T> block, T tile) {
        if(this.hasDynamicRendering() && tile != null) {
            ClientRegistry.bindTileEntitySpecialRenderer(tile.getClass(), this);
        }
        if(this.hasStaticRendering()) {

        }
    }

    public abstract void renderBlockAt(ITessellator tessellator, World world, BlockPos pos, double x, double y, double z, IBlockState state,
                                       Block block, @Nullable T tile, boolean dynamicRender, float partialTick, int destroyStage);

    @Override
    @SuppressWarnings("unchecked")
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage) {
        ITessellator tessellator = CustomTessellator.getInstance(Tessellator.getInstance());
        World world = te.getWorld();
        BlockPos pos = te.getPos();
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        tessellator.startDrawingQuads(DefaultVertexFormats.BLOCK);
        tessellator.translate(x, y, z);

        this.renderBlockAt(tessellator, world, pos, x, y, z, state, block, (T) te, true, partialTicks, destroyStage);

        tessellator.translate(-x, -y, -z);
        tessellator.draw();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
        List<BakedQuad> list;
        if(state instanceof IBlockStateSpecial) {
            World world = Minecraft.getMinecraft().theWorld;
            T tile = ((IBlockStateSpecial<T>) state).getTileEntity(world);
            BlockPos pos = ((IBlockStateSpecial<T>) state).getPos();
            Block block = state.getBlock();
            ITessellator tessellator = VertexCreator.getInstance();

            tessellator.startDrawingQuads(DefaultVertexFormats.BLOCK);
            tessellator.translate(pos);

            this.renderBlockAt(tessellator, world, pos, pos.getX(), pos.getY(), pos.getZ(), state, block, tile, false, 1, 0);

            list = tessellator.getQuads();
            tessellator.draw();
        } else {
            list = new ArrayList<>();
        }
        return list;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return null;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return null;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return null;
    }

    protected abstract boolean hasDynamicRendering();

    protected abstract boolean hasStaticRendering();
}
