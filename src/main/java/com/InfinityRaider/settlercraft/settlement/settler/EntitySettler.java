package com.InfinityRaider.settlercraft.settlement.settler;

import com.InfinityRaider.settlercraft.api.v1.*;
import com.InfinityRaider.settlercraft.reference.Names;
import com.InfinityRaider.settlercraft.settlement.SettlementHandler;
import com.InfinityRaider.settlercraft.settlement.settler.ai.*;
import com.InfinityRaider.settlercraft.settlement.settler.profession.ProfessionRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntitySettler extends EntityAgeable implements ISettler, IEntityAdditionalSpawnData {
    private static final SettlerRandomizer randomizer = SettlerRandomizer.getInstance();

    private ISettlement settlement;
    private IProfession profession;
    private ISettlementBuilding home;
    private ISettlementBuilding workplace;

    private boolean male;
    private String firstName;
    private String surname;
    private String title;

    private InventorySettler inventory;
    private EntityPlayer following;
    private EntityPlayer conversationPartner;

    public EntitySettler(ISettlement settlement) {
        this(settlement, randomizer.getRandomSurname());
    }

    public EntitySettler(ISettlement settlement, String surname) {
        this(settlement.world());
        this.settlement = settlement;
        this.surname = surname;
    }

    public EntitySettler(World world) {
        super(world);
        this.initTasks();
        this.enablePersistence();
        this.setCanPickUpLoot(true);
        this.setSize(0.6F, 1.8F);
        this.inventory = new InventorySettler(this);
        this.male = randomizer.getRandomGender();
        this.firstName = randomizer.getRandomFirstName(male);
        this.surname = randomizer.getRandomSurname();
        this.title = null;
        this.profession = ProfessionRegistry.getInstance().BUILDER;
    }

    @SuppressWarnings("unchecked")
    private void initTasks() {
        ((PathNavigateGround) this.getNavigator()).setBreakDoors(true);
        ((PathNavigateGround) this.getNavigator()).setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
        this.tasks.addTask(1, new EntityAITalkToPlayer(this));
        this.tasks.addTask(2, new EntityAIMoveIndoors(this));
        this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
        this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.6D));
        this.tasks.addTask(5, new EntityAIFollowPlayer(this, 1, 8, 3));
        this.tasks.addTask(6, new EntityAIGoToBed());
        this.tasks.addTask(7, new EntityAIGetFood());
        this.tasks.addTask(8, new EntityAIPerformJob());
        this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
        this.tasks.addTask(9, new EntityAIWander(this, 0.6D));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));}

    @Override
    protected void entityInit() {
        super.entityInit();
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
    public boolean interact(EntityPlayer player) {
        if(this.conversationPartner == null) {
            SettlementHandler.getInstance().interact(player, this);
        }
        return true;
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
    }

    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        this.inventory.readFromNBT(tag.getCompoundTag(Names.NBT.INVENTORY));
        if(tag.hasKey(Names.NBT.SETTLEMENT)) {
            this.settlement = SettlementHandler.getInstance().getSettlement(tag.getInteger(Names.NBT.SETTLEMENT));
        } else {
            this.settlement = null;
        }
        if(tag.hasKey(Names.NBT.PROFESSION)) {
            this.profession = ProfessionRegistry.getInstance().getProfession(tag.getString(Names.NBT.PROFESSION));
        } else {
            this.profession = null;
        }
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingData) {
        livingData = super.onInitialSpawn(difficulty, livingData);

        return livingData;
    }

    @Override
    public ItemStack getHeldItem() {
        return inventory.getEquippedItem();
    }

    @Override
    public ItemStack getEquipmentInSlot(int slot) {
        if(slot < 0) {
            return null;
        }
        if(slot == 0) {
            return inventory.getEquippedItem();
        }
        return inventory.getArmorItemInSlot(slot - 1);
    }

    @Override
    public ItemStack getCurrentArmor(int slot) {
        return inventory.getArmorItemInSlot(slot);
    }

    @Override
    public void setCurrentItemOrArmor(int slot, ItemStack stack) {
        if(slot == 0) {
            this.inventory.setEquippedItem(stack);
        }
        if(slot > 0 && slot <= 4) {
            this.inventory.setArmorItemInSlot(stack, slot-1);
        }
    }

    @Override
    public ItemStack[] getInventory() {
        return new ItemStack[0];
    }

    @Override
    public void setSettlement(ISettlement settlement) {
        this.settlement = settlement;
    }

    @Override
    public ISettlement settlement() {
        return settlement;
    }

    @Override
    public ISettlementBuilding home() {
        return home;
    }

    @Override
    public void setHome(ISettlementBuilding building) {
        if(building.settlement() == this.settlement()) {
            this.home = building;
            this.setHomePosAndDistance(home.homePosition(), Math.max(settlement.xSize(), settlement.ySize()));
        }
    }

    @Override
    public ISettlementBuilding workPlace() {
        return workplace;
    }

    @Override
    public void setWorkPlace(ISettlementBuilding building) {
        this.workplace = building;
    }

    @Override
    public IProfession profession() {
        return profession;
    }

    @Override
    public void setProfession(IProfession profession) {
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
    public void writeSpawnData(ByteBuf buffer) {

    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {

    }
}
