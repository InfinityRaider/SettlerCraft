package com.InfinityRaider.settlercraft.settlement.settler;

import com.InfinityRaider.settlercraft.api.v1.*;
import com.InfinityRaider.settlercraft.handler.GuiHandlerSettler;
import com.InfinityRaider.settlercraft.reference.Names;
import com.InfinityRaider.settlercraft.render.entity.RenderSettler;
import com.InfinityRaider.settlercraft.settlement.SettlementHandler;
import com.InfinityRaider.settlercraft.settlement.settler.ai.*;
import com.InfinityRaider.settlercraft.settlement.settler.profession.ProfessionRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.*;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.*;

public class EntitySettler extends EntityAgeable implements ISettler, IEntityAdditionalSpawnData {
    public static final IRenderFactory<EntitySettler> RENDER_FACTORY = new RenderFactory();

    private static final SettlerRandomizer RANDOMIZER = SettlerRandomizer.getInstance();

    private static final DataParameter<Integer> DATA_SETTLEMENT = EntityDataManager.createKey(EntitySettler.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> DATA_SETTLER_STATUS = EntityDataManager.createKey(EntitySettler.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> DATA_HOME_ID = EntityDataManager.createKey(EntitySettler.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> DATA_WORK_PLACE_ID = EntityDataManager.createKey(EntitySettler.class, DataSerializers.VARINT);
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
    private EntityAIAimAtTarget aimAI;
    private EntityAISettler settlerAI;

    private FoodStats foodStats;
    private CooldownTracker cooldownTracker;
    private Optional<IMissingResource> missingResource;

    /** used for player logic */
    private final EntityPlayerWrappedSettler fakePlayer;
    private final SettlerInteractionController interactionController;

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
        this.foodStats = new FoodStats();
        this.cooldownTracker = new CooldownTracker();
        this.missingResource = Optional.empty();
        this.fakePlayer = new EntityPlayerWrappedSettler(this);
        this.interactionController = new SettlerInteractionController(this);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.inventory = new InventorySettler(this);
        this.getDataManager().register(DATA_SETTLER_STATUS, 0);
        this.getDataManager().register(DATA_SETTLEMENT, -1);
        this.getDataManager().register(DATA_HOME_ID, -1);
        this.getDataManager().register(DATA_WORK_PLACE_ID, -1);
        this.getDataManager().register(DATA_SLEEPING, false);
    }

    @SuppressWarnings("unchecked")
    protected void initEntityAI() {
        ((PathNavigateGround) this.getNavigator()).setEnterDoors(true);

        EntityAIBase aiSwim = new EntityAISwimming(this);
        aiSwim.setMutexBits(4);
        this.tasks.addTask(0, aiSwim);

        EntityAIAvoidEntity aiAvoidEntity = new EntityAIAvoidEntity(this, EntityZombie.class, 8.0F, 0.6D, 0.6D);
        aiAvoidEntity.setMutexBits(1);
        this.tasks.addTask(1, aiAvoidEntity);

        EntityAITalkToPlayer aiTalkToPlayer = new EntityAITalkToPlayer(this);
        aiTalkToPlayer.setMutexBits(5);
        this.tasks.addTask(2, aiTalkToPlayer);

        EntityAIMoveIndoors aiMoveIndoors = new EntityAIMoveIndoors(this);
        aiMoveIndoors.setMutexBits(1);
        this.tasks.addTask(3, aiMoveIndoors);

        EntityAIRestrictOpenDoor aiRestrictOpenDoor = new EntityAIRestrictOpenDoor(this);
        aiRestrictOpenDoor.setMutexBits(0);
        this.tasks.addTask(4, aiRestrictOpenDoor);

        EntityAIOpenDoor aiOpenDoor = new EntityAIOpenDoor(this, true);
        aiOpenDoor.setMutexBits(0);
        this.tasks.addTask(5, aiOpenDoor);

        EntityAIMoveTowardsRestriction aiMoveTowardsRestriction = new EntityAIMoveTowardsRestriction(this, 0.6D);
        aiMoveTowardsRestriction.setMutexBits(17);
        this.tasks.addTask(6, aiMoveTowardsRestriction);

        this.aimAI = new EntityAIAimAtTarget(this, 0.6, 2);
        aimAI.setMutexBits(28);
        this.tasks.addTask(7, aimAI);

        this.settlerAI = new EntityAISettler(this);
        this.settlerAI.setMutexBits(3);
        this.tasks.addTask(8, this.settlerAI);

        EntityAIWatchClosest2 aiWatchClosest2 =  new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F);
        aiWatchClosest2.setMutexBits(11);
        this.tasks.addTask(9, aiWatchClosest2);

        EntityAIWander aiWander = new EntityAIWander(this, 0.6D);
        aiWander.setMutexBits(9);
        this.tasks.addTask(10, aiWander);

        EntityAIWatchClosest aiWatchClosest =  new EntityAIWatchClosest(this, EntityLiving.class, 8.0F);
        aiWatchClosest.setMutexBits(10);
        this.tasks.addTask(11, aiWatchClosest);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_SPEED);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.LUCK);
    }

    @Override
    protected PathNavigate getNewNavigator(World worldIn) {
        return new PathNavigateGround(this, worldIn);
    }

    public SettlerInteractionController getInteractionController() {
        return interactionController;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(!this.getWorld().isRemote) {
            //update interactions
            this.getInteractionController().update();
            //update hunger level
            this.getFoodStats().onUpdate(getFakePlayerImplementation());
            //update cooldowns
            this.getCooldownTracker().tick();
        }
        //TODO: copy position data to the wrapped player
    }

    @Override
    public void onLivingUpdate() {
        //super call
        super.onLivingUpdate();
        //natural regen
        if (this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL && this.worldObj.getGameRules().getBoolean("naturalRegeneration")) {
            if (this.getHealth() < this.getMaxHealth() && this.ticksExisted % 20 == 0) {
                this.heal(1.0F);
            }
            if (this.foodStats.needFood() && this.ticksExisted % 10 == 0) {
                this.foodStats.setFoodLevel(this.foodStats.getFoodLevel() + 1);
            }
        }
        //inventory updates
        this.inventory.decrementAnimations();
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
            if(building == null) {
                ISettlementBuilding workplace = workPlace();
                if(workplace != null) {
                    workplace.removeWorker(this);
                }
            }
            int id = building == null ? -1 : building.id();
            getDataManager().set(DATA_WORK_PLACE_ID, id);
            if(building != null) {
                if(building.doesSettlerWorkHere(this)) {
                    building.addWorker(this);
                }
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
    public EntityPlayer getFakePlayerImplementation() {
        return fakePlayer;
    }

    @Override
    public String getName() {
        String name = getFirstName() + " " + getSurname();
        if(getTitle() != null && !getTitle().equals("")) {
            name = getTitle() + " " + name;
        }
        return name;
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
    public ISettler setLookTarget(Vec3d target) {
        if(!getWorld().isRemote) {
            this.aimAI.setTarget(target);
        }
        return this;
    }

    @Override
    public Vec3d getLookTarget() {
        if(!getWorld().isRemote) {
            return this.aimAI.getTarget();
        }
        return null;
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
    public List<ITask> getTasks(int priority) {
        if(settlerAI == null) {
            return Collections.emptyList();
        }
        return settlerAI.getTasks(priority);
    }

    @Override
    public ITask getCurrentTask() {
        if(settlerAI == null) {
            return null;
        }
        return settlerAI.getCurrentTask();
    }

    @Override
    public ITask getCurrentTask(int priority) {
        if(settlerAI == null) {
            return null;
        }
        return settlerAI.getRoutinePerformTask(priority).getCurrentTask();
    }

    @Override
    public void assignTask(ITask task) {
        if (!this.worldObj.isRemote && task != null) {
            this.settlerAI.addTask(task);
        }
    }

    @Override
    public void queueTask(ITask task) {
        if (!this.worldObj.isRemote && task != null) {
            this.settlerAI.queueTask(task);
        }
    }

    @Override
    public void cancelTask(ITask task) {
        if (!this.worldObj.isRemote && task != null) {
            this.settlerAI.cancelTask(task);
        }
    }

    @Override
    public Optional<IMissingResource> getMissingResource() {
        return missingResource;
    }

    @Override
    public void setMissingResource(IMissingResource resource) {
        if(!getWorld().isRemote) {
            if (resource == null) {
                this.missingResource = Optional.empty();
            } else {
                this.missingResource = Optional.of(resource);
            }
        }
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
    public boolean isSleeping() {
        return getDataManager().get(DATA_SLEEPING);
    }

    @Override
    public EntityPlayer.SleepResult trySleepInBed(BlockPos bed) {
        if (!this.worldObj.isRemote) {
            if (this.isSleeping() || !this.isEntityAlive()) {
                return EntityPlayer.SleepResult.OTHER_PROBLEM;
            }
            if (!this.worldObj.provider.isSurfaceWorld()) {
                return EntityPlayer.SleepResult.NOT_POSSIBLE_HERE;
            }
            if (this.worldObj.isDaytime()) {
                return EntityPlayer.SleepResult.NOT_POSSIBLE_NOW;
            }
            if (Math.abs(this.posX - (double) bed.getX()) > 3.0D || Math.abs(this.posY - (double) bed.getY()) > 2.0D || Math.abs(this.posZ - (double) bed.getZ()) > 3.0D) {
                return EntityPlayer.SleepResult.TOO_FAR_AWAY;
            }
        }
        if (this.isRiding()) {
            this.dismountRidingEntity();
        }
        this.setSize(0.2F, 0.2F);
        IBlockState state = null;
        if (this.worldObj.isBlockLoaded(bed)) state = this.worldObj.getBlockState(bed);
        if (state != null && state.getBlock().isBed(state, this.worldObj, bed, this)) {
            EnumFacing enumfacing = state.getBlock().getBedDirection(state, this.worldObj, bed);
            float f = 0.5F;
            float f1 = 0.5F;
            switch (enumfacing) {
                case SOUTH:
                    f1 = 0.9F;
                    break;
                case NORTH:
                    f1 = 0.1F;
                    break;
                case WEST:
                    f = 0.1F;
                    break;
                case EAST:
                    f = 0.9F;
            }
            this.setPosition((double)((float)bed.getX() + f), (double)((float)bed.getY() + 0.6875F), (double)((float)bed.getZ() + f1));
        } else {
            this.setPosition((double)((float)bed.getX() + 0.5F), (double)((float)bed.getY() + 0.6875F), (double)((float)bed.getZ() + 0.5F));
        }
        this.getDataManager().set(DATA_SLEEPING, true);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        return EntityPlayer.SleepResult.OK;
    }

    @Override
    public DamageSource getDamageSource() {
        return new DamageSourceSettler(this);
    }

    @Override
    public FoodStats getFoodStats() {
        return this.foodStats;
    }

    public boolean canEat(boolean ignoreHunger) {
        return (ignoreHunger || this.foodStats.needFood());
    }

    @Override
    public HungerStatus getHungerStatus() {
        return HungerStatus.fromLevel(getFoodStats().getFoodLevel());
    }

    @Override
    public void useLeftClick() {
        if(!getWorld().isRemote) {
            getInteractionController().leftClick();
        }
    }

    @Override
    public void useRightClick() {
        if(!getWorld().isRemote) {
            getInteractionController().rightClick();
        }
    }

    @Override
    public CooldownTracker getCooldownTracker() {
        return this.cooldownTracker;
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
        tag.setBoolean(Names.NBT.SLEEPING, this.getDataManager().get(DATA_SLEEPING));
        this.getFoodStats().writeNBT(tag);
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
        this.getDataManager().set(DATA_SLEEPING, tag.hasKey(Names.NBT.SLEEPING) && tag.getBoolean(Names.NBT.SLEEPING));
        this.getFoodStats().readNBT(tag);
    }

    @Override
    public boolean equals(Object object) {
        return (object instanceof Entity) && isEntityEqual((Entity) object);
    }

    @Override
    public boolean isEntityEqual(Entity entityIn) {
        return entityIn == this || entityIn == getFakePlayerImplementation();
    }


    /**
     * -------------------------
     * Inventory related methods
     * -------------------------
     */

    @Override
    public void dropEquipment(boolean wasRecentlyHit, int lootingModifier) {
    }

    @Override
    public void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source) {
        dropEquipment(wasRecentlyHit, lootingModifier);
    }

    @Nullable
    public EntityItem dropItem(boolean dropAll) {
        ItemStack stack = inventory.getCurrentItem();
        if (stack == null) {
            return null;
        }
        if (stack.getItem().onDroppedByPlayer(stack, getFakePlayerImplementation())) {
            int count = dropAll && this.inventory.getCurrentItem() != null ? this.inventory.getCurrentItem().stackSize : 1;
            return dropItem(inventory.decrStackSize(0, count), false, true);
        }
        return null;
    }

    @Nullable
    public EntityItem dropItem(@Nullable ItemStack stack) {
        return dropItem(stack, false, false);
    }

    @Nullable
    public EntityItem dropItem(@Nullable ItemStack droppedItem, boolean dropAround, boolean traceItem) {
        if (droppedItem == null) {
            return null;
        } else if (droppedItem.stackSize == 0) {
            return null;
        } else {
            double d0 = this.posY - 0.30000001192092896D + (double) this.getEyeHeight();
            EntityItem entityitem = new EntityItem(this.worldObj, this.posX, d0, this.posZ, droppedItem);
            entityitem.setPickupDelay(40);
            if (traceItem) {
                entityitem.setThrower(this.getName());
            }
            if (dropAround) {
                float f = this.rand.nextFloat() * 0.5F;
                float f1 = this.rand.nextFloat() * ((float) Math.PI * 2F);
                entityitem.motionX = (double) (-MathHelper.sin(f1) * f);
                entityitem.motionZ = (double) (MathHelper.cos(f1) * f);
                entityitem.motionY = 0.20000000298023224D;
            } else {
                float f2 = 0.3F;
                entityitem.motionX = (double) (-MathHelper.sin(this.rotationYaw * 0.017453292F) * MathHelper.cos(this.rotationPitch * 0.017453292F) * f2);
                entityitem.motionZ = (double) (MathHelper.cos(this.rotationYaw * 0.017453292F) * MathHelper.cos(this.rotationPitch * 0.017453292F) * f2);
                entityitem.motionY = (double) (-MathHelper.sin(this.rotationPitch * 0.017453292F) * f2 + 0.1F);
                float f3 = this.rand.nextFloat() * ((float) Math.PI * 2F);
                f2 = 0.02F * this.rand.nextFloat();
                entityitem.motionX += Math.cos((double) f3) * (double) f2;
                entityitem.motionY += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
                entityitem.motionZ += Math.sin((double) f3) * (double) f2;
            }
            this.dropItemAndGetStack(entityitem);
            return entityitem;
        }
    }

    @Nullable
    public ItemStack dropItemAndGetStack(EntityItem item) {
        if (captureDrops){
            capturedDrops.add(item);
        } else {
            this.worldObj.spawnEntityInWorld(item);
        }
        return item.getEntityItem();
    }

    @Override
    @Nullable
    public ItemStack getHeldItemMainhand() {
        return this.getHeldItem(EnumHand.MAIN_HAND);
    }

    @Override
    @Nullable
    public ItemStack getHeldItemOffhand() {
        return this.getHeldItem(EnumHand.OFF_HAND);
    }

    @Override
    public void setHeldItem(EnumHand hand, @Nullable ItemStack stack) {
        this.inventory.setInventorySlotContents(hand == EnumHand.MAIN_HAND ? 0 : 1, stack);
    }

    @Override
    public ItemStack getHeldItem(EnumHand hand) {
        return inventory.getEquippedItem(hand);
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
    public Iterable<ItemStack> getHeldEquipment() {
        return Arrays.asList(inventory.getEquippedItem(EnumHand.MAIN_HAND), inventory.getEquippedItem(EnumHand.OFF_HAND));
    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return Arrays.asList(inventory.getArmorInventory());
    }

    @Override
    public Iterable<ItemStack> getEquipmentAndArmor() {
        return inventory.getEquipmentList();
    }

    public boolean replaceItemInInventory(int slot, @Nullable ItemStack stack) {
        if(slot >= inventory.getSizeInventory() || !inventory.isItemValidForSlot(slot, stack)) {
            return false;
        }
        inventory.setInventorySlotContents(slot, stack);
        return true;
    }

    @Override
    public void damageArmor(float damage) {
        this.inventory.damageArmor(damage);
    }

    @Override
    public void damageShield(float damage) {
        if (damage >= 3.0F && this.activeItemStack != null && this.activeItemStack.getItem() == Items.SHIELD) {
            int i = 1 + MathHelper.floor_float(damage);
            this.activeItemStack.damageItem(i, this);
            if (this.activeItemStack.stackSize <= 0) {
                EnumHand enumhand = this.getActiveHand();
                if (enumhand == EnumHand.MAIN_HAND) {
                    this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, null);
                } else {
                    this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, null);
                }
                this.activeItemStack = null;
                this.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + this.worldObj.rand.nextFloat() * 0.4F);
            }
        }
    }


    /**
     * -----------------------------
     * Required EntityPlayer methods
     * -----------------------------
     */

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
    public float updateDistance(float x, float z) {
        return super.updateDistance(x, z);
    }

    @Override
    public void onItemUseFinish() {
        super.onItemUseFinish();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) {
        if(id == 9) {
            this.onItemUseFinish();
        } else {
            super.handleStatusUpdate(id);
        }
    }

    //Because updateEntityActionState is final >>
    public void updateSettlerActionState() {
        super.updateEntityActionState();
        this.updateArmSwingProgress();
    }

    public boolean shouldHeal() {
        return this.getHealth() > 0.0F && this.getHealth() < this.getMaxHealth();
    }

    public void attackTargetEntityWithCurrentItem(Entity targetEntity) {
        if (targetEntity.canBeAttackedWithItem()) {
            if (!targetEntity.hitByEntity(this)) {
                float atkDmg = (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
                float atkModifier;
                if (targetEntity instanceof EntityLivingBase) {
                    atkModifier = EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase) targetEntity).getCreatureAttribute());
                } else {
                    atkModifier = EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), EnumCreatureAttribute.UNDEFINED);
                }
                float cdAtkStr = this.getCooledAttackStrength(0.5F);
                atkDmg = atkDmg * (0.2F + cdAtkStr * cdAtkStr * 0.8F);
                atkModifier = atkModifier * cdAtkStr;
                this.resetCooldown();

                if (atkDmg > 0.0F || atkModifier > 0.0F) {
                    boolean doDmg = cdAtkStr > 0.9F;
                    boolean doKnockBack = false;
                    int knockBack = 0;
                    knockBack = knockBack + EnchantmentHelper.getKnockbackModifier(this);
                    //knockback
                    if (this.isSprinting() && doDmg) {
                        this.worldObj.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, this.getSoundCategory(), 1.0F, 1.0F);
                        ++knockBack;
                        doKnockBack = true;
                    }
                    //crit
                    boolean crit = doDmg && this.fallDistance > 0.0F && !this.onGround && !this.isOnLadder() && !this.isInWater() && !this.isPotionActive(MobEffects.BLINDNESS) && !this.isRiding() && targetEntity instanceof EntityLivingBase;
                    crit = crit && !this.isSprinting();
                    if (crit) {
                        atkDmg *= 1.5F;
                    }
                    atkDmg = atkDmg + atkModifier;
                    boolean withSword = false;
                    double d0 = (double) (this.distanceWalkedModified - this.prevDistanceWalkedModified);
                    if (doDmg && !crit && !doKnockBack && this.onGround && d0 < (double) this.getAIMoveSpeed()) {
                        ItemStack itemstack = this.getHeldItem(EnumHand.MAIN_HAND);
                        if (itemstack != null && itemstack.getItem() instanceof ItemSword) {
                            withSword = true;
                        }
                    }
                    float targetHp = 0.0F;
                    boolean setFire = false;
                    int fireAspect = EnchantmentHelper.getFireAspectModifier(this);
                    if (targetEntity instanceof EntityLivingBase) {
                        targetHp = ((EntityLivingBase) targetEntity).getHealth();
                        if (fireAspect > 0 && !targetEntity.isBurning()) {
                            setFire = true;
                            targetEntity.setFire(1);
                        }
                    }
                    double vX = targetEntity.motionX;
                    double vY = targetEntity.motionY;
                    double vZ = targetEntity.motionZ;
                    boolean success = targetEntity.attackEntityFrom(this.getDamageSource(), atkDmg);
                    if (success) {
                        if (knockBack > 0) {
                            if (targetEntity instanceof EntityLivingBase) {
                                ((EntityLivingBase) targetEntity).knockBack(this, (float) knockBack * 0.5F, (double) MathHelper.sin(this.rotationYaw * 0.017453292F), (double) (-MathHelper.cos(this.rotationYaw * 0.017453292F)));
                            } else {
                                targetEntity.addVelocity((double) (-MathHelper.sin(this.rotationYaw * 0.017453292F) * (float) knockBack * 0.5F), 0.1D, (double) (MathHelper.cos(this.rotationYaw * 0.017453292F) * (float) knockBack * 0.5F));
                            }
                            this.motionX *= 0.6D;
                            this.motionZ *= 0.6D;
                            this.setSprinting(false);
                        }
                        if (withSword) {
                            this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, targetEntity.getEntityBoundingBox().expand(1.0D, 0.25D, 1.0D)).stream()
                                    .filter(entity -> entity != this && entity != targetEntity && !this.isOnSameTeam(entity) && this.getDistanceSqToEntity(entity) < 9.0D)
                                    .forEach(entity -> {
                                        entity.knockBack(this, 0.4F, (double) MathHelper.sin(this.rotationYaw * 0.017453292F), (double) (-MathHelper.cos(this.rotationYaw * 0.017453292F)));
                                        entity.attackEntityFrom(this.getDamageSource(), 1.0F);
                                    });
                            this.worldObj.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, this.getSoundCategory(), 1.0F, 1.0F);
                            this.spawnSweepParticles();
                        }
                        if (targetEntity instanceof EntityPlayerMP && targetEntity.velocityChanged) {
                            ((EntityPlayerMP) targetEntity).connection.sendPacket(new SPacketEntityVelocity(targetEntity));
                            targetEntity.velocityChanged = false;
                            targetEntity.motionX = vX;
                            targetEntity.motionY = vY;
                            targetEntity.motionZ = vZ;
                        }
                        if (crit) {
                            this.worldObj.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, this.getSoundCategory(), 1.0F, 1.0F);
                            this.onCriticalHit(targetEntity);
                        }
                        if (!crit && !withSword) {
                            if (doDmg) {
                                this.worldObj.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, this.getSoundCategory(), 1.0F, 1.0F);
                            } else {
                                this.worldObj.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_WEAK, this.getSoundCategory(), 1.0F, 1.0F);
                            }
                        }
                        if (atkModifier > 0.0F) {
                            this.onEnchantmentCritical(targetEntity);
                        }
                        if (!this.worldObj.isRemote && targetEntity instanceof EntityPlayer) {
                            EntityPlayer player = (EntityPlayer) targetEntity;
                            ItemStack main = this.getHeldItemMainhand();
                            ItemStack offhand = player.isHandActive() ? player.getActiveItemStack() : null;
                            if (main != null && offhand != null && main.getItem() instanceof ItemAxe && offhand.getItem() == Items.SHIELD) {
                                float efficiency = 0.25F + (float) EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;
                                if (doKnockBack) {
                                    efficiency += 0.75F;
                                }
                                if (this.rand.nextFloat() < efficiency) {
                                    player.getCooldownTracker().setCooldown(Items.SHIELD, 100);
                                    this.worldObj.setEntityState(player, (byte) 30);
                                }
                            }
                        }
                        this.setLastAttacker(targetEntity);
                        if (targetEntity instanceof EntityLivingBase) {
                            EnchantmentHelper.applyThornEnchantments((EntityLivingBase) targetEntity, this);
                        }
                        EnchantmentHelper.applyArthropodEnchantments(this, targetEntity);
                        ItemStack mainHand = this.getHeldItemMainhand();
                        Entity entity = targetEntity;
                        if (targetEntity instanceof EntityDragonPart) {
                            IEntityMultiPart entityMultiPart = ((EntityDragonPart) targetEntity).entityDragonObj;
                            if (entityMultiPart instanceof EntityLivingBase) {
                                entity = (EntityLivingBase) entityMultiPart;
                            }
                        }
                        if (mainHand != null && entity instanceof EntityLivingBase) {
                            if (mainHand.stackSize <= 0) {
                                this.setHeldItem(EnumHand.MAIN_HAND, null);
                            }
                        }
                        if (targetEntity instanceof EntityLivingBase) {
                            float remainingHp = targetHp - ((EntityLivingBase) targetEntity).getHealth();
                            if (fireAspect > 0) {
                                targetEntity.setFire(fireAspect * 4);
                            }
                            if (this.worldObj instanceof WorldServer && remainingHp > 2.0F) {
                                int k = (int) ((double) remainingHp * 0.5D);
                                ((WorldServer) this.worldObj).spawnParticle(EnumParticleTypes.DAMAGE_INDICATOR, targetEntity.posX, targetEntity.posY + (double) (targetEntity.height * 0.5F), targetEntity.posZ, k, 0.1D, 0.0D, 0.1D, 0.2D);
                            }
                        }
                        this.addExhaustion(0.3F);
                    } else {
                        this.worldObj.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, this.getSoundCategory(), 1.0F, 1.0F);
                        if (setFire) {
                            targetEntity.extinguish();
                        }
                    }
                }
            }
        }
    }

    public boolean canAttackPlayer(EntityPlayer player) {
        return !isMayor(player);
    }

    public float getDigSpeed(IBlockState state) {
        float strength = this.inventory.getStrVsBlock(state);
        if (strength > 1.0F) {
            int efficiency = EnchantmentHelper.getEfficiencyModifier(this);
            ItemStack itemstack = this.getHeldItemMainhand();
            if (efficiency > 0 && itemstack != null) {
                strength += (float)(efficiency * efficiency + 1);
            }
        }
        if (this.isPotionActive(MobEffects.HASTE)) {
            PotionEffect effect = this.getActivePotionEffect(MobEffects.HASTE);
            if(effect != null) {
                strength *= 1.0F + (float) (effect.getAmplifier() + 1)*0.2F;
            }
        }
        if (this.isPotionActive(MobEffects.MINING_FATIGUE)) {
            PotionEffect effect = this.getActivePotionEffect(MobEffects.MINING_FATIGUE);
            if(effect != null) {
                float fatigue;
                switch (effect.getAmplifier()) {
                    case 0:
                        fatigue = 0.3F;
                        break;
                    case 1:
                        fatigue = 0.09F;
                        break;
                    case 2:
                        fatigue = 0.0027F;
                        break;
                    case 3:
                    default:
                        fatigue = 8.1E-4F;
                }
                strength *= fatigue;
            }
        }
        if (this.isInsideOfMaterial(Material.WATER) && !EnchantmentHelper.getAquaAffinityModifier(this)) {
            strength /= 5.0F;
        }
        if (!this.onGround) {
            strength /= 5.0F;
        }
        return (strength < 0 ? 0 : strength);
    }

    public boolean canHarvestBlock(IBlockState state) {
        return this.inventory.canHarvestBlock(state);
    }

    public float getArmorVisibility() {
        int i = 0;
        for (ItemStack itemstack : this.inventory.getArmorInventory()) {
            if (itemstack != null) {
                ++i;
            }
        }
        return (float)i / (float)this.inventory.getArmorInventory().length;
    }

    public void addExhaustion(float exhaustion) {
        if (!this.worldObj.isRemote) {
            this.foodStats.addExhaustion(exhaustion);
        }
    }

    public float getCooledAttackStrength(float adjustTicks) {
        return MathHelper.clamp_float(((float)this.ticksSinceLastSwing + adjustTicks) / this.getCooldownPeriod(), 0.0F, 1.0F);
    }

    public float getCooldownPeriod() {
        return (float)(1.0D / this.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).getAttributeValue() * 20.0D);
    }

    public void resetCooldown() {
        this.ticksSinceLastSwing = 0;
    }

    public void spawnSweepParticles() {
        if (this.worldObj instanceof WorldServer) {
            double d0 = (double)(-MathHelper.sin(this.rotationYaw * 0.017453292F));
            double d1 = (double)MathHelper.cos(this.rotationYaw * 0.017453292F);
            ((WorldServer)this.worldObj).spawnParticle(
                    EnumParticleTypes.SWEEP_ATTACK, this.posX + d0, this.posY + (double)this.height * 0.5D, this.posZ + d1, 0, d0, 0.0D, d1, 0.0D);
        }
    }

    public void onCriticalHit(Entity entityHit) {
        if(this.getWorld().isRemote) {
            Minecraft.getMinecraft().effectRenderer.emitParticleAtEntity(entityHit, EnumParticleTypes.CRIT);
        } else {
            WorldServer server = this.getServerWorld();
            if(server != null) {
                server.getEntityTracker().sendToTrackingAndSelf(this, new SPacketAnimation(entityHit, 4));
            }
        }
    }

    public void onEnchantmentCritical(Entity entityHit) {
        if(this.getWorld().isRemote) {
            Minecraft.getMinecraft().effectRenderer.emitParticleAtEntity(entityHit, EnumParticleTypes.CRIT_MAGIC);
        } else {
            WorldServer server = this.getServerWorld();
            if (server != null) {
                server.getEntityTracker().sendToTrackingAndSelf(this, new SPacketAnimation(entityHit, 5));
            }
        }
    }

    private WorldServer getServerWorld() {
        if(this.getWorld().isRemote) {
            return null;
        } else {
            return (WorldServer) this.worldObj;
        }
    }

    public boolean canEdit(BlockPos pos, EnumFacing facing, @Nullable ItemStack stack) {
        if (stack == null) {
            return false;
        } else {
            BlockPos blockpos = pos.offset(facing.getOpposite());
            Block block = this.worldObj.getBlockState(blockpos).getBlock();
            return stack.canPlaceOn(block) || stack.canEditBlocks();
        }
    }

    public float getLuck() {
        return (float)this.getEntityAttribute(SharedMonsterAttributes.LUCK).getAttributeValue();
    }

    @Override
    public void moveEntityWithHeading(float strafe, float forward) {
        double x0 = this.posX;
        double y0 = this.posY;
        double z0 = this.posZ;
        super.moveEntityWithHeading(strafe, forward);
        this.addMovementStat(this.posX - x0, this.posY - y0, this.posZ - z0);
    }

    public void addMovementStat(double x, double y, double z) {
        if (!this.isRiding()) {
            if (this.isInsideOfMaterial(Material.WATER)) {
                int i = Math.round(MathHelper.sqrt_double(x * x + y * y + z * z) * 100.0F);
                if (i > 0) {
                    this.addExhaustion(0.015F * (float)i * 0.01F);
                }
            } else if (this.isInWater()) {
                int j = Math.round(MathHelper.sqrt_double(x * x + z * z) * 100.0F);
                if (j > 0) {
                    this.addExhaustion(0.015F * (float)j * 0.01F);
                }
            } else if (this.onGround) {
                int k = Math.round(MathHelper.sqrt_double(x * x + z * z) * 100.0F);
                if (k > 0) {
                    if (this.isSprinting()) {
                        this.addExhaustion(0.099999994F * (float)k * 0.01F);
                    } else if (this.isSneaking()) {
                        this.addExhaustion(0.005F * (float)k * 0.01F);
                    } else {
                        this.addExhaustion(0.01F * (float)k * 0.01F);
                    }
                }
            }
        }
    }

    public EnumActionResult interact(Entity entity, @Nullable ItemStack stack, EnumHand hand) {
        if (!entity.processInitialInteract(getFakePlayerImplementation(), stack, hand)) {
            if (stack != null && entity instanceof EntityLivingBase) {
                if (stack.interactWithEntity(getFakePlayerImplementation(), (EntityLivingBase) entity, hand)) {
                    if (stack.stackSize <= 0) {
                        this.setHeldItem(hand, null);
                    }
                    return EnumActionResult.SUCCESS;
                }
            }
            return EnumActionResult.PASS;
        } else {
            if (stack != null && stack == this.getHeldItem(hand)) {
                if (stack.stackSize <= 0) {
                    this.setHeldItem(hand, null);
                }
            }
            return EnumActionResult.SUCCESS;
        }
    }


    /**
     * -----------------------------------
     * protected super methods made public
     * -----------------------------------
     */

    @Override
    public boolean isPlayer() {
        return super.isPlayer();
    }

    @Override
    public boolean canTriggerWalking() {
        return super.canTriggerWalking();
    }


    @Override
    public float applyArmorCalculations(DamageSource source, float damage) {
        return super.applyArmorCalculations(source, damage);
    }

    @Override
    public float applyPotionDamageCalculations(DamageSource source, float damage) {
        return super.applyPotionDamageCalculations(source, damage);
    }

    @Override
    public void damageEntity(DamageSource damageSrc, float damageAmount) {
        super.damageEntity(damageSrc, damageAmount);
    }

    @Override
    public void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
        super.updateFallState(y, onGroundIn, state, pos);
    }

    @Override
    public boolean pushOutOfBlocks(double x, double y, double z) {
        return super.pushOutOfBlocks(x, y , z);
    }

    @Override
    public void frostWalk(BlockPos pos) {
        super.frostWalk(pos);
    }

    @Override
    public void onDeathUpdate() {
        super.onDeathUpdate();
    }

    @Override
    public boolean canDropLoot() {
        return super.canDropLoot();
    }

    @Override
    public int decreaseAirSupply(int air) {
        return super.decreaseAirSupply(air);
    }

    @Override
    public float getJumpUpwardsMotion() {
        return super.getJumpUpwardsMotion();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void preparePlayerToSpawn() {
        super.preparePlayerToSpawn();
    }

    public void setSettlerSize(float width, float height) {
        super.setSize(width, height);
    }

    @Override
    public void setRotation(float yaw, float pitch) {
        super.setRotation(yaw, pitch);
    }

    @Override
    public void decrementTimeUntilPortal() {
        super.decrementTimeUntilPortal();
    }

    @Override
    public void setOnFireFromLava() {
        super.setOnFireFromLava();
    }

    @Override
    public void doBlockCollisions() {
        super.doBlockCollisions();
    }

    @Override
    public void dealFireDamage(int amount) {
        super.dealFireDamage(amount);
    }

    @Override
    public void jump() {
        super.jump();
    }

    @Override
    public void handleJumpWater() {
        super.handleJumpWater();
    }

    @Override
    public void handleJumpLava() {
        super.handleJumpLava();
    }

    @Override
    public float func_189749_co() {
        return super.func_189749_co();
    }

    @Override
    public void resetHeight() {
        super.resetHeight();
    }

    @Override
    public void setBeenAttacked() {
        super.setBeenAttacked();
    }

    @Override
    public void collideWithNearbyEntities() {
        super.collideWithNearbyEntities();
    }

    @Override
    public void collideWithEntity(Entity entityIn) {
        super.collideWithEntity(entityIn);
    }

    @Override
    public void createRunningParticles() {
        super.createRunningParticles();
    }

    @Override
    public boolean shouldSetPosAfterLoading() {
        return super.shouldSetPosAfterLoading();
    }

    @Override
    public NBTTagList newDoubleNBTList(double... numbers) {
        return super.newDoubleNBTList(numbers);
    }

    @Override
    public NBTTagList newFloatNBTList(float... numbers) {
        return super.newFloatNBTList(numbers);
    }

    @Override
    public boolean canBeRidden(Entity entityIn) {
        return super.canBeRidden(entityIn);
    }

    @Override
    public void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
    }

    @Override
    public void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
    }

    @Override
    public boolean canFitPassenger(Entity passenger) {
        return super.canFitPassenger(passenger);
    }

    //There is absolutely no reason this should be client only
    @Override
    public void setVelocity(double x, double y, double z) {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
    }

    @Override
    public void updatePotionEffects() {
        super.updatePotionEffects();
    }

    @Override
    public void updatePotionMetadata() {
        super.updatePotionMetadata();
    }

    @Override
    public void resetPotionEffectMetadata() {
        super.resetPotionEffectMetadata();
    }

    @Override
    public void onNewPotionEffect(PotionEffect id) {
        super.onNewPotionEffect(id);
    }

    @Override
    public void onChangedPotionEffect(PotionEffect id, boolean flag) {
        super.onChangedPotionEffect(id, flag);
    }

    @Override
    public void onFinishedPotionEffect(PotionEffect effect) {
        super.onFinishedPotionEffect(effect);
    }

    @Override
    public void markPotionsDirty() {
        super.markPotionsDirty();
    }

    @Override
    public void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
        super.dropFewItems(wasRecentlyHit, lootingModifier);
    }

    @Override
    public void kill() {
        super.kill();
    }

    @Override
    public void updateArmSwingProgress() {
        super.updateArmSwingProgress();
    }

    @Override
    public void updateActiveHand() {
        super.updateActiveHand();
    }

    @Override
    public void updateItemUse(@Nullable ItemStack stack, int eatingParticleCount) {
        super.updateItemUse(stack, eatingParticleCount);
    }

    @Override
    public boolean isMovementBlocked() {
        return this.getHealth() <= 0.0F;
    }

    @Override
    public float getSoundVolume() {
        return super.getSoundVolume();
    }

    @Override
    public float getSoundPitch() {
        return super.getSoundPitch();
    }

    @Override
    public void playHurtSound(DamageSource source) {
        super.playHurtSound(source);
    }

    @Override
    public void playEquipSound(@Nullable ItemStack stack) {
        super.playEquipSound(stack);
    }

    @Override
    public void playStepSound(BlockPos pos, Block blockIn) {
        super.playStepSound(pos, blockIn);
    }

    @Override
    @Nullable
    public SoundEvent getHurtSound() {
        return SoundEvents.ENTITY_PLAYER_HURT;
    }

    @Override
    @Nullable
    public SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_PLAYER_DEATH;
    }

    @Override
    public SoundEvent getSwimSound() {
        return SoundEvents.ENTITY_PLAYER_SWIM;
    }

    @Override
    public SoundEvent getSplashSound() {
        return SoundEvents.ENTITY_PLAYER_SPLASH;
    }

    @Override
    public SoundEvent getFallSound(int height) {
        return height > 4 ? SoundEvents.ENTITY_PLAYER_BIG_FALL : SoundEvents.ENTITY_PLAYER_SMALL_FALL;
    }

    @Override
    public boolean getFlag(int flag) {
        return super.getFlag(flag);
    }

    @Override
    public void setFlag(int flag, boolean set) {
        super.setFlag(flag, set);
    }

    @Override
    public HoverEvent getHoverEvent() {
        return super.getHoverEvent();
    }

    @Override
    public void applyEnchantments(EntityLivingBase entityLivingBaseIn, Entity entityIn) {
        super.applyEnchantments(entityLivingBaseIn, entityIn);
    }


    /**
     * Render factory
     */
    private static class RenderFactory implements IRenderFactory<EntitySettler> {
        @Override
        @SideOnly(Side.CLIENT)
        public Render<? super EntitySettler> createRenderFor(RenderManager manager) {
            return new RenderSettler(manager);
        }
    }
}
