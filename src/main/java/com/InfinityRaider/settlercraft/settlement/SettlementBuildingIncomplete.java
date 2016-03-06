package com.InfinityRaider.settlercraft.settlement;

import com.InfinityRaider.settlercraft.api.v1.IBuilding;
import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.reference.Names;
import com.InfinityRaider.settlercraft.utility.LogHelper;
import com.InfinityRaider.settlercraft.utility.schematic.Schematic;
import com.InfinityRaider.settlercraft.utility.schematic.SchematicReader;
import com.InfinityRaider.settlercraft.utility.schematic.SchematicRotationTransformer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SettlementBuildingIncomplete extends SettlementBuilding {
    private Schematic schematic;

    private List<BlockPos> blocksToClear;
    private List<ItemStack> neededResources;

    public SettlementBuildingIncomplete(World world) {
        super(world);
    }

    public SettlementBuildingIncomplete(ISettlement settlement, BlockPos pos, IBuilding building, Schematic schematic, int rotation) {
        super(settlement, schematic.getBoundingBox(pos, rotation), building, rotation, building.getStartingInventory());
        this.schematic = schematic;
        initBlocksToClear();
        initNeededResources();
    }

    @Override
    public boolean canDoWorkHere(ISettler settler) {
        return settler.profession() == null && (blocksToClear.size() > 0 || neededResources.size() > 0);
    }

    @Override
    public boolean canLiveHere(ISettler settler) {
        return false;
    }

    @Override
    public List<ISettler> inhabitants() {
        return new ArrayList<>();
    }

    @Override
    public BlockPos homePosition() {
        return SchematicRotationTransformer.getInstance().applyRotation(position(), schematic.home[0], schematic.home[1], schematic.home[2], getRotation());
    }

    @Override
    public boolean isComplete() {
        return false;
    }

    private void initBlocksToClear() {
        if(blocksToClear == null) {
            blocksToClear = new ArrayList<>();
            BlockPos min = getBoundingBox().getMinimumPosition();
            BlockPos max = getBoundingBox().getMaximumPosition();
            for(int x = min.getX(); x <= max.getX(); x++) {
                for(int y = min.getY(); y <= max.getY(); y++) {
                    for(int z = min.getZ(); z <= max.getZ(); z++) {
                        BlockPos pos = new BlockPos(x, y, z);
                        IBlockState state = settlement().world().getBlockState(pos);
                        Block block = state.getBlock();
                        if(block == null || block instanceof BlockAir) {
                            //no block here
                            continue;
                        }
                        if(block instanceof BlockLiquid || block instanceof IFluidBlock) {
                            //don't clean up liquids
                            continue;
                        }
                        if(block.getBlockHardness(settlement().world(), pos) < 0) {
                            //block is unbreakable
                            continue;
                        }
                        blocksToClear.add(pos);
                    }
                }
            }
        }
    }

    private void initNeededResources() {
        if (neededResources == null) {
            neededResources = new ArrayList<>();
            int index = 0;
            ItemStack stack = schematic.getItemStack(index);
            while (stack != null) {
                neededResources.add(stack);
                index++;
                stack = schematic.getItemStack(index);
            }
        }
    }


    @Override
    public void writeAdditionalDataToNBT(NBTTagCompound tag) {
        //blocks to clear
        NBTTagList blocksList = new NBTTagList();
        for(BlockPos pos : blocksToClear) {
            blocksList.appendTag(new NBTTagIntArray(new int[]{pos.getX(), pos.getY(), pos.getZ()}));
        }
        tag.setTag(Names.NBT.BLOCKS, blocksList);
        //needed resources
        NBTTagList stackList = new NBTTagList();
        for(ItemStack resource : neededResources) {
            stackList.appendTag(resource.writeToNBT(new NBTTagCompound()));
        }
        tag.setTag(Names.NBT.RESOURCES, stackList);
    }

    @Override
    public void readAdditionalDataFromNBT(NBTTagCompound tag) {
        //blocks to clear
        blocksToClear = new ArrayList<>();
        NBTTagList blockList = tag.getTagList(Names.NBT.BLOCKS, 11);
        for(int i = 0; i < blockList.tagCount(); i++) {
            int[] pos = blockList.getIntArrayAt(i);
            blocksToClear.add(new BlockPos(pos[0], pos[1], pos[2]));
        }
        //needed resources
        neededResources = new ArrayList<>();
        NBTTagList stackList = tag.getTagList(Names.NBT.BLOCKS, 10);
        for(int i = 0; i < stackList.tagCount(); i++) {
            neededResources.add(ItemStack.loadItemStackFromNBT(stackList.getCompoundTagAt(i)));
        }
        //schematic
        try {
            this.schematic = SchematicReader.getInstance().deserialize(building().schematicLocation());
        } catch (IOException e) {
            LogHelper.printStackTrace(e);
        }
    }
}
