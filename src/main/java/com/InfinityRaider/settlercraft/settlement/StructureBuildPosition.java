package com.InfinityRaider.settlercraft.settlement;

import com.InfinityRaider.settlercraft.utility.schematic.Schematic;
import com.InfinityRaider.settlercraft.utility.schematic.SchematicRotationTransformer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StructureBuildPosition {
    private World world;
    private BlockPos pos;
    private IBlockState state;
    private ItemStack resource;
    private boolean fuzzy;

    public StructureBuildPosition(World world, BlockPos pos, IBlockState state, ItemStack resource, boolean fuzzy) {
        this.world = world;
        this.pos = pos;
        this.state = state;
        this.resource = resource;
        this.fuzzy = fuzzy;
    }

    public World getWorld() {
        return world;
    }

    public BlockPos getPos() {
        return pos;
    }

    public IBlockState getState() {
        return state;
    }

    public boolean isFuzzy() {
        return fuzzy;
    }

    public ItemStack getResource() {
        return resource;
    }

    public boolean isAllowedState(IBlockState state) {
        return state != null
                && state.getBlock() == this.getState().getBlock()
                &&  ( this.isFuzzy() || (state.getBlock().getMetaFromState(state) == this.getState().getBlock().getMetaFromState(this.getState())) );
    }

    public void build() {
        if (!getWorld().isRemote) {
            BlockPos pos = getPos();
            IBlockState oldState = getWorld().getBlockState(pos);
            IBlockState newState = getState();
            getWorld().setBlockState(pos, newState, 3);
            getWorld().notifyBlockUpdate(pos, oldState, newState, 3);
        }
    }

    public static StructureBuildPosition fromSchematicData(World world, BlockPos origin, int rotation, Schematic.BlockPosition schematicData) {
        BlockPos pos = SchematicRotationTransformer.getInstance().applyRotation(origin, schematicData.x, schematicData.y, schematicData.z, rotation);
        IBlockState state = schematicData.getBlockState(rotation);
        ItemStack resource = schematicData.getResourceStack();
        boolean fuzzy = schematicData.fuzzy;
        NBTTagCompound tag = schematicData.getTag();
        if(tag != null && (state.getBlock() instanceof ITileEntityProvider)) {
            tag.setInteger("x", pos.getX());
            tag.setInteger("y", pos.getY());
            tag.setInteger("z", pos.getZ());
            TileEntity tile = ((ITileEntityProvider) state.getBlock()).createNewTileEntity(world, schematicData.worldMeta);
            SchematicRotationTransformer.getInstance().rotateTileTag(tile, tag, rotation);
            return new StructureBuildPositionTileEntity(world, pos, state, resource, fuzzy, tag);
        } else {
            return new StructureBuildPosition(world, pos, state, resource, fuzzy);
        }

    }

    public static class StructureBuildPositionTileEntity extends StructureBuildPosition {
        private NBTTagCompound tag;

        public StructureBuildPositionTileEntity(World world, BlockPos pos, IBlockState state, ItemStack resource, boolean fuzzy, NBTTagCompound tag) {
            super(world, pos, state, resource, fuzzy);
            this.tag = tag;
        }

        public NBTTagCompound getTag() {
            return tag;
        }

        @Override
        public void build() {
            if (!getWorld().isRemote) {
                super.build();
                TileEntity tile = getWorld().getTileEntity(getPos());
                if (tile != null) {
                    tile.readFromNBT(getTag());
                }
            }
        }
    }
}
