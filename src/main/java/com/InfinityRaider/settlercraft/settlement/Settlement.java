package com.InfinityRaider.settlercraft.settlement;

import com.InfinityRaider.settlercraft.api.v1.*;
import com.InfinityRaider.settlercraft.network.MessageAddInhabitant;
import com.InfinityRaider.settlercraft.network.NetworkWrapperSettlerCraft;
import com.InfinityRaider.settlercraft.reference.Names;
import com.InfinityRaider.settlercraft.settlement.building.BuildingRegistry;
import com.InfinityRaider.settlercraft.settlement.building.BuildingTypeRegistry;
import com.InfinityRaider.settlercraft.utility.ChunkCoordinates;
import com.InfinityRaider.settlercraft.utility.LogHelper;
import com.InfinityRaider.settlercraft.utility.SettlementBoundingBox;
import com.InfinityRaider.settlercraft.utility.schematic.Schematic;
import com.InfinityRaider.settlercraft.utility.schematic.SchematicReader;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Settlement extends Entity implements ISettlement, IEntityAdditionalSpawnData {
    private int id;
    private ChunkCoordinates homeChunk;
    private EntityPlayer player;
    private String name;
    private SettlementBoundingBox settlementBoundingBox;
    private HashMap<IBuildingType, List<ISettlementBuilding>> buildingsPerType;
    private List<ISettlementBuilding> tickingBuildings;

    private int populationCount;
    private String playerUUID;

    public Settlement(World world) {
        super(world);
        this.isImmuneToFire = true;
        this.firstUpdate = false;
        this.dataWatcher = new DataWatcher(this);
        this.motionX = 0;
        this.motionY = 0;
        this.motionZ = 0;
        this.rotationYaw = 0;
        this.rotationPitch = 0;
        this.prevRotationYaw = 0;
        this.prevRotationPitch = 0;
        this.resetBuildings();
    }

    public Settlement(int id, World world, EntityPlayer player, BlockPos center, String name) {
        this(world);
        this.posX = center.getX() + 0.5;
        this.posY = center.getY() + 0.5;
        this.posZ = center.getZ() + 0.5;
        this.prevPosX = posX;
        this.prevPosY = posY;
        this.prevPosZ = posZ;
        this.id = id;
        this.homeChunk = new ChunkCoordinates(world, center);
        this.player = player;
        this.playerUUID = player.getUniqueID().toString();
        this.name = name;
        this.settlementBoundingBox = new SettlementBoundingBox(center.add(0, -1, 0));
        populationCount = 1;
    }

    @Override
    public void writeSpawnData(ByteBuf data) {
        data.writeInt(this.id);
        data.writeInt(homeChunk.x());
        data.writeInt(homeChunk.z());
        data.writeInt(homeChunk.dim());
        data.writeBoolean(player != null);
        if(player != null) {
            data.writeInt(player.getEntityId());
        } else {
            ByteBufUtils.writeUTF8String(data, playerUUID);
        }
        ByteBufUtils.writeUTF8String(data, name);
        data.writeInt(settlementBoundingBox.minX());
        data.writeInt(settlementBoundingBox.minY());
        data.writeInt(settlementBoundingBox.minZ());
        data.writeInt(settlementBoundingBox.maxX());
        data.writeInt(settlementBoundingBox.maxY());
        data.writeInt(settlementBoundingBox.maxZ());
        SettlementHandler.getInstance().onSettlementLoaded(this);
    }

    @Override
    public void readSpawnData(ByteBuf data) {
        this.id = data.readInt();
        this.homeChunk = new ChunkCoordinates(data.readInt(), data.readInt(), data.readInt());
        boolean playerNotNull = data.readBoolean();
        if(playerNotNull) {
            this.player = (EntityPlayer) world().getEntityByID(data.readInt());
        } else {
            playerUUID = ByteBufUtils.readUTF8String(data);
        }
        this.name = ByteBufUtils.readUTF8String(data);
        int minX = data.readInt();
        int minY = data.readInt();
        int minZ = data.readInt();
        int maxX = data.readInt();
        int maxY = data.readInt();
        int maxZ = data.readInt();
        this.settlementBoundingBox = new SettlementBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
        SettlementHandler.getInstance().onSettlementLoaded(this);
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public World world() {
        return getEntityWorld();
    }

    @Override
    public Chunk homeChunk() {
        return homeChunk.getChunk();
    }

    @Override
    public EntityPlayer mayor() {
        if(player == null) {
            player = world().getPlayerEntityByUUID(UUID.fromString(playerUUID));
        }
        return player;
    }

    @Override
    public boolean isMayor(EntityPlayer player) {
        if(player == null) {
            return false;
        }
        EntityPlayer mayor = mayor();
        if(mayor == null) {
            return playerUUID.equals(player.getUniqueID().toString());
        }
        return player.getUniqueID().equals(mayor().getUniqueID());
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
        if(!world().isRemote) {
            NetworkWrapperSettlerCraft.getInstance().sendToAll(new MessageAddInhabitant(this, settler.getEntityImplementation()));
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
    public NBTTagCompound readSettlementFromNBT(NBTTagCompound tag) {
        this.id = tag.getInteger(Names.NBT.SLOT);
        this.name = tag.getString(Names.NBT.FIRST_NAME);
        this.playerUUID = tag.getString(Names.NBT.SURNAME);
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
        return tag;
    }

    @Override
    public NBTTagCompound writeSettlementToNBT(NBTTagCompound tag) {
        tag.setInteger(Names.NBT.SLOT, id);
        tag.setString(Names.NBT.FIRST_NAME, name);
        tag.setString(Names.NBT.SURNAME, playerUUID);
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


    //--------------
    //Entity Methods
    //--------------
    @Override
    public void onEntityUpdate() {
        this.update();
    }

    @Override
    protected final void entityInit() {}

    @Override
    protected void setOnFireFromLava() {}

    @Override
    public void extinguish() {}


    @Override
    protected void kill() {}


    @Override
    public boolean isOffsetPositionInLiquid(double x, double y, double z) {
        return false;
    }

    @Override
    public void moveEntity(double x, double y, double z) {}

    @Override
    protected void doBlockCollisions() {}

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn) {}

    @Override
    public void playSound(String name, float volume, float pitch) {}

    @Override
    public boolean isSilent() {
        return true;
    }

    @Override
    public void setSilent(boolean isSilent) {}

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, Block blockIn, BlockPos pos) {}

    @Override
    protected void dealFireDamage(int amount) {}

    @Override
    public void fall(float distance, float damageMultiplier) {}

    @Override
    public boolean isWet() {
        return false;
    }

    @Override
    public boolean isInWater() {
        return false;
    }

    @Override
    public boolean handleWaterMovement() {
        return false;
    }

    @Override
    protected void resetHeight() {}

    @Override
    public void spawnRunningParticles() {}

    @Override
    protected void createRunningParticles() {}

    @Override
    public boolean isInsideOfMaterial(Material material) {
        return false;
    }

    @Override
    public boolean isInLava() {
        return false;
    }

    @Override
    public void moveFlying(float strafe, float forward, float friction) {}

    @Override
    public void setPositionAndRotation(double x, double y, double z, float yaw, float pitch) {}

    @Override
    public void moveToBlockPosAndAngles(BlockPos pos, float rotationYawIn, float rotationPitchIn) {}

    @Override
    public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {}

    @Override
    public void applyEntityCollision(Entity entityIn) {}

    @Override
    public void addVelocity(double x, double y, double z) {}

    @Override
    protected void setBeenAttacked() {}

    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    public void onChunkLoad() {
        SettlementHandler.getInstance().onSettlementLoaded(this);
    }

    @Override
    public boolean isEntityAlive() {
        return true;
    }

    @Override
    public boolean isEntityInsideOpaqueBlock() {
        return false;
    }

    @Override
    public void updateRidden() {}

    @Override
    public void updateRiderPosition() {}

    @Override
    public void mountEntity(Entity entity) {}

    @Override
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean flag) {}

    @Override
    public float getCollisionBorderSize() {
        return 0;
    }

    @Override
    public void setPortal(BlockPos pos) {}

    @Override
    public int getPortalCooldown() {
        return Integer.MAX_VALUE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setVelocity(double x, double y, double z) {}

    @Override
    public boolean isBurning() {
        return false;
    }

    @Override
    public boolean isRiding() {
        return false;
    }

    @Override
    public boolean isSneaking() {
        return false;
    }

    @Override
    public void setSneaking(boolean sneaking) {}

    @Override
    public boolean isSprinting() {
        return false;
    }

    @Override
    public void setSprinting(boolean sprinting) {}

    @Override
    public boolean isInvisible() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isInvisibleToPlayer(EntityPlayer player) {
        return false;
    }

    @Override
    public void setInvisible(boolean invisible) {}

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isEating() {
        return false;
    }

    @Override
    public void setEating(boolean eating) {}

    @Override
    protected boolean getFlag(int flag) {
        return false;
    }

    @Override
    protected void setFlag(int flag, boolean set) {}

    @Override
    public int getAir() {
        return 0;
    }

    @Override
    public void setAir(int air) {}

    @Override
    public void onStruckByLightning(EntityLightningBolt lightningBolt) {}

    @Override
    protected boolean pushOutOfBlocks(double x, double y, double z) {
        return false;
    }

    @Override
    public void setInWeb() {}

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public boolean canAttackWithItem() {
        return false;
    }

    @Override
    public boolean hitByEntity(Entity entityIn) {
        return true;
    }

    @Override
    public boolean isEntityInvulnerable(DamageSource source) {
        return true;
    }

    @Override
    public void copyLocationAndAnglesFrom(Entity entity) {}

    @Override
    public void travelToDimension(int dimension) {}

    @Override
    public boolean verifyExplosion(Explosion explosion, World world, BlockPos pos, IBlockState blockState, float f) {
        return false;
    }

    @Override
    public int getMaxFallHeight() {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean canRenderOnFire() {
        return false;
    }

    @Override
    public boolean isPushedByWater() {
        return false;
    }

    @Override
    public void setCustomNameTag(String name) {}

    @Override
    public String getCustomNameTag() {
        return null;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public void setAlwaysRenderNameTag(boolean alwaysRenderNameTag) {}

    @Override
    public boolean getAlwaysRenderNameTag() {
        return false;
    }

    @Override
    public void setPositionAndUpdate(double x, double y, double z) {}

    @Override
    @SideOnly(Side.CLIENT)
    public boolean getAlwaysRenderNameTagForRender() {
        return false;
    }

    @Override
    public void setEntityBoundingBox(AxisAlignedBB bb){}

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }

    @Override
    protected void applyEnchantments(EntityLivingBase entityLivingBaseIn, Entity entityIn) {}

    @Override
    public boolean isCreatureType(EnumCreatureType type, boolean forSpawnCount) {
        return false;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tag) {
        readSettlementFromNBT(tag);
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tag) {
        writeSettlementToNBT(tag);
    }
}
