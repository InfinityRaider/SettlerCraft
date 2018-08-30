package com.infinityraider.settlercraft.api.v1;

import net.minecraft.entity.EntityAgeable;

/**
 * Helper interface to access the entities added to Minecraft by SettlerCraft
 * The instance of this can be retrieved via APIv1.getEntityRegistry()
 */
@SuppressWarnings("unused")
public interface ISettlerCraftEntityRegistry {
    /**
     * @return the class for settler entity objects
     */
    Class<? extends EntityAgeable> entitySettlerClass();

    /**
     * @return the id for the settler entity
     */
    String entitySettlerId();
}
