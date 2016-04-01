package com.InfinityRaider.settlercraft.settlement.settler.profession.builder;

import com.InfinityRaider.settlercraft.api.v1.IBoundingBox;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.utility.schematic.Schematic;
import com.InfinityRaider.settlercraft.utility.schematic.SchematicRotationTransformer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;

import java.util.ArrayList;
import java.util.List;

public class StructureBuildProgress {
    private final World world;
    private final BlockPos origin;
    private final int rotation;
    private final Schematic schematic;

    private List<BlockPos> blocksToClear;

    private BlockBuildPosition[][][] blocksToBuild;
    private boolean[][][] progress;
    private List<BlockBuildPosition> finalBlocksToBuild;
    private List<BlockBuildPosition> finalBlocksBuilt;

    private List<ISettler> builders;

    private boolean completed;
    private boolean performCompletedCheck;

    public StructureBuildProgress(World world, BlockPos origin, Schematic schematic, int rotation) {
        this.world = world;
        this.origin = origin;
        this.rotation = rotation;
        this.schematic = schematic;
        this.init();
    }

    public void clearBlock(BlockPos pos) {
        blocksToClear.remove(pos);
        performCompletedCheck = true;
    }

    public boolean isComplete() {
        if(performCompletedCheck) {
            completed = checkCompletion();
        }
        return completed;
    }

    private boolean checkCompletion() {
        boolean flag = true;
        if (blocksToClear.size() > 0 || finalBlocksToBuild.size() > 0) {
            flag = false;
        } else {
            for (boolean[][] layer : progress) {
                for (boolean[] row : layer) {
                    for (boolean pos : row) {
                        if (!pos) {
                            flag = false;
                            break;
                        }
                    }
                }
            }
        }
        performCompletedCheck = false;
        return flag;
    }

    private void init() {
        IBoundingBox box = schematic.getBoundingBox(origin, rotation);
        blocksToClear = new ArrayList<>();
        blocksToBuild = new BlockBuildPosition[box.xSize()][box.ySize()][box.zSize()];
        finalBlocksToBuild = new ArrayList<>();
        finalBlocksBuilt = new ArrayList<>();
        this.builders = new ArrayList<>();
        progress = new boolean[box.xSize()][box.ySize()][box.zSize()];
        for(Schematic.BlockPosition position : schematic.blocks) {
            BlockPos coords = SchematicRotationTransformer.getInstance().applyRotation(new BlockPos(0, 0, 0), position.x, position.y, position.z, rotation);
            BlockBuildPosition toBuild = position.toBlockBuildPosition(world, origin, rotation);
            if(position.needsSupportBlock) {
                finalBlocksToBuild.add(toBuild);
            } else {
                blocksToBuild[coords.getX()][coords.getY()][coords.getZ()] = toBuild;
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
                    if(state.equals(blocksToBuild[x][y][z].getState())) {
                        //correct block is already here
                        progress[x][y][z] = true;
                        continue;
                    }
                    blocksToClear.add(pos);
                }
            }
        }
        this.checkCompletion();
    }
}
