package com.InfinityRaider.settlercraft.api.v1;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.INpc;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * This interface is used to interact with settlers and should not be implemented by you
 */
public interface ISettler extends INpc {
    /**
     * @return the world object the settler is currently in
     */
    World getWorld();

    /**
     * Sets the settlement for the settler, this is called from within SettlerCraft's logic and should not be called by you
     * unless you are doing very specific things.
     * @param settlement settlement for the settler
     */
    void setSettlement(ISettlement settlement);

    /**
     * Gets the settlement the settler is currently living in, might return null if the settler does not have a home settlement
     * @return the settler's settlement
     */
    ISettlement settlement();

    /**
     * Gets the building within the settlement which is the settler's home, he will go here to sleep.
     * Might return null if the settler does not have a home yet.
     * Settlers without a home are susceptible to mob attacks and sleep deprivation
     * @return the home of the settler
     */
    ISettlementBuilding home();

    /**
     * Sets the settler's home building, he will go there to sleep.
     * Settlers without a home are susceptible to mob attacks and sleep deprivation
     * @param building the home for the settler
     */
    void setHome(ISettlementBuilding building);

    /**
     * Gets the settler's work place building, he will go there to get tasks.
     * Tasks might be performed at the workplace (e.g. blacksmith), or somewhere else (e.g. lumberjack).
     * Might return null if the settler does not have a workplace assigned.
     * A settler without a workplace will wander around and look for a workplace.
     * @return the settler's current workplace
     */
    ISettlementBuilding workPlace();

    /** Sets the settler's work place building, he will go there to get tasks.
     * Tasks might be performed at the workplace (e.g. blacksmith), or somewhere else (e.g. lumberjack).
     * A settler without a workplace will wander around and look for a workplace.     *
     * @param building the settler's new workplace
     */
    void setWorkPlace(ISettlementBuilding building);

    /**
     * Gets the settler's current profession, the profession determines which tasks the settler can/will do.
     * Every settler's profession defaults to being a builder, so this will never return null.
     * @return  the settler's current profession
     */
    IProfession profession();

    /**
     * Sets the settler's new profession, the profession determines which tasks the settler can/will do.
     * Every settler's profession defaults to being a builder, so this will never return null.
     * @param profession the settler's new profession
     */
    void setProfession(IProfession profession);

    /**
     * Checks if the player is mayor of the settlement where this settler lives.
     * Being the mayor allows a player to give commands to the settler and opens up different dialogue options.
     * @param player a player
     * @return if the player is mayor
     */
    boolean isMayor(EntityPlayer player);

    /**
     * Every settler has its own inventory, see IInventorySettler for more detailed information
     * @return the settler's inventory
     */
    IInventorySettler getSettlerInventory();

    /**
     * An EntityAgeable object for this settler, this object is the actual entity in the world
     * @return EntityAgeable object for the settler
     */
    EntityAgeable getEntityImplementation();

    /**
     * An EntityPlayer object for this settler, can be used when the settler performs actions which are usually done by a player.
     * This is a virtual entity which does not exist in the world, all method calls to this object are forwarded to the settler object, use with caution.
     *
     * @return A fake EntityPlayer object representing the settler
     */
    EntityPlayer getFakePlayerImplementation();

    /**
     * @return The full name of the settler, including title, first name and surname
     */
    String getName();

    /**
     * Some settlers have achieved more than others, some settlers are allowed more than others.
     * This is reflected in the settler's title.
     * @return the settler's title
     */
    String getTitle();

    /**
     * Gets the first name of the settler, first names are determined at birth based on the settler's gender.
     * @return the settler's first name
     */
    String getFirstName();

    /**
     * Gets the surname of the settler, surnames are inherited from the father at birth.
     * @return the settler's surname
     */
    String getSurname();

    /**
     * Checks the settler's gender, gender is randomly determined at birth.
     * @return true for male, false for female
     */
    boolean isMale();

    /**
     * Checks if the settler is an adult, only adults can perform tasks.
     * Children will just wander about.
     * @return true if this settler is an adult
     */
    boolean isAdult();

    /**
     * Sets the conversation partner of the settler, a settler can only have one conversation partner at a time.
     * The parameter can be null if the conversation ended.
     * @param player the player currently conversing with the settler
     */
    void setConversationPartner(EntityPlayer player);

    /**
     * Gets the conversation partner of the settler, a settler can only have one conversation partner at a time.
     * Might return null if the settler is not having a conversation
     * @return the player currently conversing with the settler
     */
    EntityPlayer getConversationPartner();

    /**
     * Gets the player the settler is currently following, a settler can only follow one player at a time.
     * Settlers which live in a settlement will only follow the mayor.
     * @return the player which the settler is currently following
     */
    EntityPlayer getCurrentlyFollowingPlayer();

    /***
     * Commands the settler to follow a player, a settler can only follow one player at a time.
     * Settlers which live in a settlement will only follow the mayor.
     * This will return true if the settler accepted the command, false if not.
     * Possible explanations for the settler not accepting the command are:
     *  - the settler is already following someone else
     *  - the player is not the mayor
     *
     * @param player the player to follow
     * @return true if the settler is now following the player
     */
    boolean followPlayer(EntityPlayer player);

