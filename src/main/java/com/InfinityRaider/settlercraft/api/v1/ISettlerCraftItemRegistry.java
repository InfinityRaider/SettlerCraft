package com.InfinityRaider.settlercraft.api.v1;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * Helper interface to access the items added to Minecraft by SettlerCraft
 * The instance of this can be retrieved via APIv1.getItemRegistry()
 */
public interface ISettlerCraftItemRegistry {
    CreativeTabs creativeTabSettlerCraft();

    Item itemSchematicCreator();

    Item itemDebugger();
}
