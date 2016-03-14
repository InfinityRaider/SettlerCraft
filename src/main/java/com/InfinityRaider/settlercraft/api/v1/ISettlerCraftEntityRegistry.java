package com.InfinityRaider.settlercraft.api.v1;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;

public interface ISettlerCraftEntityRegistry {
    Class<? extends EntityAgeable> entitySettlerClass();

    String entitySettlerId();

    Class<? extends Entity> entitySettlementClass();

    String entitySettlementId();

    Class<? extends Entity> entityBuildingCompleteClass();

    String entityBuildingCompleteId();

    Class<? extends Entity> entityBuildingIncompleteClass();

    String entityBuildingIncompleteId();
}
