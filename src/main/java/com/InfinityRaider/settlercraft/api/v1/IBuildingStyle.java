package com.InfinityRaider.settlercraft.api.v1;

import net.minecraft.util.ResourceLocation;

public interface IBuildingStyle {
    /**
     * This method gets the name for this style, the name should be unique for each style.
     * This name is used in the registry to keep track of registered styles,
     * but also as a language key used in dialogues, add a translation to your lang files.
     * @return the name of this style
     */
    String getName();

    /**
     * Checks if this style recognizes this building, if true getSchematicLocation(building) will be called,
     * If false, the call will be forwarded to the building.
     *
     * @param building Building to retrieve a schematic for in this style
     * @return true if this style recognizes the building
     */
    boolean isBuildingHandled(IBuilding building);

    /**
     * Method used to retrieve a schematic resource location for a building, this method is only called when true is returned from isBuildingHandled()
     * NEVER CALL THIS METHOD DIRECTLY, instead use IBuildingStyleRegistry.getSchematicLocation(building, style).
     *
     * the format of the resource location will be according to this example:
     * new ResourceLocation("settlercraft", "buildings/house/house1") will be converted to "assets/settlercraft/buildings/house/house1.json"
     *
     * Schematics can be created by building the structure in a world and then using the schematic creator item to export the building as a json to
     * the file specified in the config.
     *
     * @param building the building to get the schematic for
     * @@return a ResourceLocation containing the path to the json file defining this building
     */
    ResourceLocation getSchematicLocation(IBuilding building);
}
