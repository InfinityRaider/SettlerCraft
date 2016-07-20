package com.InfinityRaider.settlercraft.settlement.settler;

import com.InfinityRaider.settlercraft.api.v1.*;
import com.InfinityRaider.settlercraft.handler.ConfigurationHandler;
import com.InfinityRaider.settlercraft.handler.GuiHandlerSettler;
import com.InfinityRaider.settlercraft.network.MessageAssignTask;
import com.InfinityRaider.settlercraft.reference.Names;
import com.InfinityRaider.settlercraft.settlement.SettlementHandler;
import com.InfinityRaider.settlercraft.settlement.settler.ai.*;
import com.InfinityRaider.settlercraft.settlement.settler.profession.ProfessionRegistry;
import com.google.common.base.Optional;
import com.infinityraider.infinitylib.network.NetworkWrapper;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class EntitySettler extends EntityAgeable implements ISettler, IEntityAdditionalSpawnData {
    private static final SettlerRandomizer RANDOMIZER = SettlerRandomizer.getInstance();

    private static final DataParameter<Integer> DATA_SETTLEMENT = EntityDataManager.createKey(EntitySettler.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> DATA_SETTLER_STATUS = EntityDataManager.createKey(EntitySettler.class, DataSerializers.VARINT);
    private static final DataParameter<Optional<ItemStack>> DATA_NEEDED_RESOURCE = EntityDataManager.createKey(EntitySettler.class, DataSerializers.OPTIONAL_ITEM_STACK);
    private static final DataParameter<Integer> DATA_HOME_ID = EntityDataManager.createKey(EntitySettler.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> DATA_WORK_PLACE_ID = EntityDataManager.createKey(EntitySettler.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> DATA_HAS_TASK = EntityDataManager.createKey(EntitySettler.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> DATA_HUNGER_LEVEL = EntityDataManager.createKey(EntitySettler.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> DATA_SLEEPING = EntityDataManager.createKey(EntitySettler.class, DataSerializers.BOOLEAN);

    private ISettlement settlement;
    private IProfession profession;

    private boolean male;
    private String firstName;
    private String surname;
    private String title;

    private InventorySettler inventory;
    private EntityPlayer following;
    private EntityPlayer conversationPartner;

    private ITask task;

    public EntitySettler(ISettlement settlement) {
        this(settlement.world());
    }

    public EntitySettler(ISettlement settlement, String surname) {
        this(settlement.world());
        this.setSettlement(settlement);
        this.surname = surname;
    }

    public EntitySettler(World world) {
        super(world);
        this.enablePersistence();
        this.setCanPickUpLoot(true);
        this.setSize(0.6F, 1.8F);
        this.male = RANDOMIZER.getRandomGender();
        this.firstName = RANDOMIZER.getRandomFirstName(male);
        this.surname = RANDOMIZER.getRandomSurname();
        this.title = null;
        this.profession = ProfessionRegistry.getInstance().professionBuilder();
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.inventory = new InventorySettler(this);
        this.getDataManager().register(DATA_SETTLER_STATUS, 0);
        this.getDataManager().register(DATA_SETTLEMENT, -1);
        this.getDataManager().register(DATA_NEEDED_RESOURCE, Optional.absent());
        this.getDataManager().register(DATA_HOME_ID, -1);
        this.getDataManager().register(DATA_WORK_PLACE_ID, -1);
        this.getDataManager().register(DATA_HAS_TASK, false);
        this.getDataManager().register(DATA_HUNGER_LEVEL, 9);
        this.getDataManager().register(DATA_SLEEPING, false);
    }

    @SuppressWarnings("unchecked")
    protected void initEntityAI() {
        ((PathNavigateGround) this.getNavigator()).setEnterDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
        this.tasks.addTask(1, new EntityAITalkToPlayer(this));
        this.tasks.addTask(2, new EntityAIMoveIndoors(this));
        this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
        this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.6D));
        this.tasks.addTask(6, new EntityAISettler(this));
        this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
        this.tasks.addTask(9, new EntityAIWander(this, 0.6D));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
    }

    @Override
    protected PathNavigate getNewNavigator(World worldIn) {
        return new PathNavigateGround(this, worldIn);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        //update hunger level
        long time = this.getWorld().getWorldTime();
        if(!this.getWorld().isRemote && (time % 2400 == 0)) {
            int hungerLevel = this.getDataManager().get(DATA_HUNGER_LEVEL) - 1;
            this.getDataManager().set(DATA_HUNGER_LEVEL, hungerLevel);
        }
        // hurt or heal the entity based on its hunger status
        if(!this.getWorld().isRemote && (time % 1200 == 0)) {
            HungerStatus status = this.getHungerStatus();
            if(status.shouldHurt()) {
                this.damageEntity(DamageSource.starve, this.getMaxHealth() / 20F);
            } else if(status.shouldHeal()) {
                float health = this.getHealth();
                health = Math.min(this.getMaxHealth(), health + (this.getMaxHealth()/20F));
                this.setHealth(health);
            }
        }
    }

    @Override
    protected void dropEquipment(boolean flag, int nr) {
    }

    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        if(ageable instanceof EntitySettler) {
            EntitySettler partner = (EntitySettler) ageable;
            if(partner.isMale() != this.isMale() && !partner.getSurname().equals(this.surname)) {
                EntitySettler father = this.isMale() ? this : partner;
                return new EntitySettler(this.settlement(), father.getSurname());
            }
        }
        return null;
    }

    @Override
    public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, ItemStack stack, EnumHand hand) {
        if(this.conversationPartner == null) {
            this.conversationPartner = player;
        }
        if(!player.worldObj.isRemote) {
            GuiHandlerSettler.getInstance().openSettlerDialogueContainer(player, this);
        }
        return EnumActionResult.SUCCESS;
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingData) {
        livingData = super.onInitialSpawn(difficulty, livingData);

        return livingData;
    }

    @Override
    public ItemStack getHeldItem(EnumHand hand) {
        return inventory.getEquippedItem(hand);
    }

    @Override
    public List<ItemStack> getArmorInventoryList() {
        return inventory.getEquipmentList();
    }

    @Override
    public ItemStack getItemStackFromSlot(EntityEquipmentSlot slot) {
        return inventory.getEquipmentInSlot(slot);
    }

    @Override
    public void setItemStackToSlot(EntityEquipmentSlot slot, ItemStack stack) {
        this.inventory.setEquipmentInSlot(slot, stack);
    }

    @Override
    public World getWorld() {
        return worldObj;
    }

    @Override
    public void setSettlement(ISettlement settlement) {
        this.settlement = settlement;
        this.getDataManager().set(DATA_SETTLEMENT, settlement.id());
    }

    @Override
    public ISettlement settlement() {
        if(settlement == null) {
            int id = this.getDataManager().get(DATA_SETTLEMENT);
            if(id >= 0) {
                settlement = SettlementHandler.getInstance().getSettlement(this.getWorld(), id);
            }
        }
        return settlement;
    }

    @Override
    public ISettlementBuilding home() {
        if(settlement() == null) {
            return null;
        }
        return settlement().getBuildingFromId(getDataManager().get(DATA_HOME_ID));
    }

    @Override
    public void setHome(ISettlementBuilding building) {
        if(!worldObj.isRemote) {
            if (settlement() == null || (building != null && building.settlement() != settlement())) {
                return;
            }
            int id = building == null ? -1 : building.id();
            getDataManager().set(DATA_HOME_ID, id);
            if(building != null && !building.doesSettlerLiveHere(this)) {
                building.addInhabitant(this);
            }
        }
    }

    @Override
    public ISettlementBuilding workPlace() {
        if(settlement() == null) {
            return null;
        }
        return settlement().getBuildingFromId(getDataManager().get(DATA_WORK_PLACE_ID));
    }

    @Override
    public void setWorkPlace(ISettlementBuilding building) {
        if(!worldObj.isRemote) {
            if (settlement() == null || (building != null && building.settlement() != settlement())) {
                return;
            }
            int id = building == null ? -1 : building.id();
            getDataManager().set(DATA_WORK_PLACE_ID, id);
            if(building != null && !building.doesSettlerWorkHere(this)) {
                building.addWorker(this);
            }
        }
    }

    @Override
    public IProfession profession() {
        return profession;
    }

    @Override
    public void setProfession(IProfession profession) {
        if(profession == null) {
            profession = ProfessionRegistry.getInstance().professionBuilder();
        }
        this.profession = profession;
    }

    @Override
    public boolean isMayor(EntityPlayer player) {
        return settlement() != null && settlement().isMayor(player);
    }

    @Override
    public IInventorySettler getSettlerInventory() {
        return this.inventory;
    }

    @Override
    public EntityAgeable getEntityImplementation() {
        return this;
    }

    @Override
    public FakePlayer getFakePlayerImplementation() {
        return SettlementHandler.getInstance().getFakePlayerForSettler(this);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getSurname() {
        return surname;
    }

    @Override
    public boolean isMale() {
        return male;
    }

    @Override
    public boolean isAdult() {
        return !isChild();
    }

    @Override
    public void setConversationPartner(EntityPlayer player) {
        this.conversationPartner = player;
    }

    @Override
    public EntityPlayer getConversationPartner() {
        return conversationPartner;
    }

    @Override
    public EntityPlayer getCurrentlyFollowingPlayer() {
        return following;
    }

    @Override
    public boolean followPlayer(EntityPlayer player) {
        if(player == null) {
            this.following = null;
            return true;
        }
        if(this.settlement() == null) {
            if(following == null) {
                following = player;
                return true;
            } else {
                return false;
            }
        } else if(isMayor(player)) {
            following = player;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ITask getCurrentTask() {
        if(worldObj.isRemote && task == null && getDataManager().get(DATA_HAS_TASK)) {
            this.assignTask();
        }
        return task;
    }

    @Override
    public void assignTask() {
        if(workPlace() == null || workPlace().building() == null) {
            return;
        }
        if(!workPlace().getBoundingBox().areAllChunksLoaded(getWorld())) {
            return;
        }
        ITask task = workPlace().getTaskForSettler(this);
        if (!this.worldObj.isRemote) {
            if (this.task != null) {
                this.task.cancelTask();
            }
            this.getDataManager().set(DATA_HAS_TASK, true);
            NetworkWrapper.getInstance().sendToAll(new MessageAssignTask(this, false));
        }
        this.task = task;
    }

    public void setTaskCompleted() {
        this.task = null;
        if(!this.worldObj.isRemote) {
            this.getDataManager().set(DATA_HAS_TASK, false);
            NetworkWrapper.getInstance().sendToAll(new MessageAssignTask(this, true));
        }
    }

    @Override
    public ItemStack getMissingResource() {
        Optional<ItemStack> optional = this.getDataManager().get(DATA_NEEDED_RESOURCE);
        return optional.isPresent() ? optional.get() : null;
    }

    @Override
    public void setMissingResource(ItemStack stack) {
        this.getDataManager().set(DATA_NEEDED_RESOURCE, Optional.fromNullable(stack));
    }

    @SideOnly(Side.CLIENT)
    public void setSettlerStatus(SettlerStatus status) {
        this.getDataManager().set(DATA_SETTLER_STATUS, status.ordinal());
    }

    @Override
    public SettlerStatus getSettlerStatus() {
        return ISettler.SettlerStatus.values()[this.getDataManager().get(DATA_SETTLER_STATUS)];
    }

    @Override
    public int getHungerLevel() {
        return getDataManager().get(DATA_HUNGER_LEVEL);
    }

    @Override
    public HungerStatus getHungerStatus() {
        return HungerStatus.fromLevel(this.getHungerLevel());
    }

    @Override
    public boolean eatFood(ItemStack stack) {
        if(getHungerStatus() == HungerStatus.STUFFED ||stack == null || stack.getItem() == null || !(stack.getItem() instanceof ItemFood)) {
            return false;
        }
        if(!this.getWorld().isRemote) {
            ItemFood food = (ItemFood) stack.getItem();
            float saturation = food.getSaturationModifier(stack);
            float multiplier = ConfigurationHandler.getInstance().settlerFoodMultiplier;
            int hungerLevel = this.getHungerLevel() + Math.round(multiplier * saturation);
            this.getDataManager().set(DATA_HUNGER_LEVEL, hungerLevel);
        }
        return true;
    }

    @Override
    public boolean isSleeping() {
        return getDataManager().get(DATA_SLEEPING);
    }

    @Override
    public boolean goSleepInBed(BlockPos pos) {
        if(getWorld().isRemote) {
            return false;
        }
        if(this.isSleeping()) {
            return false;
        }
        double dist = pos.distanceSqToCenter(this.posX, this.posY + this.getEyeHeight(), this.posZ);
        double reach = 2.5;
        if(dist > reach * reach) {
            return false;
        }
        IBlockState bed = getWorld().getBlockState(pos);
        if(bed.getBlock() instanceof BlockBed) {
            if(bed.getValue(BlockBed.OCCUPIED)) {
                return false;
            }
            EntityPlayer.SleepResult status = this.getFakePlayerImplementation().trySleep(pos);
            boolean flag = status == EntityPlayer.SleepResult.OK;
            if(flag) {
                this.getDataManager().set(DATA_SLEEPING, true);
            }
            return flag;
        }
        return false;
    }

    @Override
    public void writeSpawnData(ByteBuf data) {
        NBTTagCompound tag = new NBTTagCompound();
        this.writeEntityToNBT(tag);
        ByteBufUtils.writeTag(data, tag);
    }

    @Override
    public void readSpawnData(ByteBuf data) {
        NBTTagCompound tag = ByteBufUtils.readTag(data);
        this.readEntityFromNBT(tag);
    }

    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setTag(Names.NBT.INVENTORY, inventory.writeToNBT());
        if(settlement != null) {
            tag.setInteger(Names.NBT.SETTLEMENT, settlement.id());
        }
        if(profession != null) {
            tag.setString(Names.NBT.PROFESSION, profession.getName());
        }
        if(title != null) {
            tag.setString(Names.NBT.TITLE, title);
        }
        tag.setString(Names.NBT.FIRST_NAME, firstName);
        tag.setString(Names.NBT.SURNAME, surname);
        tag.setBoolean(Names.NBT.GENDER, male);
        tag.setInteger(Names.NBT.SETTLEMENT, this.getDataManager().get(DATA_SETTLEMENT));
        tag.setInteger(Names.NBT.HOME, this.getDataManager().get(DATA_HOME_ID));
        tag.setInteger(Names.NBT.WORK_PLACE, this.getDataManager().get(DATA_WORK_PLACE_ID));
        tag.setBoolean(Names.NBT.TASK, this.task != null);
        tag.setInteger(Names.NBT.HUNGER, this.getDataManager().get(DATA_HUNGER_LEVEL));
        tag.setBoolean(Names.NBT.SLEEPING, this.getDataManager().get(DATA_SLEEPING));
    }

    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        this.inventory.readFromNBT(tag.getCompoundTag(Names.NBT.INVENTORY));
        if(tag.hasKey(Names.NBT.PROFESSION)) {
            this.profession = ProfessionRegistry.getInstance().getProfession(tag.getString(Names.NBT.PROFESSION));
        } else {
            this.profession = null;
        }
        if(tag.hasKey(Names.NBT.TITLE)) {
            this.title = tag.getString(Names.NBT.TITLE);
        } else {
            this.title = null;
        }
        this.firstName = tag.getString(Names.NBT.FIRST_NAME);
        this.surname = tag.getString(Names.NBT.SURNAME);
        this.male = tag.getBoolean(Names.NBT.GENDER);
        if(tag.hasKey(Names.NBT.SETTLEMENT)) {
            this.getDataManager().set(DATA_SETTLEMENT, tag.getInteger(Names.NBT.SETTLEMENT));
        } else {
            this.getDataManager().set(DATA_SETTLEMENT, -1);
        }
        this.getDataManager().set(DATA_HOME_ID, tag.getInteger(Names.NBT.HOME));
        this.getDataManager().set(DATA_WORK_PLACE_ID, tag.getInteger(Names.NBT.WORK_PLACE));
        this.getDataManager().set(DATA_HUNGER_LEVEL, tag.hasKey(Names.NBT.HUNGER) ? tag.getInteger(Names.NBT.HUNGER) : 9);
        this.getDataManager().set(DATA_SLEEPING, tag.hasKey(Names.NBT.SLEEPING) && tag.getBoolean(Names.NBT.SLEEPING));
    }
}
