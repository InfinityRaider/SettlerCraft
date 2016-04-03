package com.InfinityRaider.settlercraft.settlement.settler.profession.builder;

import com.InfinityRaider.settlercraft.api.v1.IBoundingBox;
import com.InfinityRaider.settlercraft.utility.schematic.Schematic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
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
    private BlockBuildPosition[][][] blocksToBuild;
    private BlockBuildPosition[][][] finalBlocksToBuild;

    //TODO: priority system for jobs
    private List<Work> workQueue;

    public StructureBuildProgress(World world, BlockPos clicked, Schematic schematic, int rotation) {
        this.world = world;
        this.schematic = schematic;
        this.init(clicked, rotation);
        this.buildWorkQueue();
    }

    public Work getJob() {
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

    protected void placeBlock(BlockBuildPosition position) {
        position.build();
    }

    public boolean isComplete() {
        return workQueue.size() <= 0;
    }

    private void init(BlockPos clicked, int rotation) {
        IBoundingBox box = schematic.getBoundingBox(clicked, rotation);
        this.origin = box.getMinimumPosition();
        blocksToClear = new BlockPos[box.xSize()][box.ySize()][box.zSize()];
        blocksToBuild = new BlockBuildPosition[box.xSize()][box.ySize()][box.zSize()];
        finalBlocksToBuild = new BlockBuildPosition[box.xSize()][box.ySize()][box.zSize()];
        int dx = origin.getX();
        int dy = origin.getY();
        int dz = origin.getZ();
        for(Schematic.BlockPosition position : schematic.blocks) {
            BlockBuildPosition toBuild = BlockBuildPosition.fromSchematicData(world, clicked, rotation, position);
            if(position.needsSupportBlock) {
                finalBlocksToBuild[toBuild.getPos().getX() - dx][toBuild.getPos().getY() - dy][toBuild.getPos().getZ() - dz] = toBuild;
            } else {
                blocksToBuild[toBuild.getPos().getX() - dx][toBuild.getPos().getY() - dy][toBuild.getPos().getZ() - dz] = toBuild;
            }
        }
        BlockPos min = box.getMinimumPosition();
        BlockPos max = box.getMaximumPosition();
        for(int x = min.getX(); x <= max.getX(); x++) {
            for(int y = min.getY(); y <= max.getY(); y++) {
                for(int z = min.getZ(); z <= max.getZ(); z++) {
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
                    if(state.equals(blocksToBuild[x - min.getX()][y - min.getY()][z - min.getZ()].getState())) {
                        //correct block is already here
                        continue;
                    }
                    blocksToClear[x - min.getX()][y - min.getY()][z - min.getZ()] = pos;
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
                        if(!state.equals(blocksToBuild[x][y][z].getState())) {
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

        public abstract String describeJob();

        public static class PlaceBlock extends Work {
            private BlockBuildPosition work;

            private PlaceBlock(StructureBuildProgress progress, BlockBuildPosition work) {
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
            public String describeJob() {
                return "builder.clearingBlocks";
            }
        }
    }
}