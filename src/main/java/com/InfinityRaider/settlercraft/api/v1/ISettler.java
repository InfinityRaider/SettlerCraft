package com.InfinityRaider.settlercraft.api.v1;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.INpc;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public interface ISettler extends INpc {
    void setSettlement(ISettlement settlement);

    ISettlement settlement();

    ISettlementBuilding home();

    void setHome(ISettlementBuilding building);

    ISettlementBuilding workPlace();

    void setWorkPlace(ISettlementBuilding building);

    IProfession profession();

    void setProfession(IProfession profession);

    boolean isMayor(EntityPlayer player);

    IInventorySettler getSettlerInventory();

    EntityAgeable getEntityImplementation();

    String getTitle();

    String getFirstName();

    String getSurname();

    boolean isMale();

    boolean isAdult();

    void setConversationPartner(EntityPlayer player);

    EntityPlayer getConversationPartner();

    EntityPlayer getCurrentlyFollowingPlayer();

    boolean followPlayer(EntityPlayer player);

    ITask getCurrentTask();

    SettlerStatus getSettlerStatus();

    enum SettlerStatus {
        IDLE,
        FOLLOWING_PLAYER,
        GETTING_FOOD,
        GOING_TO_BED,
        PERFORMING_TASK;
    }
}
