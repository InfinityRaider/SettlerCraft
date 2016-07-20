package com.InfinityRaider.settlercraft.render.block;

import com.infinityraider.infinitylib.block.BlockBaseTile;
import com.infinityraider.infinitylib.block.tile.TileEntityBase;
import com.infinityraider.infinitylib.render.RenderUtilBase;
import com.infinityraider.infinitylib.render.block.IBlockRenderingHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class RenderBlockBase<T extends TileEntityBase> extends RenderUtilBase implements IBlockRenderingHandler<T> {
    private final BlockBaseTile<T> block;
    private final T dummy;
    private final boolean inv;
    private final boolean statRender;
    private final boolean dynRender;

    protected RenderBlockBase(BlockBaseTile<T> block, T te, boolean inv, boolean statRender, boolean dynRender) {
        this.block = block;
        this.dummy = te;
        this.inv = inv;
        this.statRender = statRender;
        this.dynRender = dynRender;
    }

    @Override
    public BlockBaseTile<T> getBlock() {
        return block;
    }

    @Override
    public T getTileEntity() {
        return dummy;
    }

    @Override
    public List<ResourceLocation> getAllTextures() {
        return Collections.emptyList();
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
}
