package com.InfinityRaider.settlercraft.settlement;

import com.InfinityRaider.settlercraft.api.v1.*;
import com.InfinityRaider.settlercraft.reference.Names;
import com.InfinityRaider.settlercraft.settlement.building.BuildingRegistry;
import com.InfinityRaider.settlercraft.utility.AbstractEntityFrozen;
import com.InfinityRaider.settlercraft.utility.BoundingBox;
import com.InfinityRaider.settlercraft.utility.schematic.SchematicRotationTransformer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class SettlementBuilding extends AbstractEntityFrozen implements ISettlementBuilding {
    private int id;
    private int settlementId;
    private ISettlement settlement;
    private BoundingBox boundingBox;
    private IBuilding building;
    private int rotation;
    private IInventorySerializable inventory;

    public SettlementBuilding(World world) {
        super(world);
    }

    public SettlementBuilding(ISettlement settlement, BoundingBox box, IBuilding building, int rotation, IInventorySerializable inventory) {
        this(settlement.world());
        this.posX = box.minX() + 0.5;
        this.posY = box.minY() + 0.5;
        this.posZ = box.minZ() + 0.5;
        this.prevPosX = posX;
        this.prevPosY = posY;
        this.prevPosZ = posZ;
        this.id = -1;
        this.settlementId = settlement.id();
        this.settlement = settlement;
        this.boundingBox = box;
        this.building = building;
        this.rotation = rotation;
        this.inventory = inventory;
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
        this.writeAdditionalDataToNBT(tag);
        return tag;
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
        this.readAdditionalDataFromNBT(tag);
        return tag;
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
    protected void readDataFromNBT(NBTTagCompound tag) {
        this.readBuildingFromNBT(tag);
    }

    @Override
    protected void writeDataToNBT(NBTTagCompound tag) {
        this.writeBuildingToNBT(tag);
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
    public ISettlement settlement() {
        if(settlement == null) {
            this.settlement = SettlementHandler.getInstance().getSettlement(settlementId);
        }
        return settlement;
    }

    @Override
    public boolean canLiveHere(ISettler settler) {
        return inhabitants().size() < building.maxInhabitants();
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
        return SchematicRotationTransformer.getInstance().applyRotation(position(), pos.getX(), pos.getY(), pos.getZ(), getRotation());
    }

    @Override
    public void assignIdAndAddToWorld(int id) {
        if(!worldObj.isRemote && this.id < 0 && id >= 0) {
            this.id = id;
            worldObj.spawnEntityInWorld(this);
        }
    }

    private void writeInventoryToNBT(NBTTagCompound tag) {
        if(this.inventory != null) {
            tag.setTag(Names.NBT.INVENTORY, this.inventory.writeToNBT());
        }
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

    protected abstract void writeAdditionalDataToNBT(NBTTagCompound tag);

    protected abstract void readAdditionalDataFromNBT(NBTTagCompound tag);
}
