package com.InfinityRaider.settlercraft.render.block;

import com.InfinityRaider.settlercraft.block.BlockBase;
import com.InfinityRaider.settlercraft.block.tile.TileEntityBase;
import com.sun.istack.internal.NotNull;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class RenderBlockBase<T extends TileEntityBase> implements IBlockRenderingHandler<T> {
    private final BlockBase<T> block;
    private final T dummy;
    private final boolean inv;
    private final boolean statRender;
    private final boolean dynRender;

    protected RenderBlockBase(BlockBase<T> block, T te, boolean inv, boolean statRender, boolean dynRender) {
        this.block = block;
        this.dummy = te;
        this.inv = inv;
        this.statRender = statRender;
        this.dynRender = dynRender;
    }

    @Override
    public BlockBase<T> getBlock() {
        return block;
    }

    @Override
    @NotNull public T getTileEntity() {
        return dummy;
    }

    @Override
    public List<ResourceLocation> getAllTextures() {
        return getBlock().getTextures();
    }

    @Override
    public boolean doInventoryRendering() {
        return inv;
    }

    @Override
    public boolean hasDynamicRendering() {
        return dynRender;
    }

    @Override
    public boolean hasStaticRendering() {
        return statRender;
    }

    public final TextureAtlasSprite getIcon(ResourceLocation loc) {
        return ModelLoader.defaultTextureGetter().apply(loc);
    }
}
