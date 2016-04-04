package com.InfinityRaider.settlercraft.settlement;

import com.InfinityRaider.settlercraft.SettlerCraft;
import com.InfinityRaider.settlercraft.api.v1.*;
import com.InfinityRaider.settlercraft.reference.Names;
import com.InfinityRaider.settlercraft.settlement.building.BuildingRegistry;
import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import com.InfinityRaider.settlercraft.settlement.settler.profession.ProfessionRegistry;
import com.InfinityRaider.settlercraft.settlement.settler.profession.builder.TaskBuildBuilding;
import com.InfinityRaider.settlercraft.utility.AbstractEntityFrozen;
import com.InfinityRaider.settlercraft.utility.BoundingBox;
import com.InfinityRaider.settlercraft.utility.LogHelper;
import com.InfinityRaider.settlercraft.utility.schematic.Schematic;
import com.InfinityRaider.settlercraft.utility.schematic.SchematicReader;
import com.InfinityRaider.settlercraft.utility.schematic.SchematicRotationTransformer;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SettlementBuilding extends AbstractEntityFrozen implements ISettlementBuilding {
    private int id;
    private int settlementId;
    private ISettlement settlement;
    private IBuilding building;
    private BoundingBox boundingBox;
    private BlockPos origin;
    private BlockPos home;
    private int rotation;
    private StructureBuildProgress buildProgress;
    private IInventorySerializable inventory;
    private List<EntitySettler> inhabitants;

    public SettlementBuilding(World world) {
        super(world);
        this.inhabitants = new ArrayList<>();
    }

    public SettlementBuilding(ISettlement settlement, BlockPos pos, IBuilding building, Schematic schematic, int rotation) {
        this(settlement.world());
        this.id = -1;
        this.settlementId = settlement.id();
        this.settlement = settlement;
        this.building = building;
        this.boundingBox = schematic.getBoundingBox(pos, rotation);
        this.origin = pos;
        this.home = SchematicRotationTransformer.getInstance().applyRotation(pos, schematic.home[0], schematic.home[1], schematic.home[2], rotation);
        this.rotation = rotation;
        this.buildProgress = new StructureBuildProgress(getWorld(), pos, schematic, rotation);
        this.inventory = building.getDefaultInventory();
        this.posX = this.prevPosX = this.home.getX() + 0.5;
        this.posY = this.prevPosY = this.home.getY() + 0.5;
        this.posZ = this.prevPosZ = this.home.getZ() + 0.5;
    }

    @Override
    protected String name() {
        return building().name();
    }

    @Override
    protected void update() {
        if(isComplete() && building() != null && building().needsUpdateTicks()) {
            building().onUpdateTick(this);
        }
    }

    @Override
    protected void onEntitySpawned() {
        ISettlement settlement = this.settlement();
        if(settlement == null) {
            SettlementHandler.getInstance().addBuildingToBuffer(settlementId, this);
        } else {
            settlement.onBuildingUpdated(this);
        }
    }

    @Override
    protected void onChunkLoaded() {
        onEntitySpawned();
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
        if(settler.profession() == ProfessionRegistry.getInstance().BUILDER) {
            return settlement() != null
                    && settler.profession() == ProfessionRegistry.getInstance().BUILDER
                    && !getBuildProgress().isComplete();
        }
        return building().canSettlerWorkHere(this, settler);
    }

    @Override
    public ITask getTaskForSettler(ISettler settler) {
        if(this.isComplete()) {
            return building().getTaskForSettler(this, settler);
        } else {
            return new TaskBuildBuilding(this.settlement(), settler, this, this.getBuildProgress());
        }
    }

    @Override
    public List<? extends ISettler> inhabitants() {
        return inhabitants;
    }

    @Override
    public ISettlement settlement() {
        if(settlement == null) {
            this.settlement = SettlementHandler.getInstance().getSettlement(settlementId);
        }
        return settlement;
    }

    @Override
    public boolean canLiveHere(ISettler settler) {
        return isComplete() && inhabitants().size() < building.maxInhabitants();
    }

    @Override
    public boolean isComplete() {
        return getBuildProgress().isComplete();
    }

    @Override
    public IInventorySerializable inventory() {
        return inventory;
    }

    public World getWorld() {
        return settlement().world();
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
    public boolean isInsideBuilding(int x, int y, int z) {
        return getBoundingBox().isWithinBounds(x, y, z);
    }

    @Override
    public BlockPos getActualPosition(BlockPos pos) {
        return SchematicRotationTransformer.getInstance().applyRotation(origin, pos.getX(), pos.getY(), pos.getZ(), getRotation());
    }

    @Override
    public void assignIdAndAddToWorld(int id) {
        if(!worldObj.isRemote && this.id < 0 && id >= 0) {
            this.id = id;
            worldObj.spawnEntityInWorld(this);
        }
    }

    public StructureBuildProgress getBuildProgress() {
        if(buildProgress == null) {
            Schematic schematic = deserializeSchematic();
            if(schematic != null) {
                this.buildProgress = new StructureBuildProgress(this.getWorld(), this.origin, schematic, this.getRotation());
            }
        }
        return this.buildProgress;
    }

    private Schematic deserializeSchematic() {
        try {
            return SchematicReader.getInstance().deserialize(building().schematicLocation());
        } catch (IOException e) {
            LogHelper.printStackTrace(e);
            return null;
        }
    }

    @Override
    protected void readDataFromNBT(NBTTagCompound tag) {
        this.readBuildingFromNBT(tag);
    }

    @Override
    public final NBTTagCompound readBuildingFromNBT(NBTTagCompound tag) {
        this.id = tag.getInteger(Names.NBT.SLOT);
        this.settlementId = tag.getInteger(Names.NBT.SETTLEMENT);
        this.settlement = SettlementHandler.getInstance().getSettlement(this.settlementId);
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
        for(int id : ids) {
            Entity entity = SettlerCraft.proxy.getEntityById(settlement().world(), id);
            if(entity != null && entity instanceof EntitySettler) {
                inhabitants.add((EntitySettler) entity);
            }
        }
        return tag;
    }

    private void readInventoryFromNBT(NBTTagCompound tag) {
        if(!tag.hasKey(Names.NBT.INVENTORY)) {
            this.inventory = null;
            return;
        }
        if(this.inventory == null) {
            this.inventory = this.building.getDefaultInventory();
        }
        this.inventory.readFromNBT(tag.getCompoundTag(Names.NBT.INVENTORY));
    }

    @Override
    protected void writeDataToNBT(NBTTagCompound tag) {
        this.writeBuildingToNBT(tag);
    }

    @Override
    public final NBTTagCompound writeBuildingToNBT(NBTTagCompound tag) {
        BlockPos pos = getBoundingBox().getMinimumPosition();
        tag.setInteger(Names.NBT.SLOT, id);
        tag.setInteger(Names.NBT.SETTLEMENT, settlementId);
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
        tag.setInteger(Names.NBT.Y2, origin.getY());
        tag.setInteger(Names.NBT.Z2, origin.getZ());
        int[] settlers = new int[inhabitants.size()];
        for(int i = 0; i < settlers.length; i++) {
            settlers[i] = inhabitants.get(i).getEntityId();
        }
        tag.setIntArray(Names.NBT.SETTLERS, settlers);
        return tag;
    }

    private void writeInventoryToNBT(NBTTagCompound tag) {
        if(this.inventory != null) {
            tag.setTag(Names.NBT.INVENTORY, this.inventory.writeToNBT());
        }
    }
}
