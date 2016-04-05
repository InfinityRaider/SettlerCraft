package com.InfinityRaider.settlercraft.settlement;

import com.InfinityRaider.settlercraft.utility.schematic.Schematic;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;

import java.util.ArrayList;
import java.util.List;

public class StructureBuildProgress {
    private World world;
    private BlockPos origin;
    private Schematic schematic;

    private BlockPos[][][] blocksToClear;
    private StructureBuildPosition[][][] blocksToBuild;
    private StructureBuildPosition[][][] finalBlocksToBuild;

    //TODO: priority system for jobs
    private List<Work> workQueue;

    public StructureBuildProgress(World world, BlockPos clicked, Schematic schematic, int rotation) {
        this.world = world;
        this.schematic = schematic;
        this.init(clicked, rotation);
        this.buildWorkQueue();
    }

    public World getWorld() {
        return world;
    }

    public Work getNextJob() {
        if(workQueue.size() <= 0) {
            return null;
        }
        Work job = workQueue.get(0);
        workQueue.remove(0);
        return job;
    }

    public void cancelJob(Work job) {
        workQueue.add(0, job);
    }

    public void doJob(Work work) {
        work.doJob();
    }

    protected void clearBlock(BlockPos pos) {
        world.setBlockToAir(pos);
        int x = pos.getX() - origin.getX();
        int y = pos.getY() - origin.getY();
        int z = pos.getZ() - origin.getZ();
        blocksToClear[x][y][z] = null;
    }

    protected void placeBlock(StructureBuildPosition position) {
        position.build();
    }

    public boolean isComplete() {
        return workQueue.size() <= 0;
    }

    private void init(BlockPos clicked, int rotation) {
        List<StructureBuildPosition> blocks = new ArrayList<>();
        List<StructureBuildPosition> finalBlocks = new ArrayList<>();
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE, maxZ = Integer.MIN_VALUE;
        for(Schematic.BlockPosition position : schematic.blocks) {
            StructureBuildPosition toBuild = StructureBuildPosition.fromSchematicData(world, clicked, rotation, position);
            if(position.needsSupportBlock) {
                finalBlocks.add(toBuild);
            } else {
                blocks.add(toBuild);
            }
            minX = Math.min(minX, toBuild.getPos().getX());
            minY = Math.min(minY, toBuild.getPos().getY());
            minZ = Math.min(minZ, toBuild.getPos().getZ());
            maxX = Math.max(maxX, toBuild.getPos().getX());
            maxY = Math.max(maxY, toBuild.getPos().getY());
            maxZ = Math.max(maxZ, toBuild.getPos().getZ());
        }
        this.origin = new BlockPos(minX, minY, minZ);
        this.blocksToClear = new BlockPos[maxX - minX + 1][maxY - minY + 1][maxZ - minZ + 1];
        this.blocksToBuild = new StructureBuildPosition[maxX - minX + 1][maxY - minY + 1][maxZ - minZ + 1];
        this.finalBlocksToBuild = new StructureBuildPosition[maxX - minX + 1][maxY - minY + 1][maxZ - minZ + 1];
        for(StructureBuildPosition pos : blocks) {
            blocksToBuild[pos.getPos().getX() - minX][pos.getPos().getY() - minY][pos.getPos().getZ() - minZ] = pos;
        }
        for(StructureBuildPosition pos : finalBlocks) {
            finalBlocksToBuild[pos.getPos().getX() - minX][pos.getPos().getY() - minY][pos.getPos().getZ() - minZ] = pos;
        }
        for(int x = minX; x <= maxX; x++) {
            for(int y = minY; y <= maxY; y++) {
                for(int z = minZ; z <= maxZ; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    IBlockState state = world.getBlockState(pos);
                    Block block = state.getBlock();
                    if(block == null || block instanceof BlockAir) {
                        //no block here
                        continue;
                    }
                    if(block instanceof BlockLiquid || block instanceof IFluidBlock) {
                        //don't clean up liquids
                        continue;
                    }
                    if(block.getBlockHardness(state, world, pos) < 0) {
                        //block is unbreakable
                        continue;
                    }
                    if(isSameState(state, blocksToBuild[x - minX][y - minY][z - minZ])) {
                        //correct block is already here
                        continue;
                    }
                    blocksToClear[x - minX][y - minY][z - minZ] = pos;
                }
            }
        }
    }

