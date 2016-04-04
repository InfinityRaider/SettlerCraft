package com.InfinityRaider.settlercraft.api.v1;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;

/**
 * Helper interface to access the entities added to Minecraft by SettlerCraft
 * The instance of this can be retrieved via APIv1.getEntityRegistry()
 */
public interface ISettlerCraftEntityRegistry {
    /**
     * @return the class for settler entity objects
     */
    Class<? extends EntityAgeable> entitySettlerClass();

    /**
     * @return the id for the settler entity
     */
    String entitySettlerId();

    /**
     * @return the class for settlement entity objects
     */
    Class<? extends Entity> entitySettlementClass();

    /**
     * @return the id for the settlement entity
     */
    String entitySettlementId();

    /**
     * @return the class for completed settlement building entity objects
     */
    Class<? extends Entity> entityBuildingCompleteClass();

    /**
     * @return the id for the completed settlement building entity
     */
    String entityBuildingCompleteId();

    /**
     * @return the class for incomplete settlement building entity objects
     */
    Class<? extends Entity> entityBuildingIncompleteClass();

    /**
     * @return the id for the incomplete settlement building entity
     */
    String entityBuildingIncompleteId();
}
