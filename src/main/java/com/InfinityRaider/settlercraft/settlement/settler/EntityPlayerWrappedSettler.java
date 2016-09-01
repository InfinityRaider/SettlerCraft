package com.InfinityRaider.settlercraft.settlement.settler;

import com.InfinityRaider.settlercraft.api.v1.*;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatBase;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.GameType;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.LockCode;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.*;

public class EntityPlayerWrappedSettler extends EntityPlayer implements ISettler {
    private static final GameProfile PROFILE = new GameProfile(UUID.fromString("41C82C87-7AfB-4024-BA57-13D2C99CAE77"), "[Minecraft]");

    private final EntitySettler settler;

    public EntityPlayerWrappedSettler(EntitySettler settler) {
        super(settler.getWorld(), PROFILE);
        this.settler = settler;
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
        //TODO: research this, its potentially dangerous
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

    //final methods, ugh...
    //TODO: sync health from settler to this
    /*
    @Override
    public final float getHealth() {
        return getSettler().getHealth();
    }
    */

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

    //stupid final methods >>
    //TODO: sync max health from settler to this
    /*
    @Override
    public final float getMaxHealth() {
        return getSettler().getMaxHealth();
    }
    */

    //stupid final methods >>
    //TODO: sync max arrow count
    /*
    @Override
    public final int getArrowCountInEntity() {
        return getSettler().getArrowCountInEntity();
    }
    */

    //stupid final methods >>
    //TODO: sync max arrow count
    /*
    @Override
    public final void setArrowCountInEntity(int count) {
        getSettler().setArrowCountInEntity(count);
    }
    */

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
    public void playSound(SoundEvent soundIn, float volume, float pitch) {
        //no sound has to be played for a settler
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
    @Nullable
    public EntityItem dropItem(boolean dropAll) {
        return getSettler().dropItem(dropAll);
    }

    @Override
    @Nullable
    public EntityItem dropItem(@Nullable ItemStack stack, boolean unused) {
        return getSettler().dropItem(stack, unused);
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
        return getSettler().getDigSpeed(state, pos);
    }

    @Override
    public boolean canHarvestBlock(IBlockState state) {
        return getSettler().canHarvestBlock(state);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        //not relevant
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        //not relevant
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
        return PROFILE;
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
        //TODO: ???
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
    public HungerStatus getHungerStatus() {
        return null;
    }

    @Override
    public ItemStack eatFood(ItemStack food) {
        return null;
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
    public ITextComponent getDisplayName() {
        return getSettler().getDisplayName();
    }

    @Override
    public float getEyeHeight() {
        return getSettler().getEyeHeight();
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
    public boolean sendCommandFeedback() {
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
    public ITask getCurrentTask() {
        return getSettler().getCurrentTask();
    }

    @Override
    public void assignTask() {
        getSettler().assignTask();
    }

    @Override
    public ItemStack getMissingResource() {
        return getSettler().getMissingResource();
    }

    @Override
    public void setMissingResource(ItemStack stack) {
        getSettler().setMissingResource(stack);
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
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return getSettler().getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return getSettler().hasCapability(capability, facing);
    }
}
