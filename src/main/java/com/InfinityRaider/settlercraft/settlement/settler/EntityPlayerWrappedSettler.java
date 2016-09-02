package com.InfinityRaider.settlercraft.settlement.settler;

import com.InfinityRaider.settlercraft.api.v1.*;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandResultStats;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatBase;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.*;

public class EntityPlayerWrappedSettler extends EntityPlayer implements ISettler {
    private final EntitySettler settler;
    private final GameProfile profile;

    public EntityPlayerWrappedSettler(EntitySettler settler) {
        super(settler.getWorld(), new GameProfile(UUID.fromString("41C82C87-7AfB-4024-BA57-13D2C99CAE77"), settler.getName()));
        this.settler = settler;
        this.profile = super.getGameProfile();
        //set this object's data manager to be the same as the settler's data manager
        this.dataManager = getSettler().getDataManager();
    }

    public EntitySettler getSettler() {
        return settler;
    }

    @Override
    protected void applyEntityAttributes() {
        //not relevant
    }

    @Override
    protected void entityInit() {
        //not relevant
    }

    @Override
    public int getEntityId() {
        return getSettler().getEntityId();
    }

    @Override
    public void setEntityId(int id) {
        getSettler().setEntityId(id);
    }

    @Override
    public Set<String> getTags() {
        return getSettler().getTags();
    }

    @Override
    public boolean addTag(String tag) {
        return getSettler().addTag(tag);
    }

    @Override
    public boolean removeTag(String tag) {
        return getSettler().removeTag(tag);
    }

    @Override
    public void onKillCommand() {
        getSettler().onKillCommand();
    }

    @Override
    public EntityDataManager getDataManager() {
        return getSettler().getDataManager();
    }

    @Override
    public boolean equals(Object object) {
        return getSettler().equals(object);
    }

    @Override
    public int hashCode() {
        return getSettler().hashCode();
    }

    @Override
    protected void setSize(float width, float height) {
        getSettler().setSettlerSize(width, height);
    }

    @Override
    protected void setRotation(float yaw, float pitch) {
        getSettler().setRotation(yaw, pitch);
    }

