package com.InfinityRaider.settlercraft.api.v1;

/**
 * Interface used to interact with the building registry,
 * the instance of this interface can be retrieved via api.getBuildingRegistry()
 */
public interface IBuildingRegistry {
    IBuilding getBuildingFromName(String name);
}
