package com.InfinityRaider.settlercraft.settlement;

import com.InfinityRaider.settlercraft.api.v1.*;
import com.InfinityRaider.settlercraft.network.MessageSyncBuildingsToClient;
import com.InfinityRaider.settlercraft.reference.Names;
import com.InfinityRaider.settlercraft.settlement.building.BuildingInventory;
import com.InfinityRaider.settlercraft.settlement.building.BuildingRegistry;
import com.InfinityRaider.settlercraft.settlement.building.BuildingStyleRegistry;
import com.InfinityRaider.settlercraft.settlement.building.StructureBuildProgress;
import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import com.InfinityRaider.settlercraft.settlement.settler.profession.ProfessionRegistry;
import com.InfinityRaider.settlercraft.settlement.settler.profession.builder.TaskBuildBuilding;
import com.InfinityRaider.settlercraft.utility.BoundingBox;
import com.InfinityRaider.settlercraft.utility.LogHelper;
import com.InfinityRaider.settlercraft.utility.schematic.Schematic;
import com.InfinityRaider.settlercraft.utility.schematic.SchematicReader;
import com.InfinityRaider.settlercraft.utility.schematic.SchematicRotationTransformer;
import com.google.common.collect.ImmutableList;
import com.infinityraider.infinitylib.network.NetworkWrapper;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SettlementBuilding implements ISettlementBuilding {
    private int id;
    private ISettlement settlement;
    private IBuilding building;
    private BoundingBox boundingBox;
    private BlockPos origin;
    private BlockPos home;
    private int rotation;
    private StructureBuildProgress buildProgress;
    private BuildingInventory inventory;
    private List<Integer> inhabitants;
    private List<Integer> workers;
    private List<BlockPos> beds;

    public SettlementBuilding(ISettlement settlement) {
        this.settlement = settlement;
        this.inhabitants = new ArrayList<>();
        this.workers = new ArrayList<>();
        this.beds = new ArrayList<>();
        this.inventory = new BuildingInventory(this);
    }

    public SettlementBuilding(ISettlement settlement, int id, BlockPos pos, IBuilding building, Schematic schematic, int rotation) {
        this(settlement);
        this.id = id;
        this.building = building;
        this.boundingBox = schematic.getBoundingBox(pos, rotation);
        this.origin = pos;
        this.home = SchematicRotationTransformer.getInstance().applyRotation(pos, schematic.home[0], schematic.home[1], schematic.home[2], rotation);
        this.rotation = rotation;
        this.buildProgress = new StructureBuildProgress(this, pos, schematic, rotation);
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public IBuilding building() {
        return building;
    }

    @Override
    public boolean canDoWorkHere(ISettler settler) {
        if(settler == null) {
            return false;
        }
        if(settler.profession() == ProfessionRegistry.getInstance().professionBuilder()) {
            return settlement() != null
                    && !getBuildProgress().isComplete();
        }
        return building().canSettlerWorkHere(this, settler);
    }

    @Override
    public void addWorker(ISettler settler) {
        if(!getWorld().isRemote) {
            this.workers.add(settler.getEntityImplementation().getEntityId());
            if(settler.workPlace() != this) {
                settler.setWorkPlace(this);
            }
            this.markDirty();
            this.syncToClient();
        }
    }

    @Override
    public void removeWorker(ISettler settler) {
        if(!getWorld().isRemote && doesSettlerWorkHere(settler)) {
            Iterator<Integer> iterator = workers.iterator();
            while(iterator.hasNext()) {
                int id = iterator.next();
                if(id == settler.getEntityImplementation().getEntityId()) {
                    iterator.remove();
                    break;
                }
            }
            settler.setWorkPlace(null);
            this.markDirty();
            this.syncToClient();
        }
    }

    @Override
    public boolean canLiveHere(ISettler settler) {
        boolean complete = isComplete();
        boolean maxed = inhabitants.size() < building.maxInhabitants();
        boolean allowed = building.canSettlerLiveHere(this, settler);
        return isComplete() && inhabitants.size() < building.maxInhabitants() && building.canSettlerLiveHere(this, settler);
    }

    @Override
    public void addInhabitant(ISettler settler) {
        if(!getWorld().isRemote) {
            this.inhabitants.add(settler.getEntityImplementation().getEntityId());
            if(settler.home() != this) {
                settler.setHome(this);
            }
            this.markDirty();
            this.syncToClient();
        }
    }

    @Override
    public ITask getTaskForSettler(ISettler settler) {
        if(this.isComplete()) {
            return building().getTaskForSettler(this, settler);
        } else {
            StructureBuildProgress progress = getBuildProgress();
            return progress == null ? null : new TaskBuildBuilding(this.settlement(), settler, this, progress);
        }
    }

    @Override
    public List<EntitySettler> inhabitants() {
        List<EntitySettler> list = new ArrayList<>();
        for(int id : this.inhabitants) {
            Entity e = this.getWorld().getEntityByID(id);
            if(e != null && (e instanceof EntitySettler)) {
                list.add((EntitySettler) e);
            }
        }
        return list;
    }

    @Override
    public List<? extends ISettler> workers() {
        List<EntitySettler> list = new ArrayList<>();
        for(int id : this.workers) {
            Entity e = this.getWorld().getEntityByID(id);
            if(e != null && (e instanceof EntitySettler)) {
                list.add((EntitySettler) e);
            }
        }
        return list;
    }

    @Override
    public int workerCount() {
        return workers.size();
    }

    @Override
    public int inhabitantCount() {
        return inhabitants.size();
    }

    public void resetWorkersAndInhabitants() {
        if(!this.getWorld().isRemote) {
            this.workers = new ArrayList<>();
            this.inhabitants = new ArrayList<>();
            this.markDirty();
            this.syncToClient();
        }
    }

    @Override
    public boolean doesSettlerLiveHere(ISettler settler) {
        return settler != null && this.inhabitants.contains(settler.getEntityImplementation().getEntityId());
    }

    @Override
    public boolean doesSettlerWorkHere(ISettler settler) {
        return settler != null && this.workers.contains(settler.getEntityImplementation().getEntityId());
    }

    @Override
    public ISettlement settlement() {
        return settlement;
    }

    @Override
    public boolean isComplete() {
        StructureBuildProgress progress = getBuildProgress();
        return progress != null && progress.isComplete();
    }

    @Override
    public BuildingInventory inventory() {
        return inventory;
    }

    public World getWorld() {
        return this.settlement().world();
    }

    @Override
    public BlockPos position() {
        return getBoundingBox().getMinimumPosition();
    }

    @Override
    public BlockPos homePosition() {
        return home;
    }

    @Override
    public int sizeX() {
        return getBoundingBox().xSize();
    }

    @Override
    public int sizeY() {
        return getBoundingBox().ySize();
    }

    @Override
    public int sizeZ() {
        return getBoundingBox().zSize();
    }

    @Override
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    @Override
    public int getRotation() {
        return rotation;
    }

    @Override
    public boolean isInsideBuilding(double x, double y, double z) {
        return getBoundingBox().isWithinBounds(x, y, z);
    }

    @Override
    public List<BlockPos> getBeds() {
        return ImmutableList.copyOf(this.beds);
    }

    @Override
    public BlockPos getActualPosition(BlockPos pos) {
        return SchematicRotationTransformer.getInstance().applyRotation(origin, pos.getX(), pos.getY(), pos.getZ(), getRotation());
    }

    @Override
    public void onBuildingCompleted() {
        if(!getWorld().isRemote) {
            this.beds = new ArrayList<>();
            for (BlockPos pos : this.getBoundingBox()) {
                IBlockState state = this.getWorld().getBlockState(pos);
                TileEntity tile = this.getWorld().getTileEntity(pos);
                if ((state.getBlock() instanceof BlockBed) && (state.getValue(BlockBed.PART) == BlockBed.EnumPartType.HEAD)) {
                    this.beds.add(new BlockPos(pos));
                }
                if (tile != null && (tile instanceof IInventory)) {
                    this.inventory().registerInventory(new BlockPos(pos), (IInventory) tile);
                }
            }
            this.workers().stream().filter(settler -> settler.profession() == ProfessionRegistry.getInstance().professionBuilder()).forEach(settler -> {
                List<ITask> tasks = settler.getTasks(4);
                for(ITask task : tasks) {
                    settler.cancelTask(task);
                }
                settler.setMissingResource(null);
                settler.setWorkPlace(null);
            });
            this.building().onBuildingCompleted(this);
            this.markDirty();
            this.syncToClient();
        }
    }

    @Override
    public void onBlockBroken(EntityPlayer player, BlockPos pos, IBlockState state) {
        this.getBuildProgress().onBlockBroken(pos);
    }

    @Override
    public void onBlockPlaced(EntityPlayer player, BlockPos pos, IBlockState state) {
        this.getBuildProgress().onBlockPlaced(pos, state);
    }

    public StructureBuildProgress getBuildProgress() {
        if(buildProgress == null) {
            if(building() == null) {
                //should never happen
                LogHelper.info("[ERROR] SETTLEMENT BUILDING WITHOUT BUILDING DETECTED, WORLD DAMAGE PREVENTED");
                LogHelper.info("[ERROR] THIS IS A SERIOUS BUG, CONTACT THE MOD AUTHOR");
                return null;
            }
            Schematic schematic = deserializeSchematic();
            if(schematic != null) {
                this.buildProgress = new StructureBuildProgress(this, this.origin, schematic, this.getRotation());
            }
        }
        return this.buildProgress;
    }

    private Schematic deserializeSchematic() {
        try {
            return SchematicReader.getInstance().deserialize(BuildingStyleRegistry.getInstance().getSchematicLocation(building(), settlement().getBuildingStyle()));
        } catch (IOException e) {
            LogHelper.printStackTrace(e);
            return null;
        }
    }

    @Override
    public void markDirty() {
        this.settlement().markDirty();
    }

    @Override
    public void syncToClient() {
        MessageSyncBuildingsToClient msg = new MessageSyncBuildingsToClient(this);
        NetworkWrapper.getInstance().sendToDimension(msg, this.getWorld());
    }

    @Override
    public final NBTTagCompound readBuildingFromNBT(NBTTagCompound tag) {
        this.id = tag.getInteger(Names.NBT.SLOT);
        int minX = tag.getInteger(Names.NBT.X);
        int minY = tag.getInteger(Names.NBT.Y);
        int minZ = tag.getInteger(Names.NBT.Z);
        int maxX = minX + tag.getInteger(Names.NBT.X_SIZE) - 1;
        int maxY = minY + tag.getInteger(Names.NBT.Y_SIZE) - 1;
        int maxZ = minZ + tag.getInteger(Names.NBT.Z_SIZE) - 1;
        this.boundingBox = new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
        this.building = BuildingRegistry.getInstance().getBuildingFromName(tag.getString(Names.NBT.BUILDINGS));
        this.rotation = tag.getInteger(Names.NBT.ROTATION);
        this.readInventoryFromNBT(tag);
        this.home = new BlockPos(tag.getInteger(Names.NBT.X2), tag.getInteger(Names.NBT.Y2), tag.getInteger(Names.NBT.Z2));
        this.origin = new BlockPos(tag.getInteger(Names.NBT.X3), tag.getInteger(Names.NBT.Y3), tag.getInteger(Names.NBT.Z3));
        int[] ids = tag.getIntArray(Names.NBT.SETTLERS);
        this.inhabitants = new ArrayList<>();
        for(int id : ids) {
            inhabitants.add(id);
        }
        this.workers = new ArrayList<>();
        ids = tag.getIntArray(Names.NBT.WORKERS);
        for(int id : ids) {
            this.workers.add(id);
        }
        this.beds = new ArrayList<>();
        if(tag.hasKey(Names.NBT.SLEEPING)) {
            NBTTagList list = tag.getTagList(Names.NBT.SLEEPING, 10);
            int count = list.tagCount();
            for(int i = 0; i < count; i ++) {
                NBTTagCompound bedTag = list.getCompoundTagAt(i);
                int[] bedCoords = bedTag.getIntArray(Names.NBT.SLEEPING);
                if(bedCoords != null && bedCoords.length >= 3) {
                    this.beds.add(new BlockPos(bedCoords[0], bedCoords[1], bedCoords[2]));
                }
            }
        }
        return tag;
    }

    private void readInventoryFromNBT(NBTTagCompound tag) {
        if(!tag.hasKey(Names.NBT.INVENTORY)) {
            this.inventory = new BuildingInventory(this);
            return;
        }
        if(this.inventory == null) {
            this.inventory = new BuildingInventory(this);
        }
        this.inventory.readFromNBT(tag.getCompoundTag(Names.NBT.INVENTORY));
    }

    @Override
    public final NBTTagCompound writeBuildingToNBT(NBTTagCompound tag) {
        BlockPos pos = getBoundingBox().getMinimumPosition();
        tag.setInteger(Names.NBT.SLOT, id);
        tag.setInteger(Names.NBT.X, pos.getX());
        tag.setInteger(Names.NBT.Y, pos.getY());
        tag.setInteger(Names.NBT.Z, pos.getZ());
        tag.setInteger(Names.NBT.X_SIZE, sizeX());
        tag.setInteger(Names.NBT.Y_SIZE, sizeY());
        tag.setInteger(Names.NBT.Z_SIZE, sizeZ());
        tag.setString(Names.NBT.BUILDINGS, building.name());
        tag.setInteger(Names.NBT.ROTATION, rotation);
        this.writeInventoryToNBT(tag);
        tag.setInteger(Names.NBT.X2, home.getX());
        tag.setInteger(Names.NBT.Y2, home.getY());
        tag.setInteger(Names.NBT.Z2, home.getZ());
        tag.setInteger(Names.NBT.X3, origin.getX());
        tag.setInteger(Names.NBT.Y3, origin.getY());
        tag.setInteger(Names.NBT.Z3, origin.getZ());
        int[] inhabitants = new int[this.inhabitants.size()];
        for(int i = 0; i < inhabitants.length; i++) {
            inhabitants[i] = this.inhabitants.get(i);
        }
        tag.setIntArray(Names.NBT.SETTLERS, inhabitants);
        int[] workers = new int[this.workers.size()];
        for(int i = 0; i < workers.length; i++) {
            workers[i] = this.workers.get(i);
        }
        tag.setIntArray(Names.NBT.WORKERS, workers);
        NBTTagList bedList = new NBTTagList();
        for(BlockPos bedPos : this.getBeds()) {
            NBTTagCompound bedTag = new NBTTagCompound();
            bedTag.setIntArray(Names.NBT.SLEEPING, new int[] {bedPos.getX(), bedPos.getY(), bedPos.getZ()});
            bedList.appendTag(bedTag);
        }
        tag.setTag(Names.NBT.SLEEPING, bedList);
        return tag;
    }

    private void writeInventoryToNBT(NBTTagCompound tag) {
        if(this.inventory != null) {
            tag.setTag(Names.NBT.INVENTORY, this.inventory.writeToNBT());
        }
    }
}
