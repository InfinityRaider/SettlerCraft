package com.InfinityRaider.settlercraft.api.v1;

import java.util.List;
import java.util.function.Function;

/**
 * Interface used to interact with the building registry,
 * the instance of this interface can be retrieved via api.getBuildingRegistry()
 */
public interface IBuildingRegistry extends Function<IBuildingType, List<IBuilding>> {
    IBuilding getBuildingFromName(String name);
}
