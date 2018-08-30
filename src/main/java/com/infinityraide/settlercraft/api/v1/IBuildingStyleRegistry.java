package com.infinityraide.settlercraft.api.v1;

import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * The implementation of this interface keeps track of all registered building styles,
 * use it to access registered building styles or register new building styles.
 * The instance of the registry can be retrieved vya APIv1.getBuildingStyleRegistry()
 */
public interface IBuildingStyleRegistry {
    /**
     * Retrieves a resource location from a building and style.
     * This first checks the style if it recognizes the building,
     * if this is the case, this call is forwarded to the building style.
     * Else the call is forwarded to the building
     * @param building the building to get a schematic for
     * @param style the style of the schematic
     * @return a ResourceLocation pointing to the json for the structure data
     */
    ResourceLocation getSchematicLocation(IBuilding building, IBuildingStyle style);

    /**
     * Retrieves a building style from its name
     * @param name name of the style
     * @return the building style for this name, or the default if there is no style with such a name
     */
    IBuildingStyle getBuildingStyleFromName(String name);

    /**
     * @return a list holding all registered styles
     */
    List<IBuildingStyle> getBuildingStyles();

    /**
     * Registers a new building style
     * @param style the style to be registered
     * @return the registered style, can return null if registering has failed (for instance if not all required mods are loaded)
     */
    IBuildingStyle registerBuildingStyle(IBuildingStyle style);

    /**
     * @return The SettlerCraft default style
     */
    IBuildingStyle defaultStyle();

    /**
     * @return The SettlerCraft desert style
     */
    IBuildingStyle desertStyle();
}
