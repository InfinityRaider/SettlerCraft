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
     * @return the class for settlement building entity objects
     */
    Class<? extends Entity> entityBuildingClass();

    /**
     * @return the id for the settlement building entity
     */
    String entityBuildingId();
}
