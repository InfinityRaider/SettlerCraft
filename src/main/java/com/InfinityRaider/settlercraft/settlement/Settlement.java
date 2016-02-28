package com.InfinityRaider.settlercraft.settlement;

import com.InfinityRaider.settlercraft.api.v1.*;
import com.InfinityRaider.settlercraft.reference.Names;
import com.InfinityRaider.settlercraft.settlement.building.BuildingRegistry;
import com.InfinityRaider.settlercraft.settlement.building.BuildingTypeRegistry;
import com.InfinityRaider.settlercraft.utility.ChunkCoordinates;
import com.InfinityRaider.settlercraft.utility.LogHelper;
import com.InfinityRaider.settlercraft.utility.SettlementBoundingBox;
import com.InfinityRaider.settlercraft.utility.schematic.Schematic;
import com.InfinityRaider.settlercraft.utility.schematic.SchematicReader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Settlement implements ISettlement {
    private int id;
    private World world;
    private ChunkCoordinates homeChunk;
    private EntityPlayer player;
    private String name;
    private SettlementBoundingBox settlementBoundingBox;
    private HashMap<IBuildingType, List<ISettlementBuilding>> buildingsPerType;
    private List<ISettlementBuilding> tickingBuildings;

    private int populationCount;

    public Settlement(World world) {
        this.world = world;
        this.id = -1;
    }

    public Settlement(int id, World world, EntityPlayer player, BlockPos center, String name) {
        this(world);
        this.id = id;
        this.homeChunk = new ChunkCoordinates(world, center);
        this.player = player;
        this.name = name;
        this.settlementBoundingBox = new SettlementBoundingBox(center);
        this.resetBuildings();
        populationCount = 1;
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public World world() {
        return world;
    }

    @Override
    public Chunk homeChunk() {
        return homeChunk.getChunk();
    }

    @Override
    public EntityPlayer mayor() {
        return player;
    }

    @Override
    public boolean isMayor(EntityPlayer player) {
        return player != null && player.getUniqueID().equals(mayor().getUniqueID());
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public void rename(String name) {
        this.name = name;
    }

    @Override
    public List<ISettlementBuilding> getBuildings() {
        List<ISettlementBuilding> structuresList = new ArrayList<>();
        buildingsPerType.values().forEach(structuresList::addAll);
        return structuresList;
    }

    @Override
    public List<ISettlementBuilding> getBuildings(IBuildingType buildingType) {
        if(buildingsPerType.containsKey(buildingType)) {
            return buildingsPerType.get(buildingType);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public boolean hasBuilding(IBuilding building) {
        if(building == null) {
            return false;
        }
        for(ISettlementBuilding built : buildingsPerType.get(building.buildingType())) {
            if(built.building().equals(building) && built.isComplete()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<IBuilding> getBuildableBuildings() {
        List<IBuilding> list = new ArrayList<>();
        if(this.hasBuilding(BuildingRegistry.getInstance().TOWN_HALL_1)) {

        } else {
            list.add(BuildingRegistry.getInstance().TOWN_HALL_1);
        }
        return list;
    }

    @Override
    public boolean canBuildNewBuilding(BlockPos pos, IBuilding building, int rotation) {
        List<ISettlementBuilding> buildingsForType = buildingsPerType.get(building.buildingType());
        return buildingsForType.size() < building.buildingType().maximumBuildingCountPerSettlement(this) && doesBuildingFitInGrid(pos, rotation, building);
    }

    @Override
    public boolean canUpgradeOldBuilding(ISettlementBuilding oldStructure, IBuilding newStructure) {
        return newStructure.canBeUpgradedFromBuilding(oldStructure);
    }

    @Override
    public void buildNewBuilding(BlockPos pos, IBuilding building) {

    }

    @Override
    public void upgradeOldBuilding(ISettlementBuilding oldBuilding, IBuilding newBuilding) {

    }

    @Override
    public void addBuilding(ISettlementBuilding building) {
        if(building.building().needsUpdateTicks()) {
            tickingBuildings.add(building);
        }
        buildingsPerType.get(building.building().buildingType()).add(building);
    }

    @Override
    public void removeBuilding(ISettlementBuilding building) {
        if(building.building().needsUpdateTicks()) {
            tickingBuildings.remove(building);
        }
        buildingsPerType.get(building.building().buildingType()).remove(building);
    }

    @Override
    public void addInhabitant(ISettler settler) {
        if(settler.settlement() == null) {
            settler.setSettlement(this);
            this.populationCount = populationCount +1;
        }
    }

    @Override
    public List<ISettler> getSettlementInhabitants() {
        List<ISettler> list = new ArrayList<>();
        for(ISettlementBuilding building : getBuildings()) {
            list.addAll(building.inhabitants());
        }
        return list;
    }

    @Override
    public int population() {
        return populationCount;
    }

    @Override
    public boolean isWithinSettlementBounds(double x, double y, double z) {
        return settlementBoundingBox.isWithinBounds(x, y, z);
    }

    @Override
    public int xSize() {
        return settlementBoundingBox.xSize();
    }

    @Override
    public int ySize() {
        return settlementBoundingBox.ySize();
    }

    @Override
    public int zSize() {
        return settlementBoundingBox.zSize();
    }

    @Override
    public SettlementBoundingBox getBoundingBox() {
        return settlementBoundingBox;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        this.id = tag.getInteger(Names.NBT.SLOT);
        int[] homeCoords = tag.getIntArray(Names.NBT.HOME);
        this.homeChunk = new ChunkCoordinates(homeCoords[0], homeCoords[1], homeCoords[2]);
        int minX = tag.getInteger(Names.NBT.X);
        int minY = tag.getInteger(Names.NBT.Y);
        int minZ = tag.getInteger(Names.NBT.Z);
        int maxX = minX + tag.getInteger(Names.NBT.X_SIZE);
        int maxY = minY + tag.getInteger(Names.NBT.Y_SIZE);
        int maxZ = minZ + tag.getInteger(Names.NBT.Z_SIZE);
        this.settlementBoundingBox = new SettlementBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
        this.resetBuildings();
        NBTTagList list = tag.getTagList(Names.NBT.BUILDINGS, 10);
        for(int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tagAt = list.getCompoundTagAt(i);
            ISettlementBuilding building = tagAt.getBoolean(Names.NBT.COMPLETED) ? new SettlementBuildingComplete() : new SettlementBuildingIncomplete();
            building.readFromNBT(tagAt);
            this.addBuilding(building);
        }
    }

    @Override
    public NBTTagCompound writeToNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger(Names.NBT.SLOT, id);
        tag.setIntArray(Names.NBT.HOME, new int[] {homeChunk.x(), homeChunk.z(), homeChunk.dim()});
        BlockPos pos = settlementBoundingBox.getMinimumPosition();
        tag.setInteger(Names.NBT.X, pos.getX());
        tag.setInteger(Names.NBT.Y, pos.getY());
        tag.setInteger(Names.NBT.Z, pos.getZ());
        tag.setInteger(Names.NBT.X_SIZE, xSize());
        tag.setInteger(Names.NBT.Y_SIZE, ySize());
        tag.setInteger(Names.NBT.Z_SIZE, zSize());
        NBTTagList buildings = new NBTTagList();
        for(List<ISettlementBuilding> buildingForType : this.buildingsPerType.values())
        for(ISettlementBuilding building : buildingForType) {
            NBTTagCompound buildingTag = building.writeToNBT();
            buildingTag.setBoolean(Names.NBT.COMPLETED, building.isComplete());
            buildings.appendTag(buildingTag);
        }
        tag.setTag(Names.NBT.BUILDINGS, buildings);
        return tag;
    }

    private boolean doesBuildingFitInGrid(BlockPos pos, int rotation, IBuilding building) {
        SettlementBoundingBox newBox = null;
        try {
            Schematic schematic = SchematicReader.getInstance().deserialize(building.schematicLocation());
            newBox = schematic.getBoundingBox(pos, rotation);
        } catch (IOException e) {
            LogHelper.printStackTrace(e);
        }
        for(ISettlementBuilding built : getBuildings()) {
            if(built.isInsideBuilding(pos.getX(), pos.getY(), pos.getZ())) {
                return false;
            }
            if(newBox != null && newBox.intersects(built.getBoundingBox())) {
                return  false;
            }
        }
        return true;
    }

    private void resetBuildings() {
        this.buildingsPerType = new HashMap<>();
        for(IBuildingType type : BuildingTypeRegistry.getInstance().allBuildingTypes()) {
            buildingsPerType.put(type, new ArrayList<>());
        }
        tickingBuildings = new ArrayList<>();

    }

    @Override
    public void update() {
        for(ISettlementBuilding building : tickingBuildings) {
            building.building().onUpdateTick(building);
        }
    }
}
