package com.InfinityRaider.settlercraft.block;

import com.InfinityRaider.settlercraft.block.blockstate.BlockStateSpecial;
import com.InfinityRaider.settlercraft.block.blockstate.IBlockStateSpecial;
import com.InfinityRaider.settlercraft.reference.Reference;
import com.InfinityRaider.settlercraft.registry.BlockRegistry;
import com.InfinityRaider.settlercraft.utility.RegisterHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockBase<T extends TileEntity> extends Block implements ICustomRenderedBlock<T> {
    private final String internalName;

    public BlockBase(String name, Material blockMaterial, MapColor blockMapColor) {
        super(blockMaterial, blockMapColor);
        this.internalName = name;
        this.setCreativeTab(BlockRegistry.getInstance().creativeTabSettlerCraft());
        RegisterHelper.registerBlock(this, this.getInternalName(), this.getItemBlockClass());
        BlockRegistry.getInstance().settlerCraftBlocks.add(this);
    }

    public String getInternalName() {
        return this.internalName;
    }

    @Override
    public IBlockStateSpecial<T, ? extends IBlockState> getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        return new BlockStateSpecial<>(state, pos, this.getTileEntity(world, pos));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelResourceLocation getBlockModelResourceLocation() {
        return new  ModelResourceLocation(Reference.MOD_ID.toLowerCase()+":"+getInternalName());
    }

    @Override
    protected final BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, getPropertyArray());
    }

    /**
     * @return a property array containing all properties for this block's state
     */
    protected abstract IProperty[] getPropertyArray();

    /**
     * Retrieves the block's ItemBlock class, as a generic class bounded by the
     * ItemBlock class.
     *
     * @return the block's class, may be null if no specific ItemBlock class is
     * desired.
     */
    protected abstract Class<? extends ItemBlock> getItemBlockClass();

    /**
     * @return The default bounding box for this block
     */
    public abstract AxisAlignedBB getDefaultBoundingBox();

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return getDefaultBoundingBox();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
}
