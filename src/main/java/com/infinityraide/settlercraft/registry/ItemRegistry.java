package com.infinityraide.settlercraft.registry;

import com.infinityraide.settlercraft.api.v1.IItemBuildingPlanner;
import com.infinityraide.settlercraft.api.v1.ISettlerCraftItemRegistry;
import com.infinityraide.settlercraft.item.ItemSchematicCreator;
import com.infinityraide.settlercraft.reference.Reference;
import com.google.common.collect.ImmutableList;
import com.infinityraide.settlercraft.item.ItemBuildingPlanner;
import com.infinityraide.settlercraft.item.ItemDebugger;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemRegistry implements ISettlerCraftItemRegistry {
    private static final ItemRegistry INSTANCE = new ItemRegistry();

    public static final ItemStack LOGO_STACK = new ItemStack(Items.BED);

    public static ItemRegistry getInstance() {
        return INSTANCE;
    }

    private ItemRegistry() {
        settlerCraftTab = new CreativeTabs(Reference.MOD_ID.toLowerCase()+".creativeTab") {
            @Override
            public ItemStack getTabIconItem() {
                return LOGO_STACK;
            }
        };
        settlerCraftItems = new ArrayList<>();
        this.init();
    }

    public final CreativeTabs settlerCraftTab;

    public final List<Item> settlerCraftItems;

    public ItemSchematicCreator itemSchematicCreator;
    public ItemDebugger itemDebugger;
    public ItemBuildingPlanner itemBuildingPlanner;

    private void init() {
        itemSchematicCreator = new ItemSchematicCreator();
        settlerCraftItems.add(itemSchematicCreator.setCreativeTab(creativeTabSettlerCraft()));

        itemDebugger = new ItemDebugger();
        settlerCraftItems.add(itemDebugger.setCreativeTab(creativeTabSettlerCraft()));

        itemBuildingPlanner = new ItemBuildingPlanner();
        settlerCraftItems.add(itemBuildingPlanner);
    }

    @Override
    public CreativeTabs creativeTabSettlerCraft() {
        return settlerCraftTab;
    }

    @Override
    public List<Item> settlerCraftItems() {
        return ImmutableList.copyOf(settlerCraftItems);
    }

    @Override
    public Item itemSchematicCreator() {
        return itemSchematicCreator;
    }

    @Override
    public Item itemDebugger() {
        return itemDebugger;
    }

    @Override
    public Item itemBuildingPlanner() {
        return itemBuildingPlanner;
    }

    @Override
    public IItemBuildingPlanner getBuildingPlanner() {
        return itemBuildingPlanner;
    }
}
