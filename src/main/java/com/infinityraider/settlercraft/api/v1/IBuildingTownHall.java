package com.infinityraider.settlercraft.api.v1;

/**
 * This should be implemented in buildings which are considered town halls, when implementing this,
 * the building type must be the town hall type (see IBuildingTypeRegistry.buildingTypeTownHall() ).
 */
public interface IBuildingTownHall extends IBuilding {
    /**
     * The tier of the town hall defines the tier of the settlement, see ISettlement for a more precise description of what the tier means.
     * @return the town hall tier
     */
    int getTier();
}