    @Override
    public void setPosition(double x, double y, double z) {
        getSettler().setPosition(x, y, z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setAngles(float yaw, float pitch) {
        getSettler().setAngles(yaw, pitch);
    }

    @Override
    protected void decrementTimeUntilPortal() {
        getSettler().decrementTimeUntilPortal();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void preparePlayerToSpawn() {
        getSettler().preparePlayerToSpawn();
    }

    @Override public void setDropItemsWhenDead(boolean dropWhenDead) {
        getSettler().setDropItemsWhenDead(dropWhenDead);
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
        getSettler().updateFallState(y, onGroundIn, state, pos);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return getSettler().canBreatheUnderwater();
    }

    @Override
    public void onEntityUpdate() {
        getSettler().onEntityUpdate();
    }

    @Override
    protected void frostWalk(BlockPos pos) {
        getSettler().frostWalk(pos);
    }

    @Override
    public boolean isChild() {
        return getSettler().isChild();
    }

    @Override
    protected void onDeathUpdate() {
        getSettler().onDeathUpdate();
    }

    @Override
    protected boolean canDropLoot() {
        return getSettler().canDropLoot();
    }

    @Override
    protected int decreaseAirSupply(int air) {
        return getSettler().decreaseAirSupply(air);
    }

    @Override
    public Random getRNG() {
        return getSettler().getRNG();
    }

    @Override
    public EntityLivingBase getAITarget() {
        return getSettler().getAITarget();
    }

    @Override
    public int getRevengeTimer() {
        return getSettler().getRevengeTimer();
    }

    @Override
    public void setRevengeTarget(@Nullable EntityLivingBase livingBase) {
        getSettler().setRevengeTarget(livingBase);
    }

    @Override
    public EntityLivingBase getLastAttacker() {
        return getSettler().getLastAttacker();
    }

    @Override
    public int getLastAttackerTime() {
        return getSettler().getLastAttackerTime();
    }

    @Override
    public void setLastAttacker(Entity entityIn) {
        getSettler().setLastAttacker(entityIn);
    }

    @Override
    public int getAge() {
        return getSettler().getAge();
    }

    @Override
    protected void playEquipSound(@Nullable ItemStack stack) {
        getSettler().playEquipSound(stack);
    }

    @Override
    protected void updatePotionEffects() {
        getSettler().updatePotionEffects();
    }

    @Override
    protected void updatePotionMetadata() {
        getSettler().updatePotionMetadata();
    }

    @Override
    protected void resetPotionEffectMetadata() {
        getSettler().resetPotionEffectMetadata();
    }

    @Override
    public void clearActivePotions() {
        getSettler().clearActivePotions();
    }

    @Override
    public Collection<PotionEffect> getActivePotionEffects() {
        return getSettler().getActivePotionEffects();
    }

    @Override
    public boolean isPotionActive(Potion potionIn) {
        return getSettler().isPotionActive(potionIn);
    }

    @Override
    @Nullable
    public PotionEffect getActivePotionEffect(Potion potionIn) {
        return getSettler().getActivePotionEffect(potionIn);
    }

    @Override
    public void addPotionEffect(PotionEffect effect) {
        getSettler().addPotionEffect(effect);
    }

    @Override
    public boolean isPotionApplicable(PotionEffect effect) {
        return getSettler().isPotionApplicable(effect);
    }

    @Override
    public boolean isEntityUndead() {
        return getSettler().isEntityUndead();
    }

    @Override
    @Nullable
    public PotionEffect removeActivePotionEffect(@Nullable Potion potion) {
        return getSettler().removeActivePotionEffect(potion);
    }

    @Override
    public void removePotionEffect(Potion potionIn) {
        getSettler().removePotionEffect(potionIn);
    }

    @Override
    protected void onNewPotionEffect(PotionEffect id) {
        getSettler().onNewPotionEffect(id);
    }

    @Override
    protected void onChangedPotionEffect(PotionEffect id, boolean flag) {
        getSettler().onChangedPotionEffect(id, flag);
    }

    @Override
    protected void onFinishedPotionEffect(PotionEffect effect) {
        getSettler().onFinishedPotionEffect(effect);
    }

    @Override
    public void heal(float healAmount) {
        getSettler().heal(healAmount);
    }

    @Override
    public void setHealth(float health) {
        getSettler().setHealth(health);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return getEntityImplementation().attackEntityFrom(source, amount);
    }

    @Override
    @Nullable
    public DamageSource func_189748_bU() {
        return getSettler().func_189748_bU();
    }

    @Override
    protected void playHurtSound(DamageSource source) {
        getSettler().playHurtSound(source);
    }

    @Override
    public void renderBrokenItemStack(ItemStack stack) {
        getSettler().renderBrokenItemStack(stack);
    }

    @Override
    protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source) {
        getSettler().dropLoot(wasRecentlyHit, lootingModifier, source);
    }

    @Override
    protected void dropEquipment(boolean wasRecentlyHit, int lootingModifier) {
        getSettler().dropEquipment(wasRecentlyHit, lootingModifier);
    }

    @Override
    public void knockBack(Entity entityIn, float strength, double xRatio, double zRatio) {
        getSettler().knockBack(entityIn, strength, xRatio, zRatio);
    }

    @Override
    @Nullable
    protected SoundEvent getHurtSound() {
        return getSettler().getHurtSound();
    }

    @Override
    @Nullable
    protected SoundEvent getDeathSound() {
        return getSettler().getDeathSound();
    }

    @Override
    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
    }

    @Override
    public boolean isOnLadder() {
        return getSettler().isOnLadder();
    }

    @Override
    public boolean isEntityAlive() {
        return getSettler().isEntityAlive();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void performHurtAnimation() {
        getSettler().performHurtAnimation();
    }

    @Override
    public int getTotalArmorValue() {
        return getSettler().getTotalArmorValue();
    }

    @Override
    protected float applyArmorCalculations(DamageSource source, float damage) {
        return getSettler().applyArmorCalculations(source, damage);
    }

    @Override
    protected float applyPotionDamageCalculations(DamageSource source, float damage) {
        return getSettler().applyPotionDamageCalculations(source, damage);
    }

    @Override
    public CombatTracker getCombatTracker() {
        return getSettler().getCombatTracker();
    }

    @Override
    @Nullable
    public EntityLivingBase getAttackingEntity() {
        return getSettler().getAttackingEntity();
    }

    @Override
    public void swingArm(EnumHand hand) {
        getSettler().swingArm(hand);
    }

    @Override
    protected void kill() {
        getSettler().kill();
    }

    @Override
    protected void updateArmSwingProgress() {
        getSettler().updateArmSwingProgress();
    }

    @Override
    public IAttributeInstance getEntityAttribute(IAttribute attribute) {
        return getSettler().getEntityAttribute(attribute);
    }

    @Override
    public AbstractAttributeMap getAttributeMap() {
        return getSettler().getAttributeMap();
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return getSettler().getCreatureAttribute();
    }

    @Override
    @Nullable
    public ItemStack getHeldItemMainhand() {
        return getSettler().getHeldItemMainhand();
    }

    @Override
    @Nullable
    public ItemStack getHeldItemOffhand() {
        return getSettler().getHeldItemOffhand();
    }

    @Override
    public void setHeldItem(EnumHand hand, @Nullable ItemStack stack) {
        getSettler().setHeldItem(hand, stack);
    }

    @Override
    public void setSprinting(boolean sprinting) {
        getSettler().setSprinting(sprinting);
    }

    @Override
    protected float getSoundVolume() {
        return getSettler().getSoundVolume();
    }

    @Override
    protected float getSoundPitch() {
        return getSettler().getSoundPitch();
    }

    @Override
    public void dismountEntity(Entity entityIn) {
        getSettler().dismountEntity(entityIn);
    }

    @Override
    protected float getJumpUpwardsMotion() {
        return getSettler().getJumpUpwardsMotion();
    }

    @Override
    protected void handleJumpWater() {
        getSettler().handleJumpWater();
    }

    @Override
    protected void handleJumpLava() {
        getSettler().handleJumpLava();
    }

    @Override
    protected float func_189749_co() {
        return getSettler().func_189749_co();
    }

    @Override
    public void onUpdate() {
        getSettler().onUpdate();
    }

    @Override
    protected void updateSize() {
        //not relevant
    }

    @Override
    public int getMaxInPortalTime() {
        return getSettler().getMaxInPortalTime();
    }

    @Override
    protected SoundEvent getSwimSound() {
        return getSettler().getSwimSound();
    }

    @Override
    protected SoundEvent getSplashSound() {
        return getSettler().getSplashSound();
    }

    @Override
    public int getPortalCooldown() {
        return getSettler().getPortalCooldown();
    }

    @Override
    protected void setOnFireFromLava() {
        getSettler().setOnFireFromLava();
    }

    @Override
    public void setFire(int seconds) {
        getSettler().setFire(seconds);
    }

    @Override
    public void extinguish() {
        getSettler().extinguish();
    }

    @Override
    public boolean isOffsetPositionInLiquid(double x, double y, double z) {
        return getSettler().isOffsetPositionInLiquid(x, y, z);
    }

    @Override
    public void moveEntity(double x, double y, double z) {
        getSettler().moveEntity(x, y, z);
    }

    @Override
    public void resetPositionToBB() {
        getSettler().resetPositionToBB();
    }

    @Override
    protected void doBlockCollisions() {
        getSettler().doBlockCollisions();
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn) {
        getSettler().playStepSound(pos, blockIn);
    }

    @Override
    public void playSound(SoundEvent soundIn, float volume, float pitch) {
        getSettler().playSound(soundIn, volume, pitch);
    }

    @Override
    public boolean isSilent() {
        return getSettler().isSilent();
    }

    @Override
    public void setSilent(boolean isSilent) {
        getSettler().setSilent(isSilent);
    }

    @Override
    public boolean func_189652_ae() {
        return getSettler().func_189652_ae();
    }

    @Override
    public void func_189654_d(boolean flag) {
        getSettler().func_189654_d(flag);
    }

    @Override
    protected void dealFireDamage(int amount) {
        getSettler().dealFireDamage(amount);
    }

    @Override
    public boolean isWet() {
        return getSettler().isWet();
    }

    @Override
    public boolean isInWater() {
        return getSettler().isInWater();
    }

    @Override
    public boolean handleWaterMovement() {
        return getSettler().handleWaterMovement();
    }

    @Override
    public void spawnRunningParticles() {
        getSettler().spawnRunningParticles();
    }

    @Override
    protected void createRunningParticles() {
        getSettler().createRunningParticles();
    }

    @Override
    public boolean isInsideOfMaterial(Material materialIn) {
        return getSettler().isInsideOfMaterial(materialIn);
    }

    @Override
    public boolean isInLava() {
        return getSettler().isInLava();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender(float partialTicks) {
        return getSettler().getBrightnessForRender(partialTicks);
    }

    @Override
    public float getBrightness(float partialTicks) {
        return getSettler().getBrightness(partialTicks);
    }

    @Override
    public void setWorld(World worldIn) {
        getSettler().setWorld(worldIn);
        this.worldObj = worldIn;
    }

    @Override
    public void setPositionAndRotation(double x, double y, double z, float yaw, float pitch) {
        getSettler().setPositionAndRotation(x, y, z, yaw, pitch);
    }

    @Override
    public void moveToBlockPosAndAngles(BlockPos pos, float rotationYawIn, float rotationPitchIn) {
        getSettler().moveToBlockPosAndAngles(pos, rotationYawIn, rotationPitchIn);
    }

    public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {
        getSettler().setLocationAndAngles(x, y, z, yaw, pitch);
    }

    @Override
    public float getDistanceToEntity(Entity entityIn) {
        return getSettler().getDistanceToEntity(entityIn);
    }

    @Override
    public double getDistanceSq(double x, double y, double z) {
        return getSettler().getDistanceSq(x, y, z);
    }

    @Override
    public double getDistanceSq(BlockPos pos) {
        return getSettler().getDistanceSq(pos);
    }

    @Override
    public double getDistanceSqToCenter(BlockPos pos) {
        return getSettler().getDistanceSqToCenter(pos);
    }

    @Override
    public double getDistance(double x, double y, double z) {
        return getSettler().getDistance(x, y, z);
    }

    @Override
    public double getDistanceSqToEntity(Entity entityIn) {
        return getSettler().getDistanceSqToEntity(entityIn);
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        getSettler().onCollideWithPlayer(entityIn);
    }

    @Override
    public void addVelocity(double x, double y, double z) {
        getSettler().addVelocity(x, y, z);
    }

    @Override
    public void moveRelative(float strafe, float forward, float friction) {
        getSettler().moveRelative(strafe, forward, friction);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Vec3d getPositionEyes(float partialTicks) {
        return getSettler().getPositionEyes(partialTicks);
    }

    @Override
    @Nullable
    @SideOnly(Side.CLIENT)
    public RayTraceResult rayTrace(double blockReachDistance, float partialTicks) {
        return getSettler().rayTrace(blockReachDistance, partialTicks);
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox() {
        return getSettler().getCollisionBoundingBox();
    }

    @Override
    public SoundCategory getSoundCategory() {
        return getSettler().getSoundCategory();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) {
        getSettler().handleStatusUpdate(id);
    }

    @Override
    protected boolean isMovementBlocked() {
        return getSettler().isMovementBlocked();
    }

    @Override
    public void updateRidden() {
        getSettler().updateRidden();
    }

    @Override
    public void updatePassenger(Entity passenger) {
        getSettler().updatePassenger(passenger);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void applyOrientationToEntity(Entity entityToUpdate) {
        getSettler().applyOrientationToEntity(entityToUpdate);
    }

    @Override
    public double getMountedYOffset() {
        return getSettler().getMountedYOffset();
    }

    @Override
    public boolean startRiding(Entity entityIn) {
        return getSettler().startRiding(entityIn);
    }

    @Override
    public boolean startRiding(Entity entityIn, boolean force) {
        return getSettler().startRiding(entityIn, force);
    }

    @Override
    protected boolean canBeRidden(Entity entityIn) {
        return getSettler().canBeRidden(entityIn);
    }

    @Override
    public void removePassengers() {
        getSettler().removePassengers();
    }

    @Override
    protected void addPassenger(Entity passenger) {
        getSettler().addPassenger(passenger);
    }

    @Override
    protected void removePassenger(Entity passenger) {
        getSettler().removePassenger(passenger);
    }

    @Override
    protected boolean canFitPassenger(Entity passenger) {
        return getSettler().canFitPassenger(passenger);
    }

    @Override
    public float getCollisionBorderSize() {
        return getSettler().getCollisionBorderSize();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Vec2f func_189653_aC() {
        return getSettler().func_189653_aC();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Vec3d func_189651_aD() {
        return getSettler().func_189651_aD();
    }

    @Override
    public void setPortal(BlockPos pos) {
        getSettler().setPortal(pos);
    }

    @Override
    public void setVelocity(double x, double y, double z) {
        getSettler().setVelocity(x, y, z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        getSettler().setPositionAndRotationDirect(x, y, z, yaw, pitch, posRotationIncrements, teleport);
    }

    @Override
    public void setJumping(boolean jumping) {
        getSettler().setJumping(jumping);
    }

    @Override
    public void onItemPickup(Entity entityIn, int quantity) {
        getSettler().onItemPickup(entityIn, quantity);
    }

    @Override
    public boolean canEntityBeSeen(Entity entityIn) {
        return getSettler().canEntityBeSeen(entityIn);
    }

    @Override
    public Vec3d getLookVec() {
        return getSettler().getLookVec();
    }

    @Override
    public Vec3d getLook(float partialTicks) {
        return getSettler().getLook(partialTicks);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getSwingProgress(float partialTickTime) {
        return getSettler().getSwingProgress(partialTickTime);
    }

    @Override
    public boolean isServerWorld() {
        return getSettler().isServerWorld();
    }

    @Override
    public boolean canBeCollidedWith() {
        return getSettler().canBeCollidedWith();
    }

    @Override
    public boolean canBePushed() {
        return getSettler().canBePushed();
    }

    @Override
    protected void setBeenAttacked() {
        getSettler().setBeenAttacked();
    }

    @Override
    public float getRotationYawHead() {
        return getSettler().getRotationYawHead();
    }

    @Override
    public void setRotationYawHead(float rotation) {
        getSettler().setRotationYawHead(rotation);
    }

    @Override
    public void setRenderYawOffset(float offset) {
        getSettler().setRenderYawOffset(offset);
    }

    @Override
    public void sendEnterCombat() {
        getSettler().sendEnterCombat();
    }

    @Override
    public void sendEndCombat() {
        getSettler().sendEndCombat();
    }

    @Override
    protected void markPotionsDirty() {
        getSettler().markPotionsDirty();
    }

    @Override
    public void curePotionEffects(ItemStack curativeItem) {
        getSettler().curePotionEffects(curativeItem);
    }

    @Override
    public boolean shouldRiderFaceForward(EntityPlayer player) {
        return getSettler().shouldRiderFaceForward(player);
    }

    @Override
    protected void updateEntityActionState() {
        getSettler().updateSettlerActionState();
    }

    @Override
    public void onLivingUpdate() {
        getSettler().onLivingUpdate();
    }

    @Override
    protected void collideWithNearbyEntities() {
        getSettler().collideWithNearbyEntities();
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        getSettler().collideWithEntity(entityIn);
    }

    @Override
    public int getScore() {
        //not relevant
        return 0;
    }

    @Override
    public void setScore(int score) {
        //not relevant
    }

    @Override
    public void addScore(int score) {
        //not relevant
    }

    @Override
    public void onDeath(DamageSource cause) {
        getSettler().onDeath(cause);
    }

    @Override
    public void addToPlayerScore(Entity entity, int amount) {
        getSettler().addToPlayerScore(entity, amount);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRender3d(double x, double y, double z) {
        return getSettler().isInRangeToRender3d(x, y, z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double distance) {
        return getSettler().isInRangeToRenderDist(distance);
    }

    @Override
    @Nullable
    public EntityItem dropItem(boolean dropAll) {
        return getSettler().dropItem(dropAll);
    }

    @Override
    @Nullable
    public EntityItem dropItem(@Nullable ItemStack stack, boolean unused) {
        return getSettler().dropItem(stack);
    }

    @Override
    @Nullable
    public EntityItem dropItem(@Nullable ItemStack droppedItem, boolean dropAround, boolean traceItem) {
        return getSettler().dropItem(droppedItem, dropAround, traceItem);
    }

    @Override
    @Nullable
    public ItemStack dropItemAndGetStack(EntityItem item) {
        return getSettler().dropItemAndGetStack(item);
    }

    @Override
    public float getDigSpeed(IBlockState state, BlockPos pos) {
        return getSettler().getDigSpeed(state);
    }

    @Override
    public boolean canHarvestBlock(IBlockState state) {
        return getSettler().canHarvestBlock(state);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        getSettler().readEntityFromNBT(compound);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        getSettler().writeEntityToNBT(compound);
    }

    @Override
    public boolean writeToNBTAtomically(NBTTagCompound compound) {
        return getSettler().writeToNBTAtomically(compound);
    }

    @Override
    public boolean writeToNBTOptional(NBTTagCompound compound) {
        return getSettler().writeToNBTOptional(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        return getSettler().writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        getSettler().readFromNBT(compound);
    }

    @Override
    protected boolean shouldSetPosAfterLoading() {
        return getSettler().shouldSetPosAfterLoading();
    }

    @Override
    protected NBTTagList newDoubleNBTList(double... numbers) {
        return getSettler().newDoubleNBTList(numbers);
    }

    @Override
    protected NBTTagList newFloatNBTList(float... numbers) {
        return getSettler().newFloatNBTList(numbers);
    }

    @Override
    public EntityItem dropItem(Item itemIn, int size) {
        return getSettler().dropItem(itemIn, size);
    }

    @Override
    public EntityItem dropItemWithOffset(Item itemIn, int size, float offsetY) {
        return getSettler().dropItemWithOffset(itemIn, size, offsetY);
    }

    @Override
    public EntityItem entityDropItem(ItemStack stack, float offsetY) {
        return getSettler().entityDropItem(stack, offsetY);
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, @Nullable ItemStack stack, EnumHand hand) {
        return getSettler().processInitialInteract(player, stack, hand);
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBox(Entity entityIn) {
        return getSettler().getCollisionBox(entityIn);
    }

    @Override
    public boolean canAttackPlayer(EntityPlayer player) {
        return getSettler().canAttackPlayer(player);
    }

    @Override
    protected void damageArmor(float damage) {
        getSettler().damageArmor(damage);
    }

    @Override
    protected void damageShield(float damage) {
        getSettler().damageShield(damage);
    }

    @Override
    public float getArmorVisibility() {
        return getSettler().getArmorVisibility();
    }

    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        getSettler().damageEntity(damageSrc, damageAmount);
    }

    @Override
    public void openEditSign(TileEntitySign sgin) {
        //not relevant
    }

    @Override
    public void displayGuiEditCommandCart(CommandBlockBaseLogic logic) {
        //not relevant
    }

    @Override
    public void displayGuiCommandBlock(TileEntityCommandBlock commandBlock) {
        //not relevant
    }

    @Override
    public void func_189807_a(TileEntityStructure structure) {
        //not relevant
    }

    @Override
    public void displayVillagerTradeGui(IMerchant villager) {
        //not relevant
    }

    @Override
    public void displayGUIChest(IInventory chestInventory) {
        //not relevant
    }

    @Override
    public void openGuiHorseInventory(EntityHorse horse, IInventory inventoryIn) {
        //not relevant
    }

    @Override
    public void displayGui(IInteractionObject guiOwner) {
        //not relevant
    }

    @Override
    public void openBook(ItemStack stack, EnumHand hand) {
        //not relevant
    }

    @Override
    public EnumActionResult interact(Entity entityIn, @Nullable ItemStack stack, EnumHand hand) {
        return getSettler().interact(entityIn, stack, hand);
    }

    @Override
    public double getYOffset() {
        return getSettler().getYOffset();
    }

    @Override
    public void dismountRidingEntity() {
        getSettler().dismountRidingEntity();
    }

    @Override
    public void attackTargetEntityWithCurrentItem(Entity targetEntity) {
        getSettler().attackTargetEntityWithCurrentItem(targetEntity);
    }

    @Override
    public void onCriticalHit(Entity entityHit) {
        getSettler().onCriticalHit(entityHit);
    }

    @Override
    public void onEnchantmentCritical(Entity entityHit) {
        getSettler().onEnchantmentCritical(entityHit);
    }

    @Override
    public void spawnSweepParticles() {
        getSettler().spawnSweepParticles();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void respawnPlayer() {
        //not relevant
    }

    @Override
    public void setDead() {
        getSettler().setDead();
    }

    @Override
    public boolean isEntityInsideOpaqueBlock() {
        return getSettler().isEntityInsideOpaqueBlock();
    }

    @Override
    public boolean isUser() {
        return false;
    }

    @Override
    public GameProfile getGameProfile() {
        return profile;
    }

    @Override
    public EntityPlayer.SleepResult trySleep(BlockPos bedLocation) {
        return getSettler().trySleepInBed(bedLocation);
    }

    @Override
    public void wakeUpPlayer(boolean immediately, boolean updateWorldFlag, boolean setSpawn) {
        //not relevant
    }

    @Override
    public boolean isPlayerSleeping() {
        return getSettler().isSleeping();
    }

    @Override
    public boolean isPlayerFullyAsleep() {
        return getSettler().isSleeping();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getSleepTimer() {
        return 100;
    }

    @Override
    public void addChatComponentMessage(ITextComponent chatComponent) {
        //not relevant
    }

    @Override
    public BlockPos getBedLocation() {
        //TODO: cache the settler's bed
        return getSettler().getHomePosition();
    }

    @Override
    public BlockPos getBedLocation(int dimension) {
        //TODO: cache the settler's bed
        return getSettler().getHomePosition();
    }

    @Override
    @Deprecated
    @SuppressWarnings("deprecation")
    public boolean isSpawnForced(){
        //not relevant
        return false;
    }

    @Override
    public boolean isSpawnForced(int dimension) {
        //not relevant
        return false;
    }

    @Override
    public void setSpawnPoint(BlockPos pos, boolean forced) {
        //not relevant
    }

    @Override
    public boolean hasAchievement(Achievement achievementIn) {
        //not relevant
        return false;
    }

    @Override
    public void addStat(StatBase stat) {
        //not relevant
    }

    @Override
    public void addStat(StatBase stat, int amount) {
        //not relevant
    }

    @Override
    public void takeStat(StatBase stat) {
        //not relevant
    }

    @Override
    public void jump() {
        getSettler().jump();
    }

    @Override
    public void moveEntityWithHeading(float strafe, float forward) {
        getSettler().moveEntityWithHeading(strafe, forward);
    }

    @Override
    public float getAIMoveSpeed() {
        return getSettler().getAIMoveSpeed();
    }

    @Override
    public void setAIMoveSpeed(float speedIn) {
        getSettler().setAIMoveSpeed(speedIn);
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        return getSettler().attackEntityAsMob(entityIn);
    }

    @Override
    protected float updateDistance(float x, float z) {
        return getSettler().updateDistance(x, z);
    }

    @Override
    public void addMovementStat(double x, double y, double z) {
        getSettler().addMovementStat(x, y, z);
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        getSettler().fall(distance, damageMultiplier);
    }

    @Override
    protected void resetHeight() {
        getSettler().resetHeight();
    }

    @Override
    protected SoundEvent getFallSound(int height) {
        return getSettler().getFallSound(height);
    }

    @Override
    public void onKillEntity(EntityLivingBase entityLivingIn) {
        getSettler().onKillEntity(entityLivingIn);
    }

    @Override
    public void setInWeb()  {
        getSettler().setInWeb();
    }

    @Override
    public void addExperience(int amount) {
        //not relevant
    }

    @Override
    public int getXPSeed() {
        //not relevant
        return getSettler().getWorld().rand.nextInt();
    }

    @Override
    public void removeExperienceLevel(int levels) {
        //not relevant
    }

    @Override
    public void addExperienceLevel(int levels) {
        //not relevant
    }

    @Override
    public int xpBarCap() {
        //not relevant
        return 0;
    }

    @Override
    public void addExhaustion(float exhaustion) {
        getSettler().addExhaustion(exhaustion);
    }

    @Override
    public FoodStats getFoodStats() {
        return getSettler().getFoodStats();
    }

    @Override
    public boolean canEat(boolean ignoreHunger) {
        return getSettler().canEat(ignoreHunger);
    }

    @Override
    public boolean shouldHeal() {
        return getSettler().shouldHeal();
    }

    @Override
    public boolean isAllowEdit() {
        //not relevant
        return true;
    }

    @Override
    public boolean canPlayerEdit(BlockPos pos, EnumFacing facing, @Nullable ItemStack stack) {
        return getSettler().canEdit(pos, facing, stack);
    }

    @Override
    protected int getExperiencePoints(EntityPlayer player) {
        //not relevant
        return 0;
    }

    @Override
    protected boolean isPlayer() {
        return getSettler().isPlayer();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean getAlwaysRenderNameTagForRender() {
        return getSettler().getAlwaysRenderNameTagForRender();
    }

    @Override
    public void clonePlayer(EntityPlayer oldPlayer, boolean respawnFromEnd) {
        //not relevant
    }

    @Override
    protected boolean canTriggerWalking() {
        return getSettler().canTriggerWalking();
    }

    @Override
    public void sendPlayerAbilities() {
        //not relevant
    }

    @Override
    public void setGameType(GameType gameType) {
        //not relevant
    }

    @Override
    public String getName() {
        return getSettler().getName();
    }

    @Override
    public InventoryEnderChest getInventoryEnderChest() {
        // not relevant
        return super.getInventoryEnderChest();
    }

    @Override
    @Nullable
    public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
        return getSettler().getItemStackFromSlot(slotIn);
    }

    @Override
    public void setItemStackToSlot(EntityEquipmentSlot slotIn, @Nullable ItemStack stack) {
        getSettler().setItemStackToSlot(slotIn, stack);
    }

    @Override
    public Iterable<ItemStack> getHeldEquipment() {
        return getSettler().getHeldEquipment();
    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return getSettler().getArmorInventoryList();
    }

    @Override
    public Iterable<ItemStack> getEquipmentAndArmor() {
        return getSettler().getEquipmentAndArmor();
    }


    @Override
    public boolean isBurning() {
        return getSettler().isBurning();
    }

    @Override
    public boolean isRiding() {
        return getSettler().isRiding();
    }

    @Override
    public boolean isBeingRidden() {
        return getSettler().isBeingRidden();
    }

    @Override
    public boolean isSneaking() {
        return getSettler().isSneaking();
    }

    @Override
    public void setSneaking(boolean sneaking) {
        getSettler().setSneaking(sneaking);
    }

    @Override
    public boolean isSprinting() {
        return getSettler().isSprinting();
    }

    @Override
    public boolean isGlowing() {
        return getSettler().isGlowing();
    }

    @Override
    public void setGlowing(boolean glowingIn) {
        getSettler().setGlowing(glowingIn);
    }

    @Override
    public boolean isInvisible() {
        return getSettler().isInvisible();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isInvisibleToPlayer(EntityPlayer player) {
        return getSettler().isInvisibleToPlayer(player);
    }

    @Override
    public boolean isSpectator() {
        //not relevant
        return false;
    }

    @Override
    public boolean isCreative() {
        //not relevant
        return false;
    }

    @Override
    public boolean isPushedByWater() {
        return getSettler().isPushedByWater();
    }

    @Override
    public Scoreboard getWorldScoreboard() {
        //not relevant
        return super.getWorldScoreboard();
    }

    @Override
    public Team getTeam() {
        return getSettler().getTeam();
    }

    @Override
    public boolean isOnSameTeam(Entity entityIn) {
        return getSettler().isOnSameTeam(entityIn);
    }

    @Override
    public boolean isOnScoreboardTeam(Team teamIn) {
        return getSettler().isOnScoreboardTeam(teamIn);
    }

    @Override
    public void setInvisible(boolean invisible) {
        getSettler().setInvisible(invisible);
    }

    @Override
    protected boolean getFlag(int flag) {
        return getSettler().getFlag(flag);
    }

    @Override
    protected void setFlag(int flag, boolean set) {
        getSettler().setFlag(flag, set);
    }

    @Override
    public int getAir() {
        return getSettler().getAir();
    }

    @Override
    public void setAir(int air) {
        getSettler().setAir(air);
    }

    @Override
    public void onStruckByLightning(EntityLightningBolt lightningBolt) {
        getSettler().onStruckByLightning(lightningBolt);
    }

    @Override
    protected boolean pushOutOfBlocks(double x, double y, double z) {
        return getSettler().pushOutOfBlocks(x, y, z);
    }

    @Override
    public Entity[] getParts() {
        return getSettler().getParts();
    }

    @Override
    public boolean isEntityEqual(Entity entityIn) {
        return getSettler().isEntityEqual(entityIn);
    }

    @Override
    public boolean canBeAttackedWithItem() {
        return getSettler().canBeAttackedWithItem();
    }

    @Override
    public boolean hitByEntity(Entity entityIn) {
        return getSettler().hitByEntity(entityIn);
    }

    @Override
    public String toString() {
        return getSettler().toString();
    }

    @Override
    public boolean isEntityInvulnerable(DamageSource source) {
        return getSettler().isEntityInvulnerable(source);
    }

    @Override
    public void setEntityInvulnerable(boolean isInvulnerable) {
        getSettler().setEntityInvulnerable(isInvulnerable);
    }

    @Override
    public void copyLocationAndAnglesFrom(Entity entityIn) {
        getSettler().copyLocationAndAnglesFrom(entityIn);
    }

    @Override
    public Entity changeDimension(int dimensionIn) {
        return getSettler().changeDimension(dimensionIn);
    }

    @Override
    public boolean isNonBoss()
    {
        return true;
    }

    @Override
    public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn) {
        return getSettler().getExplosionResistance(explosionIn, worldIn, pos, blockStateIn);
    }

    @Override
    public boolean verifyExplosion(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn, float f) {
        return getSettler().verifyExplosion(explosionIn, worldIn, pos, blockStateIn, f);
    }

    @Override
    public int getMaxFallHeight() {
        return getSettler().getMaxFallHeight();
    }

    @Override
    public Vec3d getLastPortalVec() {
        return getSettler().getLastPortalVec();
    }

    @Override
    public EnumFacing getTeleportDirection() {
        return getSettler().getTeleportDirection();
    }

    @Override
    public boolean doesEntityNotTriggerPressurePlate() {
        return getSettler().doesEntityNotTriggerPressurePlate();
    }

    @Override
    public void addEntityCrashInfo(CrashReportCategory category) {
        category.setDetail("wrapped EntitySettler player entity", () -> "");
        getSettler().addEntityCrashInfo(category);
    }

    @Override
    public void setUniqueId(UUID uniqueIdIn) {
        getSettler().setUniqueId(uniqueIdIn);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean canRenderOnFire() {
        return getSettler().canRenderOnFire();
    }

    @Override
    public UUID getUniqueID() {
        return getSettler().getUniqueID();
    }

    @Override
    public String getCachedUniqueIdString() {
        return getSettler().getCachedUniqueIdString();
    }

    @Override
    public ITextComponent getDisplayName() {
        return getSettler().getDisplayName();
    }

    @Override
    public void setCustomNameTag(String name) {
        getSettler().setCustomNameTag(name);
    }

    @Override
    public String getCustomNameTag() {
        return getSettler().getCustomNameTag();
    }

    @Override
    public boolean hasCustomName() {
        return getSettler().hasCustomName();
    }

    @Override
    public void setAlwaysRenderNameTag(boolean alwaysRenderNameTag) {
        getSettler().setAlwaysRenderNameTag(alwaysRenderNameTag);
    }

    @Override
    public boolean getAlwaysRenderNameTag() {
        return getSettler().getAlwaysRenderNameTag();
    }

    @Override
    public void setPositionAndUpdate(double x, double y, double z) {
        getSettler().setPositionAndUpdate(x, y, z);
    }

    @Override
    public EnumFacing getHorizontalFacing() {
        return getSettler().getHorizontalFacing();
    }

    @Override
    public EnumFacing getAdjustedHorizontalFacing() {
        return getSettler().getAdjustedHorizontalFacing();
    }

    @Override
    protected HoverEvent getHoverEvent() {
        return getSettler().getHoverEvent();
    }

    @Override
    public boolean isSpectatedByPlayer(EntityPlayerMP player) {
        return getSettler().isSpectatedByPlayer(player);
    }

    @Override
    public AxisAlignedBB getEntityBoundingBox() {
        return getSettler().getEntityBoundingBox();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return getSettler().getRenderBoundingBox();
    }

    @Override
    public void setEntityBoundingBox(AxisAlignedBB bb) {
        getSettler().setEntityBoundingBox(bb);
    }

    @Override
    public float getEyeHeight() {
        return getSettler().getEyeHeight();
    }

    @Override
    public boolean isOutsideBorder() {
        return getSettler().isOutsideBorder();
    }

    @Override
    public void setOutsideBorder(boolean outsideBorder) {
        this.getSettler().setOutsideBorder(outsideBorder);
    }

    @Override
    public void addChatMessage(ITextComponent component) {
        getSettler().addChatMessage(component);
    }

    @Override
    public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
        return getSettler().canCommandSenderUseCommand(permLevel, commandName);
    }

    @Override
    public BlockPos getPosition() {
        return getSettler().getPosition();
    }

    @Override
    public Vec3d getPositionVector() {
        return getSettler().getPositionVector();
    }

    @Override
    public World getEntityWorld() {
        return getSettler().getEntityWorld();
    }

    @Override
    public Entity getCommandSenderEntity() {
        return getSettler().getCommandSenderEntity();
    }

    @Override
    public boolean sendCommandFeedback() {
        return getSettler().sendCommandFeedback();
    }

    @Override
    public void setCommandStat(CommandResultStats.Type type, int amount) {
        getSettler().setCommandStat(type, amount);
    }

    @Override
    @Nullable
    public MinecraftServer getServer() {
        return getSettler().getServer();
    }

    @Override
    public CommandResultStats getCommandStats() {
        return getSettler().getCommandStats();
    }

    @Override
    public void setCommandStats(Entity entityIn) {
        getSettler().setCommandStats(entityIn);
    }

    @Override
    public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, @Nullable ItemStack stack, EnumHand hand) {
        return getSettler().applyPlayerInteraction(player, vec, stack, hand);
    }

    @Override
    public boolean isImmuneToExplosions() {
        return getSettler().isImmuneToExplosions();
    }

    @Override
    protected void applyEnchantments(EntityLivingBase entityLivingBaseIn, Entity entityIn) {
        getSettler().applyEnchantments(entityLivingBaseIn, entityIn);
    }

    @Override
    public void setAbsorptionAmount(float amount) {
        getSettler().setAbsorptionAmount(amount);
    }

    @Override
    public float getAbsorptionAmount() {
        return getSettler().getAbsorptionAmount();
    }

    @Override
    public boolean canOpen(LockCode code) {
        //not relevant
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isWearing(EnumPlayerModelParts part) {
        //not relevant
        return false;
    }

    @Override
    public boolean replaceItemInInventory(int slot, ItemStack stack) {
        return getSettler().replaceItemInInventory(slot, stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasReducedDebug() {
        //not relevant
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void setReducedDebug(boolean reducedDebug) {
        //not relevant
    }

    @Override
    public EnumHandSide getPrimaryHand() {
        return getSettler().getPrimaryHand();
    }

    @Override
    public void setPrimaryHand(EnumHandSide hand) {
        //not relevant
    }

    @Override
    public boolean isHandActive() {
        return getSettler().isHandActive();
    }

    @Override
    protected void updateActiveHand() {
        getSettler().updateActiveHand();
    }

    @Override
    public void setActiveHand(EnumHand hand) {
        getSettler().setActiveHand(hand);
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        getSettler().notifyDataManagerChange(key);
    }

    @Override
    public EnumHand getActiveHand() {
        return getSettler().getActiveHand();
    }

    @Override
    protected void updateItemUse(@Nullable ItemStack stack, int eatingParticleCount) {
        getSettler().updateItemUse(stack, eatingParticleCount);
    }

    @Override
    protected void onItemUseFinish() {
        getSettler().onItemUseFinish();
    }

    @Override
    @Nullable
    public ItemStack getActiveItemStack() {
        return getSettler().getActiveItemStack();
    }

    @Override
    public int getItemInUseCount() {
        return getSettler().getItemInUseCount();
    }

    @Override
    public int getItemInUseMaxCount() {
        return getSettler().getItemInUseMaxCount();
    }

    @Override
    public void stopActiveHand() {
        getSettler().stopActiveHand();
    }

    @Override
    public void resetActiveHand() {
        getSettler().resetActiveHand();
    }

    @Override
    public boolean isActiveItemStackBlocking() {
        return getSettler().isActiveItemStackBlocking();
    }

    @Override
    public boolean isElytraFlying() {
        return getSettler().isElytraFlying();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getTicksElytraFlying() {
        return getSettler().getTicksElytraFlying();
    }

    @Override
    public boolean attemptTeleport(double x, double y, double z) {
        return getSettler().attemptTeleport(x, y, z);
    }

    @Override
    public boolean canBeHitWithPotion() {
        return getSettler().canBeHitWithPotion();
    }

    @Override
    public float getCooldownPeriod() {
        return getSettler().getCooldownPeriod();
    }

    @Override
    public float getCooledAttackStrength(float adjustTicks) {
        return getSettler().getCooledAttackStrength(adjustTicks);
    }

    @Override
    public void resetCooldown() {
        getSettler().resetCooldown();
    }

    @Override
    public void applyEntityCollision(Entity entityIn) {
        getSettler().applyEntityCollision(entityIn);
    }

    @Override
    public float getLuck() {
        return getSettler().getLuck();
    }

    @Override
    public boolean func_189808_dh() {
        //not relevant
        return false;
    }

    @Override
    public void openGui(Object mod, int modGuiId, World world, int x, int y, int z) {
        //not relevant
    }

    @Override
    public void setSpawnChunk(BlockPos pos, boolean forced, int dimension) {
        //not relevant
    }

    @Override
    public float getDefaultEyeHeight() {
        //not relevant
        return super.getDefaultEyeHeight();
    }

    @Override
    public String getDisplayNameString() {
        return getSettler().getName();
    }

    @Override
    public void refreshDisplayName() {
        //not relevant
    }

    @Override
    public void addPrefix(ITextComponent prefix) {
        //not relevant
    }

    @Override
    public void addSuffix(ITextComponent suffix) {
        //not relevant
    }

    @Override
    public java.util.Collection<ITextComponent> getPrefixes() {
        //not relevant
        return Collections.emptyList();
    }

    @Override
    public java.util.Collection<ITextComponent> getSuffixes() {
        //not relevant
        return Collections.emptyList();
    }

    @Override
    public NBTTagCompound getEntityData() {
        return getSettler().getEntityData();
    }

    @Override
    public boolean shouldRiderSit() {
        return getSettler().shouldRiderSit();
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return getSettler().getPickedResult(target);
    }

    @Override
    public UUID getPersistentID()
    {
        return entityUniqueID;
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return getSettler().shouldRenderInPass(pass);
    }

    @Override
    public boolean isCreatureType(EnumCreatureType type, boolean forSpawnCount) {
        return getSettler().isCreatureType(type, forSpawnCount);
    }

    @Override
    public boolean canRiderInteract() {
        return getSettler().canRiderInteract();
    }

    @Override
    public boolean shouldDismountInWater(Entity rider) {
        return getSettler().shouldDismountInWater(rider);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return getSettler().getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return getSettler().hasCapability(capability, facing);
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        getSettler().deserializeNBT(nbt);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return getSettler().serializeNBT();
    }

    @Override
    public void addTrackingPlayer(EntityPlayerMP player) {
        getSettler().addTrackingPlayer(player);
    }

    @Override
    public void removeTrackingPlayer(EntityPlayerMP player) {
        getSettler().removeTrackingPlayer(player);
    }

    @Override
    public float getRotatedYaw(Rotation transformRotation) {
        return getSettler().getRotatedYaw(transformRotation);
    }

    @Override
    public float getMirroredYaw(Mirror transformMirror) {
        return getSettler().getMirroredYaw(transformMirror);
    }

    @Override
    public boolean ignoreItemEntityData() {
        return getSettler().ignoreItemEntityData();
    }

    @Override
    public boolean setPositionNonDirty() {
        return getSettler().setPositionNonDirty();
    }

    @Override
    @Nullable
    public Entity getControllingPassenger() {
        return getSettler().getControllingPassenger();
    }

    @Override
    public List<Entity> getPassengers() {
        return getSettler().getPassengers();
    }

    @Override
    public boolean isPassenger(Entity entityIn) {
        return getSettler().isPassenger(entityIn);
    }

    @Override
    public Collection<Entity> getRecursivePassengers() {
        return getSettler().getRecursivePassengers();
    }

    @Override
    public <T extends Entity> Collection<T> getRecursivePassengersByType(Class<T> entityClass) {
        return getSettler().getRecursivePassengersByType(entityClass);
    }

    @Override
    public Entity getLowestRidingEntity() {
        return getSettler().getLowestRidingEntity();
    }

    @Override
    public boolean isRidingSameEntity(Entity entityIn) {
        return getSettler().isRidingSameEntity(entityIn);
    }

    @Override
    public boolean isRidingOrBeingRiddenBy(Entity entityIn) {
        return getSettler().isRidingOrBeingRiddenBy(entityIn);
    }

    @Override
    public boolean canPassengerSteer() {
        return getSettler().canPassengerSteer();
    }

    @Override
    @Nullable
    public Entity getRidingEntity() {
        return getSettler().getRidingEntity();
    }

    @Override
    public EnumPushReaction getPushReaction() {
        return getSettler().getPushReaction();
    }

    @Override
    public World getWorld() {
        return getSettler().getWorld();
    }

    @Override
    public void setSettlement(ISettlement settlement) {
        getSettler().setSettlement(settlement);
    }

    @Override
    public ISettlement settlement() {
        return getSettler().settlement();
    }

    @Override
    public ISettlementBuilding home() {
        return getSettler().home();
    }

    @Override
    public void setHome(ISettlementBuilding building) {
        getSettler().setHome(building);
    }

    @Override
    public ISettlementBuilding workPlace() {
        return getSettler().workPlace();
    }

    @Override
    public void setWorkPlace(ISettlementBuilding building) {
        getSettler().setWorkPlace(building);
    }

    @Override
    public IProfession profession() {
        return getSettler().profession();
    }

    @Override
    public void setProfession(IProfession profession) {
        getSettler().setProfession(profession);
    }

    @Override
    public boolean isMayor(EntityPlayer player) {
        return getSettler().isMayor(player);
    }

    @Override
    public IInventorySettler getSettlerInventory() {
        return getSettler().getSettlerInventory();
    }

    @Override
    public EntityAgeable getEntityImplementation() {
        return getSettler().getEntityImplementation();
    }

    @Override
    public EntityPlayer getFakePlayerImplementation() {
        return getSettler().getFakePlayerImplementation();
    }

    @Override
    public String getTitle() {
        return getSettler().getTitle();
    }

    @Override
    public String getFirstName() {
        return getSettler().getFirstName();
    }

    @Override
    public String getSurname() {
        return getSettler().getSurname();
    }

    @Override
    public boolean isMale() {
        return getSettler().isMale();
    }

    @Override
    public boolean isAdult() {
        return getSettler().isAdult();
    }

    @Override
    public void setConversationPartner(EntityPlayer player) {
        getSettler().setConversationPartner(player);
    }

    @Override
    public EntityPlayer getConversationPartner() {
        return getSettler().getConversationPartner();
    }

    @Override
    public EntityPlayer getCurrentlyFollowingPlayer() {
        return getSettler().getCurrentlyFollowingPlayer();
    }

    @Override
    public boolean followPlayer(EntityPlayer player) {
        return getSettler().followPlayer(player);
    }

    @Override
    public List<ITask> getTasks(int priority) {
        return getSettler().getTasks(priority);
    }

    @Override
    public ITask getCurrentTask() {
        return getSettler().getCurrentTask();
    }

    @Override
    public ITask getCurrentTask(int priority) {
        return null;
    }

    @Override
    public void assignTask(ITask task) {
        getSettler().assignTask(task);
    }

    @Override
    public void queueTask(ITask task) {
        getSettler().queueTask(task);
    }

    @Override
    public void cancelTask(ITask task) {
        getSettler().cancelTask(task);
    }

    @Override
    public Optional<IMissingResource> getMissingResource() {
        return getSettler().getMissingResource();
    }

    @Override
    public void setMissingResource(IMissingResource resource) {
        getSettler().setMissingResource(resource);
    }

    @Override
    public SettlerStatus getSettlerStatus() {
        return getSettler().getSettlerStatus();
    }

    @Override
    public boolean isSleeping() {
        return getSettler().isSleeping();
    }

    @Override
    public SleepResult trySleepInBed(BlockPos pos) {
        return getSettler().trySleepInBed(pos);
    }

    @Override
    public DamageSource getDamageSource() {
        return getSettler().getDamageSource();
    }

    @Override
    public CooldownTracker getCooldownTracker() {
        return getSettler().getCooldownTracker();
    }

    @Override
    public HungerStatus getHungerStatus() {
        return getSettler().getHungerStatus();
    }

    @Override
    public void useLeftClick() {
        getSettler().useLeftClick();
    }

    @Override
    public void useRightClick() {
        getSettler().useRightClick();
    }
}
