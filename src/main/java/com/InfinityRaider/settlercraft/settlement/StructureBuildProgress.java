package com.InfinityRaider.settlercraft.settlement;

import com.InfinityRaider.settlercraft.api.v1.ISettlementBuilding;
import com.InfinityRaider.settlercraft.utility.schematic.Schematic;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;

import java.util.ArrayList;
import java.util.List;

public class StructureBuildProgress {
    private ISettlementBuilding building;
    private BlockPos origin;
    private Schematic schematic;

    private StructureBuildPosition[][][] blocksToBuild;
    private StructureBuildPosition[][][] finalBlocksToBuild;

    private Work[][][] clearingWork;
    private Work[][][] buildingWork;

    private List<Work> currentWork;

    private boolean complete;
    private boolean needsCompletenessCheck;

    public StructureBuildProgress(ISettlementBuilding building, BlockPos clicked, Schematic schematic, int rotation) {
        this.building = building;
        this.schematic = schematic;
        this.init(clicked, rotation);
        this.buildWorkQueue();
    }

    public World getWorld() {
        return building.getWorld();
    }

    //there has to be a better way to do this...
    public Work getNextJob() {
        Work work = null;
        //first, look for the first clearing job
        int x = 0, y ,z;
        for (y = 0; y < clearingWork[x].length && work == null; y = y + 2) {
            for (x = 0; x < clearingWork.length && work == null; x++) {
                for (z = 0; z < clearingWork[x][y].length && work == null; z++) {
                    if (clearingWork[x][y][z] != null && !currentWork.contains(clearingWork[x][y][z])) {
                        work = clearingWork[x][y][z];
                    }
                    if (y + 1 < clearingWork[x].length && clearingWork[x][y+1][z] != null && !currentWork.contains(clearingWork[x][y+1][z])) {
                        work = clearingWork[x][y+1][z];
                    }
                }
            }
            x = 0;
        }
        //next, find the first building job
        x = 0;
        Work finalWork = null;
        for(y = 0; y < blocksToBuild[x].length && work == null; y++) {
            for(x = 0; x < blocksToBuild.length && work == null; x++) {
                for(z = 0; z < blocksToBuild[x][y].length && work == null; z++) {
                    if(buildingWork[x][y][z] != null && !currentWork.contains(buildingWork[x][y][z])) {
                        if(blocksToBuild[x][y][z] != null) {
                            work = buildingWork[x][y][z];
                        } else if(finalBlocksToBuild[x][y][z] != null) {
                            finalWork = buildingWork[x][y][z];
                        }
                    }
                }
            }
            x = 0;
        }
        work = work == null ? finalWork : work;
        if(work != null) {
            currentWork.add(work);
        } else if(currentWork.size() > 0) {
            work = currentWork.get(0);
        }
        return work;
    }

    public boolean validateJob(Work job) {
        return currentWork.contains(job);
    }

    public void cancelJob(Work job) {
        if(job != null) {
            currentWork.remove(job);
        }
    }

    public void doJob(Work work) {
        if(work != null) {
            work.doJob();
            currentWork.remove(work);
            needsCompletenessCheck = true;
        }
    }

    public boolean isComplete() {
        performCompletenessCheck();
        return complete;
    }

    private void performCompletenessCheck() {
        if (needsCompletenessCheck) {
            boolean wasComplete = complete;
            needsCompletenessCheck = false;
            for (int x = 0; x < blocksToBuild.length; x++) {
                for (int y = 0; y < blocksToBuild[x].length; y++) {
                    for (int z = 0; z < blocksToBuild[x][y].length; z++) {
                        if(clearingWork[x][y][z] != null || buildingWork[x][y][z] != null) {
                            complete = false;
                            return;
                        }
                    }
                }
            }
            complete = currentWork.size() <= 0;
            if(complete && !wasComplete) {
                this.building.onBuildingCompleted();
            }
        }
    }

    public void onBlockBroken(BlockPos pos) {
        int x = pos.getX() - origin.getX();
        int y = pos.getY() - origin.getY();
        int z = pos.getZ() - origin.getZ();
        if(x >= blocksToBuild.length || y >= blocksToBuild[x].length || z >= blocksToBuild[x][y].length) {
            return;
        }
        if(clearingWork[x][y][z] != null) {
            Work work = clearingWork[x][y][z];
            currentWork.remove(work);
            clearingWork[x][y][z] = null;
            needsCompletenessCheck = true;
        } else if(blocksToBuild[x][y][z] != null){
            buildingWork[x][y][z] = new Work.PlaceBlock(this, blocksToBuild[x][y][z]);
            complete = false;
            needsCompletenessCheck = false;
        } else if(finalBlocksToBuild[x][y][z] != null){
            buildingWork[x][y][z] = new Work.PlaceBlock(this, finalBlocksToBuild[x][y][z]);
            complete = false;
            needsCompletenessCheck = false;
        }
    }