    /**
     * @return a list of all tasks the settler has queued, for the given priority, in order of execution
     * (the task currently being executed is the first object in the list)
     */
    List<ITask> getTasks(int priority);

    /**
     * Gets the highest priority task the settler is currently performing, might return null if the settler does not currently have a task
     * Tasks are only performed on the server thread so this method will always return null when queried in the client thread.
     * @return the settler's current task
     */
    ITask getCurrentTask();

    /**
     * Gets the task with the given priority the settler is currently performing, might return null if the settler does not currently have a task
     * Tasks are only performed on the server thread so this method will always return null when queried in the client thread.
     * @return the settler's current task
     */
    ITask getCurrentTask(int priority);

    /**
     * Assigns a new task to the settler, if the settler is currently performing a task, this will interrupt the settler's current task.
     * The settler's current task will be resumed after this new task has been completed.
     */
    void assignTask(ITask task);

    /**
     * Queues a task for the settler, the settler will first perform all its current tasks before starting this task
     * @param task task to queue
     */
    void queueTask(ITask task);

    /**
     * Cancels a task, if the settler is currently executing this task, the settler will stop and execute its next task,
     * if the task was interrupted, the settler will not resume the task anymore
     * @param task the task to cancel
     */
    void cancelTask(ITask task);

    /**
     * If the settler needs a certain resource to perform a task, it will be returned from this method.
     * When in need of a resource, the settler will search the settlement for it.
     * If it can not find it anywhere, the settler will run to the town hall to speak to the mayor.
     * This can return null if the settler is not missing a resource.
     * @return an itemstack the settler needs but does not have
     */
    ItemStack getMissingResource();

     /**
     * If the settler needs a certain resource to perform a task, it will be returned from this method.
     * When in need of a resource, the settler will search the settlement for it.
     * If it can not find it anywhere, the settler will run to the town hall to speak to the mayor.
     * This method will accept null.
     * @param stack itemstack the settler needs
     */
    void setMissingResource(ItemStack stack);

    /**
     * @return The status of the settler, gives an idea what the settler is currently doing.
     */
    SettlerStatus getSettlerStatus();

    /**
     * @return true if the settler is asleep
     */
    boolean isSleeping();

    /**
     * Puts the settler to sleep in the specified bed, returns true if the operation was successful.
     * Going to bed may fail if: the world is remote, the settler is too far from the bed or the bed is occupied.
     * @param pos position of the bed
     * @return if the settler went to bed with success
     */
    EntityPlayer.SleepResult trySleepInBed(BlockPos pos);

    /**
     * @return A damage source for this settler's attacks
     */
    DamageSource getDamageSource();

    /**
     * @return the settler's cooldown tracker
     */
    CooldownTracker getCooldownTracker();

    /**
     * @return The settler's food stats
     */
    FoodStats getFoodStats();

    /**
     * Checks if the settler can eat
     * @param ignoreHunger if hunger should be ignored
     * @return if the settler can eat the food
     */
    boolean canEat(boolean ignoreHunger);

    /**
     * @return the settler's hunger status
     */
    HungerStatus getHungerStatus();

    /**
     * Tries to eat a food item, returns true if successful. Only one piece of food is eaten at a time
     * @param food an ItemStack holding a food item
     * @return the remaining Item Stack
     */
    ItemStack eatFood(ItemStack food);

    /**
     * An enum with al the possible states a settler can have, used to determine settler behaviour.
     */
    enum SettlerStatus {
        IDLE,
        FOLLOWING_PLAYER,
        FINDING_RESOURCE,
        GETTING_FOOD,
        GOING_TO_BED,
        PERFORMING_TASK
    }

    /**
     * An enum with the possible settler hunger levels
     * STUFFED: the settler's stomach is full, it can not possibly eat any more
     * FINE: the settler is fine, he could eat but does not need anything
     * HUNGRY: the settler is becoming hungry and can no longer regen health, he will stop doing his task and start looking for food
     * STARVING: the settler's stomach is empty and the settler is steadily losing health, he will die if he does not eat something
     */
    enum HungerStatus {
        STUFFED(20, false),
        FINE(18,  false),
        HUNGRY(6, true),
        STARVING(0, true);

        private final int level;
        private final boolean shouldEat;

        HungerStatus(int level, boolean shouldEat) {
            this.level = level;
            this.shouldEat = shouldEat;
        }

        /**
         * This number defines which status the settler's hunger level is. The hunger level is identical to vanilla player's hunger logic
         * if the settler's hunger level is higher or equal to this number, then the settler has this status.
         */
        public int getHungerLevel() {
            return this.level;
        }

        /**
         * This defines if the settler should go looking for food
         */
        public boolean shouldEat() {
            return this.shouldEat;
        }

        public static HungerStatus fromLevel(int level) {
            for(HungerStatus status : HungerStatus.values()) {
                if(level >= status.getHungerLevel()) {
                    return status;
                }
            }
            return STARVING;
        }
    }
}
