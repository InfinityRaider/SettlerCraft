package com.InfinityRaider.settlercraft.utility.schematic;

import com.InfinityRaider.settlercraft.utility.LogHelper;
import com.InfinityRaider.settlercraft.utility.SettlementBoundingBox;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Schematic {
    public int groundLevel;
    public int[] home;
    public List<BlockPosition> blocks;

    public Schematic(List<BlockPosition> blocks, int ground, int[] home) {
        this.blocks = blocks;
        this.groundLevel = ground;
        this.home = home;
    }

    public ItemStack getItemStack(int index) {
        if(index < 0 || index >= blocks.size()) {
            return null;
        }
        BlockPosition position = blocks.get(index);
        Block block = Block.blockRegistry.getObject(new ResourceLocation(position.block));
        return new ItemStack(block, 1, position.stackMeta);
    }

    public Map<BlockPos, IBlockState> getBlockStateMap() {
        Map<BlockPos, IBlockState> map = new HashMap<>();
        for(BlockPosition position : blocks) {
            Block block = position.getBlock();
            if(block == null) {
                continue;
            }
            map.put(position.getBlockPos(), block.getStateFromMeta(position.worldMeta));
        }
        return map;
    }

    public Map<BlockPos, TileEntity> getTileEntityMap(World world) {
        Map<BlockPos, TileEntity> map = new HashMap<>();
        for(BlockPosition position : blocks) {
            Block block = position.getBlock();
            if(block == null) {
                continue;
            }
            if(block instanceof ITileEntityProvider) {
                TileEntity tile = ((ITileEntityProvider) block).createNewTileEntity(world, position.worldMeta);
                NBTTagCompound tag = position.getTag();
                if(tag == null) {
                    tag = new NBTTagCompound();
                }
                tag.setInteger("x", position.x);
                tag.setInteger("y", position.y);
                tag.setInteger("z", position.z);
                tile.readFromNBT(tag);
                map.put(position.getBlockPos(), tile);
            }
        }

        return map;
    }

    public SettlementBoundingBox getBoundingBox(BlockPos start, int rotation) {
        int maxX = 0;
        int maxY = 0;
        int maxZ = 0;
        for(BlockPosition blockPosition : blocks) {
            maxX = Math.max(maxX, blockPosition.x);
            maxY = Math.max(maxY, blockPosition.y);
            maxZ = Math.max(maxZ, blockPosition.z);
        }
        BlockPos max = new BlockPos(maxX, maxY, maxZ);
        return (new SettlementBoundingBox(start, max)).rotate(rotation);
    }

    public static class BlockPosition {
        public int x;
        public int y;
        public int z;
        public String block;
        public int worldMeta;
        public int stackMeta;
        public boolean needsSupportBlock;
        public int[] rotationMetaTransform;
        public String nbtString;

        public BlockPosition(int x, int y, int z, String block, int worldMeta, int stackMeta, NBTTagCompound tag) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.block = block;
            this.worldMeta = worldMeta;
            this.needsSupportBlock = false;
            this.nbtString = tag != null ? tag.toString() : null;
            this.stackMeta = stackMeta;
        }

        public boolean needsSupportBlock() {
            return needsSupportBlock;
        }

        public BlockPosition setNeedsSupportBlock() {
            this.needsSupportBlock = true;
            return this;
        }

        public BlockPosition setRotationMetaTransforms(int[] meta) {
            if(meta != null && meta.length == 4) {
                this.rotationMetaTransform = meta;
            }
            return this;
        }

        public int getWorldMeta(int rotation) {
            if(this.rotationMetaTransform != null) {
                for (int i = 0; i < rotationMetaTransform.length; i++) {
                    if (rotationMetaTransform[i] == this.worldMeta) {
                        return rotationMetaTransform[(i + rotation) % rotationMetaTransform.length];
                    }
                }
            }
            return this.worldMeta;
        }

        public Block getBlock() {
            return Block.blockRegistry.getObject(new ResourceLocation(this.block));
        }

        public BlockPos getBlockPos() {
            return new BlockPos(x, y, z);
        }

        public NBTTagCompound getTag() {
            if(nbtString == null) {
                return null;
            }
            try {
                return JsonToNBT.getTagFromJson(nbtString);
            } catch (NBTException e) {
                LogHelper.printStackTrace(e);
            }
            return null;
        }
    }
}
