package com.InfinityRaider.settlercraft.render.block;

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

import java.util.ArrayList;
import java.util.List;

public class RenderBlockBase<T extends TileEntity> extends TileEntitySpecialRenderer<TileEntity> implements IBakedModel {
    private final IBlockRenderingHandler<T> renderer;
    private final Block block;
    private final boolean inventory;

    public RenderBlockBase(IBlockRenderingHandler<T> renderer) {
        this.renderer = renderer;
        this.block = renderer.getBlock();
        this.inventory = renderer.doInventoryRendering();
        T tile = renderer.getTileEntity();
        if(renderer.hasDynamicRendering() && tile != null) {
            ClientRegistry.bindTileEntitySpecialRenderer(tile.getClass(), this);
        }
        if(renderer.hasStaticRendering()) {

        }
    }

    public Block getBlock() {
        return block;
    }

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

        this.renderer.renderWorldBlock(tessellator, world, pos, x, y, z, state, block, (T) te, true, partialTicks, destroyStage);

        tessellator.translate(-x, -y, -z);
        tessellator.draw();
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