    private void buildWorkQueue() {
        this.workQueue = new ArrayList<>();
        //first clear out the blocks two layers at a time
        int x = 0, y ,z;
        for (y = 0; y < blocksToClear[x].length; y = y + 2) {
            for (x = 0; x < blocksToClear.length; x++) {
                for (z = 0; z < blocksToClear[x][y].length; z++) {
                    if (blocksToClear[x][y][z] != null) {
                        workQueue.add(new Work.ClearBlock(this, blocksToClear[x][y][z]));
                    }
                    if (y + 1 < blocksToClear[x].length && blocksToClear[x][y+1][z] != null) {
                        workQueue.add(new Work.ClearBlock(this, blocksToClear[x][y+1][z]));
                    }
                }
            }
            x = 0;
        }
        //build structure layer by layer
        x = 0;
        for(y = 0; y < blocksToBuild[x].length; y++) {
            for(x = 0; x < blocksToBuild.length; x++) {
                for(z = 0; z < blocksToBuild[x][y].length; z++) {
                    if(blocksToBuild[x][y][z] != null) {
                        IBlockState state = world.getBlockState(origin.add(x, y, z));
                        if(!isSameState(state, blocksToBuild[x][y][z])) {
                            workQueue.add(new Work.PlaceBlock(this, blocksToBuild[x][y][z]));
                        }
                    }
                }
            }
            x = 0;
        }
        //build final blocks which need a support block (e.g. torches, ...)
        x = 0;
        for(y = 0; y < finalBlocksToBuild[x].length; y++) {
            for(x = 0; x < finalBlocksToBuild.length; x++) {
                for(z = 0; z < finalBlocksToBuild[x][y].length; z++) {
                    if(finalBlocksToBuild[x][y][z] != null) {
                        workQueue.add(new Work.PlaceBlock(this, finalBlocksToBuild[x][y][z]));
                    }
                }
            }
            x = 0;
        }
    }

    public static abstract class Work {
        private StructureBuildProgress progress;

        private Work(StructureBuildProgress progress) {
            this.progress = progress;
        }

        public StructureBuildProgress getJob() {
            return progress;
        }

        public abstract BlockPos getWorkPos();

        protected abstract void doJob();

        public abstract ItemStack getResource();

        public abstract List<ItemStack> getGainedResources();

        public abstract String describeJob();

        public static class PlaceBlock extends Work {
            private StructureBuildPosition work;

            private PlaceBlock(StructureBuildProgress progress, StructureBuildPosition work) {
                super(progress);
                this.work = work;
            }

            @Override
            public BlockPos getWorkPos() {
                return work.getPos();
            }

            @Override
            protected void doJob() {
                getJob().placeBlock(work);
            }

            @Override
            public ItemStack getResource() {
                return work.getResource();
            }

            @Override
            public List<ItemStack> getGainedResources() {
                return ImmutableList.of();
            }

            @Override
            public String describeJob() {
                return "builder.buildingStructure";
            }
        }

        public static class ClearBlock extends Work {
            private BlockPos pos;

            private ClearBlock(StructureBuildProgress progress, BlockPos pos) {
                super(progress);
                this.pos = pos;
            }

            @Override
            public BlockPos getWorkPos() {
                return pos;
            }

            @Override
            protected void doJob() {
                getJob().clearBlock(pos);
            }

            @Override
            public ItemStack getResource() {
                return null;
            }

            @Override
            public List<ItemStack> getGainedResources() {
                List<ItemStack> list = new ArrayList<>();
                IBlockState state = getJob().getWorld().getBlockState(pos);
                list.add(state.getBlock().getPickBlock(state, null, getJob().getWorld(), pos, null));
                return list;
            }

            @Override
            public String describeJob() {
                return "builder.clearingBlocks";
            }
        }
    }

    public boolean isSameState(IBlockState a, StructureBuildPosition b) {
        if (b == null) {
            return a == null || a.getMaterial() == Material.air;
        }
        return a != null
                && a.getBlock() == b.getState().getBlock()
                && a.getBlock().getMetaFromState(a) == b.getState().getBlock().getMetaFromState(b.getState());
    }
}
