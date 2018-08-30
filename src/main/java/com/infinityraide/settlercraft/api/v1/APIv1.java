package com.infinityraide.settlercraft.api.v1;

import com.infinityraide.settlercraft.api.APIBase;
import net.minecraft.util.math.BlockPos;

/**
 * <h1>This is the SettlerCraft API, version 1.</h1>
 *
 * <p>
 * General notes:
 * </p>
 *
 * <ul>
 * <li>The methods of this API will never modify the parameter objects unless
 * explicitly stated.
 * <li>All parameters are required and may not be null unless stated otherwise.
 * <li>Return values will never be null unless stated otherwise.
 * </ul>
 *
 */
@SuppressWarnings("unused")
public interface APIv1 extends APIBase {
    /**
     * @return the SettlerCraft Block registry
     */
    ISettlerCraftBlockRegistry getBlockRegistry();

    /**
     * @return the SettlerCraft Item registry
     */
    ISettlerCraftItemRegistry getItemRegistry();

    /**
     * @return the SettlerCraft Entity regsitry
     */
    ISettlerCraftEntityRegistry getEntityRegistry();

    /**
     * @return the ISchematicMetaTransformer instance
     */
    ISchematicRotationTransformer getSchematicRotationTransformer();

    /**
     * @return The IBuildingRegistry instance keeping track of all buildings
     */
    IBuildingRegistry getBuildingRegistry();

    /**
     * @return The IBuildingTypeRegistry instance keeping track of all building types
     */
    IBuildingTypeRegistry getBuildingTypeRegistry();

    /**
     * @return The IBuildingStyleRegistry instance keeping track of all building styles
     */
    IBuildingStyleRegistry getBuildingStyleRegistry();

    /**
     * @return The IProfessionRegistry instance keeping track of all professions
     */
    IProfessionRegistry getProfessionRegistry();

    /**
     * @return the ISettlementHandler instance
     */
    ISettlementHandler getSettlementHandler();

    /**
     * @return the IDialogueOptionCreator instance to create default dialogue options
     */
    IDialogueOptionCreator getDialogueOptionCreator();

    /**
     * Gets a profession from a name
     * @param name the name of the profession
     * @return the profession or null if there is no profession with the given name
     */
    IProfession getProfessionFromName(String name);

    /**
     * Creates a new bounding box
     * @param min the minimum position
     * @param max the maximum position
     * @return a new bounding box
     */
    IBoundingBox createNewBoundingBox(BlockPos min, BlockPos max);

    /**
     * Creates a new bounding box
     * @param minX the x-coordinate of the minimum position
     * @param minY the y-coordinate of the minimum position
     * @param minZ the z-coordinate of the minimum position
     * @param maxX the x-coordinate of the maximum position
     * @param maxY the y-coordinate of the maximum position
     * @param maxZ the z-coordinate of the maximum position
     * @return a new bounding box
     */
    IBoundingBox createNewBoundingBox(int minX, int minY, int minZ, int maxX, int maxY, int maxZ);
}