    public void onBlockPlaced(BlockPos pos, IBlockState state) {
        int x = pos.getX() - origin.getX();
        int y = pos.getY() - origin.getY();
        int z = pos.getZ() - origin.getZ();
        if(x >= blocksToBuild.length || y >= blocksToBuild[x].length || z >= blocksToBuild[x][y].length) {
            return;
        }
        if(isAllowedState(state, blocksToBuild[x][y][z]) || isAllowedState(state, finalBlocksToBuild[x][y][z])) {
            if(buildingWork[x][y][z] != null) {
                currentWork.remove(buildingWork[x][y][z]);
                buildingWork[x][y][z] = null;
                needsCompletenessCheck = true;
            }
        } else if(clearingWork[x][y][z] == null) {
            clearingWork[x][y][z] = new Work.ClearBlock(this, pos);
            complete = false;
            needsCompletenessCheck = false;
        }
    }

    protected void clearBlock(BlockPos pos) {
        int x = pos.getX() - origin.getX();
        int y = pos.getY() - origin.getY();
        int z = pos.getZ() - origin.getZ();
        if(clearingWork[x][y][z] != null) {
            getWorld().setBlockToAir(pos);
        }
        clearingWork[x][y][z] = null;
        needsCompletenessCheck = true;
    }

    protected void placeBlock(StructureBuildPosition position) {
        int x = position.getPos().getX() - origin.getX();
        int y = position.getPos().getY() - origin.getY();
        int z = position.getPos().getZ() - origin.getZ();
        position.build();
        buildingWork[x][y][z] = null;
        needsCompletenessCheck = true;
    }

    private void init(BlockPos clicked, int rotation) {
        List<StructureBuildPosition> blocks = new ArrayList<>();
        List<StructureBuildPosition> finalBlocks = new ArrayList<>();
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE, maxZ = Integer.MIN_VALUE;
        for(Schematic.BlockPosition position : schematic.blocks) {
            StructureBuildPosition toBuild = StructureBuildPosition.fromSchematicData(getWorld(), clicked, rotation, position);
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
        this.blocksToBuild = new StructureBuildPosition[maxX - minX + 1][maxY - minY + 1][maxZ - minZ + 1];
        this.finalBlocksToBuild = new StructureBuildPosition[maxX - minX + 1][maxY - minY + 1][maxZ - minZ + 1];
        for(StructureBuildPosition pos : blocks) {
            blocksToBuild[pos.getPos().getX() - minX][pos.getPos().getY() - minY][pos.getPos().getZ() - minZ] = pos;
        }
        for(StructureBuildPosition pos : finalBlocks) {
            finalBlocksToBuild[pos.getPos().getX() - minX][pos.getPos().getY() - minY][pos.getPos().getZ() - minZ] = pos;
        }
    }

    private void buildWorkQueue() {
        needsCompletenessCheck = false;
        complete = true;
        this.currentWork = new ArrayList<>();
        clearingWork = new Work[blocksToBuild.length][blocksToBuild[0].length][blocksToBuild[0][0].length];
        buildingWork = new Work[blocksToBuild.length][blocksToBuild[0].length][blocksToBuild[0][0].length];
        for(int x = 0; x < blocksToBuild.length; x ++) {
            for(int y = 0; y < blocksToBuild[x].length; y++) {
                for(int z = 0; z < blocksToBuild[x][y].length; z ++) {
                    BlockPos pos = new BlockPos(x + origin.getX(), y + origin.getY(), z + origin.getZ());
                    IBlockState state = getWorld().getBlockState(pos);
                    Block block = state.getBlock();
                    boolean base = isAllowedState(state, blocksToBuild[x][y][z]);
                    boolean last = isAllowedState(state, finalBlocksToBuild[x][y][z]);
                    if(block == null || state.getMaterial() == Material.AIR || block instanceof BlockLiquid || block instanceof IFluidBlock) {
                        if(blocksToBuild[x][y][z] != null) {
                            if(!base) {
                                buildingWork[x][y][z] = new Work.PlaceBlock(this, blocksToBuild[x][y][z]);
                                complete = false;
                            }
                        } else if(finalBlocksToBuild[x][y][z] != null) {
                            if(!last) {
                                buildingWork[x][y][z] = new Work.PlaceBlock(this, finalBlocksToBuild[x][y][z]);
                                complete = false;
                            }
                        }
                    }
                    if(base || last) {
                        continue;
                    }
                    if(block != null && block.getBlockHardness(state, getWorld(), pos) < 0) {
                        //block is unbreakable
                        continue;
                    }
                    clearingWork[x][y][z] = new Work.ClearBlock(this, pos);
                    complete = false;
                }
            }
        }
    }

    protected boolean isAllowedState(IBlockState state, StructureBuildPosition mask) {
        if (mask == null) {
            return state == null || state.getMaterial() == Material.AIR;
        }
        return mask.isAllowedState(state);
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
                ItemStack resource = getResource();
                if(resource != null && resource.getItem() != null && !(resource.getItem() instanceof ItemBlock)) {
                    Item item = resource.getItem();
                    if(item instanceof ItemBucket) {
                        List<ItemStack> list = new ArrayList<>();
                        list.add(new ItemStack(Items.BUCKET));
                        return list;
                    } /*else if(item instanceof IFluidContainerItem) {
                        //TODO
                    }
                    */
                }
                return ImmutableList.of();
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
        }
    }
}
