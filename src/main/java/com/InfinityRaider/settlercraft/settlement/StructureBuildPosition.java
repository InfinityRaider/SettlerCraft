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

    public StructureBuildPosition(World world, BlockPos pos, IBlockState state, ItemStack resource) {
        this.world = world;
        this.pos = pos;
        this.state = state;
        this.resource = resource;
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

    public ItemStack getResource() {
        return resource;
    }

    public void build() {
        if (!getWorld().isRemote) {
            getWorld().setBlockState(getPos(), getState(), 3);
        }
    }

    public static StructureBuildPosition fromSchematicData(World world, BlockPos origin, int rotation, Schematic.BlockPosition schematicData) {
        BlockPos pos = SchematicRotationTransformer.getInstance().applyRotation(origin, schematicData.x, schematicData.y, schematicData.z, rotation);
        IBlockState state = schematicData.getBlockState(rotation);
        ItemStack resource = schematicData.getResourceStack();
        NBTTagCompound tag = schematicData.getTag();
        if(tag != null && (state.getBlock() instanceof ITileEntityProvider)) {
            tag.setInteger("x", pos.getX());
            tag.setInteger("y", pos.getY());
            tag.setInteger("z", pos.getZ());
            TileEntity tile = ((ITileEntityProvider) state.getBlock()).createNewTileEntity(world, schematicData.worldMeta);
            SchematicRotationTransformer.getInstance().rotateTileTag(tile, tag, rotation);
            return new StructureBuildPositionTileEntity(world, pos, state, resource, tag);
        } else {
            return new StructureBuildPosition(world, pos, state, resource);
        }

    }

    public static class StructureBuildPositionTileEntity extends StructureBuildPosition {
        private NBTTagCompound tag;

        public StructureBuildPositionTileEntity(World world, BlockPos pos, IBlockState state, ItemStack resource, NBTTagCompound tag) {
            super(world, pos, state, resource);
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
