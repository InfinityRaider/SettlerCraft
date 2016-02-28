package com.InfinityRaider.settlercraft.settlement;

import com.InfinityRaider.settlercraft.api.v1.IBuilding;
import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.api.v1.ISettlementBuilding;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.reference.Names;
import com.InfinityRaider.settlercraft.settlement.building.BuildingRegistry;
import com.InfinityRaider.settlercraft.utility.SettlementBoundingBox;
import com.InfinityRaider.settlercraft.utility.schematic.SchematicRotationTransformer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;

public abstract class SettlementBuilding implements ISettlementBuilding {
    private ISettlement settlement;
    private SettlementBoundingBox boundingBox;
    private IBuilding building;
    private int rotation;
    private IInventory inventory;

    protected SettlementBuilding() {}

    public SettlementBuilding(ISettlement settlement, SettlementBoundingBox box, IBuilding building, int rotation, IInventory inventory) {
        this.settlement = settlement;
        this.boundingBox = box;
        this.building = building;
        this.rotation = rotation;
        this.inventory = inventory;
    }

    @Override
    public IBuilding building() {
        return building;
    }

    @Override
    public ISettlement settlement() {
        return settlement;
    }

    @Override
    public boolean canLiveHere(ISettler settler) {
        return inhabitants().size() < building.maxInhabitants();
    }

    @Override
    public IInventory inventory() {
        return inventory;
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
    public SettlementBoundingBox getBoundingBox() {
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
    public NBTTagCompound writeToNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        BlockPos pos = getBoundingBox().getMinimumPosition();
        tag.setInteger(Names.NBT.SETTLEMENT, settlement.id());
        tag.setInteger(Names.NBT.X, pos.getX());
        tag.setInteger(Names.NBT.Y, pos.getY());
        tag.setInteger(Names.NBT.Z, pos.getZ());
        tag.setInteger(Names.NBT.X_SIZE, sizeX());
        tag.setInteger(Names.NBT.Y_SIZE, sizeY());
        tag.setInteger(Names.NBT.Z_SIZE, sizeZ());
        tag.setString(Names.NBT.BUILDINGS, building.name());
        this.writeInventoryToNBT(tag);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        this.settlement = SettlementHandler.getInstance().getSettlement(tag.getInteger(Names.NBT.SETTLEMENT));
        int minX = tag.getInteger(Names.NBT.X);
        int minY = tag.getInteger(Names.NBT.Y);
        int minZ = tag.getInteger(Names.NBT.Z);
        int maxX = minX + tag.getInteger(Names.NBT.X_SIZE);
        int maxY = minY + tag.getInteger(Names.NBT.Y_SIZE);
        int maxZ = minZ + tag.getInteger(Names.NBT.Z_SIZE);
        this.boundingBox = new SettlementBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
        this.building = BuildingRegistry.getInstance().getBuildingFromName(tag.getString(Names.NBT.BUILDINGS));
        this.readInventoryFromNBT(tag);
    }

    private void writeInventoryToNBT(NBTTagCompound tag) {

    }

    private void readInventoryFromNBT(NBTTagCompound tag) {

    }
}
