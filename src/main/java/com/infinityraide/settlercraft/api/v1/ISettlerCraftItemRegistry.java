package com.infinityraide.settlercraft.api.v1;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import java.util.List;

/**
 * Helper interface to access the items added to Minecraft by SettlerCraft
 * The instance of this can be retrieved via APIv1.getItemRegistry()
 */
@SuppressWarnings("unused")
public interface ISettlerCraftItemRegistry {
    /**
     * @return the Creative Tab for SettlerCraft
     */
    CreativeTabs creativeTabSettlerCraft();

    /**
     * @return a List containing all SettlerCraft items
     */
    List<Item> settlerCraftItems();

    /**
     * @return the Item instance for the schematic creator
     */
    Item itemSchematicCreator();

    /**
     * @return the Item instance for the debugger
     */
    Item itemDebugger();

    /**
     * @return the Item instance for the building planner
     */
    Item itemBuildingPlanner();

    /**
     * @return the IItemBuildingPlanner implementation for the building planner
     */
    IItemBuildingPlanner getBuildingPlanner();
}
