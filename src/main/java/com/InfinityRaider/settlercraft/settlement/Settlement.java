package com.InfinityRaider.settlercraft.settlement;

import com.InfinityRaider.settlercraft.api.v1.*;
import com.InfinityRaider.settlercraft.reference.Names;
import com.InfinityRaider.settlercraft.settlement.building.BuildingStyleRegistry;
import com.InfinityRaider.settlercraft.settlement.building.BuildingTypeRegistry;
import com.InfinityRaider.settlercraft.utility.BoundingBox;
import com.InfinityRaider.settlercraft.utility.LogHelper;
import com.InfinityRaider.settlercraft.utility.schematic.Schematic;
import com.InfinityRaider.settlercraft.utility.schematic.SchematicReader;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Settlement implements ISettlement {
    private static final int BUILDING_CLEARANCE = 5;
    //Settlement data
    private int id;
    private SettlementWorldData worldData;
    private Chunk homeChunk;
    private UUID mayorId;
    private String name;
    private BoundingBox boundingBox;
    private IBuildingStyle style;
    //Settler data
    private int populationCount;
    //Building data
    private Map<Integer, ISettlementBuilding> buildings;
    private Map<IBuildingType, List<ISettlementBuilding>> buildingsPerType;

    public Settlement(SettlementWorldData worldData) {
        this.worldData = worldData;
        this.resetBuildings();
    }

    public Settlement(SettlementWorldData worldData, int id, EntityPlayer player, BlockPos center, String name, IBuildingStyle style) {
        this(worldData);
        this.id = id;
        this.homeChunk = this.world().getChunkFromChunkCoords(center.getX(), center.getZ());
        this.mayorId = player.getUniqueID();
        this.name = name;
        this.boundingBox = new BoundingBox(center.add(0, -1, 0));
        this.style = style;
    }

    @Override
    public int id() {
        return this.id;
    }

    @Override
    public World world() {
        return this.worldData.getWorld();
    }

    @Override
    public Chunk homeChunk() {
        return this.homeChunk;
    }

    @Override
    public EntityPlayer mayor() {
        return this.world().getPlayerEntityByUUID(this.mayorId);
    }

    @Override
    public boolean isMayor(EntityPlayer player) {
        return player == this.mayor();
    }

    @Override
    public int tier() {
        List<ISettlementBuilding> townHalls = this.getCompletedBuildings(BuildingTypeRegistry.getInstance().buildingTypeTownHall());
        if(townHalls.size() <= 0) {
            return 0;
        }
        //this is safe, see: BuildingTypeTowHall.addNewBuilding(IBuilding building)
        return ((IBuildingTownHall) townHalls.get(0).building()).getTier();
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public void rename(String name) {
        if(!world().isRemote) {
            this.name = name;
            this.worldData.markSettlementDirty(this);
        }
    }

    @Override
    public IBuildingStyle getBuildingStyle() {
        return this.style;
    }

    @Override
    public ISettlementBuilding getBuildingFromId(int id) {
        return buildings.get(id);
    }

    @Override
    public ISettlementBuilding getBuildingForLocation(double x, double y, double z) {
        if(!this.getBoundingBox().isWithinBounds(x, y ,z)) {
            return null;
        }
        for (ISettlementBuilding building : getBuildings()) {
            if(building.getBoundingBox().isWithinBounds(x, y, z)) {
                return building;
            }
        }
        return null;
    }

    @Override
    public List<ISettlementBuilding> getBuildings() {
        List<ISettlementBuilding> structuresList = new ArrayList<>();
        buildingsPerType.values().forEach(structuresList::addAll);
        return structuresList;
    }

    @Override
    public List<ISettlementBuilding> getCompletedBuildings() {
        List<ISettlementBuilding> structuresList = new ArrayList<>();
        for(List<ISettlementBuilding> buildings : buildingsPerType.values()) {
            structuresList.addAll(buildings.stream().filter(ISettlementBuilding::isComplete).collect(Collectors.toList()));
        }
        return structuresList;
    }

    @Override
    public List<ISettlementBuilding> getBuildings(IBuildingType buildingType) {
        if(buildingsPerType.containsKey(buildingType)) {
            return buildingsPerType.get(buildingType);
        } else {
            return ImmutableList.of();
        }
    }

    @Override
    public List<ISettlementBuilding> getCompletedBuildings(IBuildingType buildingType) {
        if(buildingsPerType.containsKey(buildingType)) {
            List<ISettlementBuilding> list = new ArrayList<>();
            list.addAll(buildingsPerType.get(buildingType).stream().filter(ISettlementBuilding::isComplete).collect(Collectors.toList()));
            return list;
        } else {
            return ImmutableList.of();
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
    public List<IBuilding> getBuildableBuildings(IBuildingType type) {
        if(type == null) {
            return ImmutableList.of();
        }
        return type.getAllBuildings().stream().filter(this::canBuildNewBuilding).collect(Collectors.toList());
    }

    @Override
    public boolean canBuildNewBuilding(IBuilding building) {
        List<ISettlementBuilding> buildingsForType = buildingsPerType.get(building.buildingType());
        return buildingsForType.size() < building.buildingType().maximumBuildingCountPerSettlement(this) && building.canBuild(mayor(), this);
    }

    @Override
    @Nullable
    public ISettlementBuilding tryBuildNewBuildingAtLocation(EntityPlayer player, IBuilding building, BlockPos pos, int rotation) {
        Schematic schematic;
        try {
            schematic = SchematicReader.getInstance().deserialize(BuildingStyleRegistry.getInstance().getSchematicLocation(building, this.getBuildingStyle()));
        } catch (IOException e) {
            LogHelper.printStackTrace(e);
            return null;
        }
        IBoundingBox box = schematic.getBoundingBox(pos, rotation);
        if(isValidBoundingBoxForBuilding(player, building, box)) {
            ISettlementBuilding built = new SettlementBuilding(this, nextBuildingId(), pos, building, schematic, rotation);
            this.buildings.put(built.id(), built);
            this.buildingsPerType.get(built.building().buildingType()).add(built);
            this.boundingBox.expandToFit(box.expand(BUILDING_CLEARANCE));
            this.worldData.markSettlementDirty(this);
            return built;
        }
        return null;
    }

    private boolean isValidBoundingBoxForBuilding(EntityPlayer player, IBuilding building, IBoundingBox buildingBox) {
        if(!this.canBuildNewBuilding(building)) {
            return false;
        }
        if(!building.canBuild(player, this)) {
            return false;
        }
        if(!this.getBoundingBox().intersects(buildingBox)) {
            return false;
        }
        for(ISettlementBuilding built : this.getBuildings()) {
            if(built.getBoundingBox().intersects(buildingBox)) {
                return false;
            }
        }
        return true;
    }

    private int nextBuildingId() {
        int id = this.buildings.size();
        if(!this.buildings.containsKey(id)) {
            return id;
        }
        for(int i = 0; i < id; i++) {
            if(!this.buildings.containsKey(i)) {
                return i;
            }
        }
        return id;
    }

    @Override
    public void removeBuilding(ISettlementBuilding building) {
        buildingsPerType.get(building.building().buildingType()).remove(building);
    }

    @Override
    public void addInhabitant(ISettler settler) {
        if(settler.settlement() == null) {
            settler.setSettlement(this);
            this.populationCount = populationCount +1;
        }
        if(!world().isRemote) {
            this.worldData.markSettlementDirty(this);
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
        return boundingBox.isWithinBounds(x, y, z);
    }

    @Override
    public int xSize() {
        return boundingBox.xSize();
    }

    @Override
    public int ySize() {
        return boundingBox.ySize();
    }

    @Override
    public int zSize() {
        return boundingBox.zSize();
    }

    @Override
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    @Override
    public double calculateDistanceSquaredToSettlement(BlockPos pos) {
        return this.getBoundingBox().calculateDistanceToCenterSquared(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
     public boolean onBuildingUpdated(ISettlementBuilding building) {
        this.buildings.put(building.id(), building);
        if(building.building() == null) {
            return false;
        }
        IBuildingType type = building.building().buildingType();
        if(!buildingsPerType.containsKey(type)) {
            this.buildingsPerType.put(type, new ArrayList<>());
        }
        this.buildingsPerType.get(type).add(building);
        IBoundingBox buildingBox = building.getBoundingBox().copy();
        buildingBox.expandToFit(buildingBox.getMaximumPosition().add(BUILDING_CLEARANCE, BUILDING_CLEARANCE, BUILDING_CLEARANCE));
        buildingBox.expandToFit(buildingBox.getMinimumPosition().add(-BUILDING_CLEARANCE, -BUILDING_CLEARANCE, -BUILDING_CLEARANCE));
        this.boundingBox.expandToFit(buildingBox);
        return true;
    }

    @Override
    public void update() {

    }

    @Override
    public NBTTagCompound writeSettlementToNBT(NBTTagCompound tag) {
        tag.setInteger(Names.NBT.SLOT, id);
        tag.setString(Names.NBT.FIRST_NAME, name);
        tag.setString(Names.NBT.SURNAME, this.mayorId.toString());
        tag.setIntArray(Names.NBT.HOME, new int[]{homeChunk.xPosition, homeChunk.zPosition});
        BlockPos pos = boundingBox.getMinimumPosition();
        tag.setInteger(Names.NBT.X, pos.getX());
        tag.setInteger(Names.NBT.Y, pos.getY());
        tag.setInteger(Names.NBT.Z, pos.getZ());
        tag.setInteger(Names.NBT.X_SIZE, xSize());
        tag.setInteger(Names.NBT.Y_SIZE, ySize());
        tag.setInteger(Names.NBT.Z_SIZE, zSize());
        tag.setString(Names.NBT.STYLE, style.getName());
        tag.setInteger(Names.NBT.INVENTORY, population());
        this.writeBuildingsToNBT(tag);
        return tag;
    }

    private void writeBuildingsToNBT(NBTTagCompound tag) {
        NBTTagList list = new NBTTagList();
        for(ISettlementBuilding building : this.getBuildings()) {
            NBTTagCompound buildingTag = building.writeBuildingToNBT(new NBTTagCompound());
            list.appendTag(buildingTag);
        }
        tag.setTag(Names.NBT.BUILDINGS, list);
    }

    @Override
    public NBTTagCompound readSettlementFromNBT(NBTTagCompound tag) {
        this.id = tag.getInteger(Names.NBT.SLOT);
        this.name = tag.getString(Names.NBT.FIRST_NAME);
        this.mayorId = UUID.fromString(tag.getString(Names.NBT.SURNAME));
        int[] homeCoords = tag.getIntArray(Names.NBT.HOME);
        this.homeChunk = this.world().getChunkFromChunkCoords(homeCoords[0], homeCoords[1]);
        int minX = tag.getInteger(Names.NBT.X);
        int minY = tag.getInteger(Names.NBT.Y);
        int minZ = tag.getInteger(Names.NBT.Z);
        int maxX = minX + tag.getInteger(Names.NBT.X_SIZE) - 1;
        int maxY = minY + tag.getInteger(Names.NBT.Y_SIZE) - 1;
        int maxZ = minZ + tag.getInteger(Names.NBT.Z_SIZE) - 1;
        this.boundingBox = new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
        this.style = BuildingStyleRegistry.getInstance().getBuildingStyleFromName(tag.getString(Names.NBT.STYLE));
        this.populationCount = tag.getInteger(Names.NBT.INVENTORY);
        this.readBuildingsFromNBT(tag);
        return tag;
    }

    private void readBuildingsFromNBT(NBTTagCompound tag) {
        this.resetBuildings();
        if(tag.hasKey(Names.NBT.BUILDINGS)) {
            NBTTagList list = tag.getTagList(Names.NBT.BUILDINGS, 10);
            for(int i = 0; i < list.tagCount(); i++) {
                ISettlementBuilding building = new SettlementBuilding(this);
                building.readBuildingFromNBT(list.getCompoundTagAt(i));
                this.buildings.put(building.id(), building);
                this.buildingsPerType.get(building.building().buildingType()).add(building);
            }
        }
    }

    private void resetBuildings() {
        this.buildings = new IdentityHashMap<>();
        this.buildingsPerType = new IdentityHashMap<>();
        for(IBuildingType type : BuildingTypeRegistry.getInstance().allBuildingTypes()) {
            buildingsPerType.put(type, new ArrayList<>());
        }
    }
}
