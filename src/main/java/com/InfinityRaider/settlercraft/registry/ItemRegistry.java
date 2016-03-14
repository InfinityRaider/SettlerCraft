package com.InfinityRaider.settlercraft.registry;

import com.InfinityRaider.settlercraft.api.v1.IItemBuildingPlanner;
import com.InfinityRaider.settlercraft.api.v1.ISettlerCraftItemRegistry;
import com.InfinityRaider.settlercraft.item.IItemWithRecipe;
import com.InfinityRaider.settlercraft.item.ItemBuildingPlanner;
import com.InfinityRaider.settlercraft.item.ItemDebugger;
import com.InfinityRaider.settlercraft.item.ItemSchematicCreator;
import com.InfinityRaider.settlercraft.reference.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;

public class ItemRegistry implements ISettlerCraftItemRegistry {
    private static final ItemRegistry INSTANCE = new ItemRegistry();

    public static ItemRegistry getInstance() {
        return INSTANCE;
    }

    private ItemRegistry() {
        settlerCraftTab = new CreativeTabs(Reference.MOD_ID.toLowerCase()+".creativeTab") {
            @Override
            public Item getTabIconItem() {
                return Items.bed;
            }
        };
        settlerCraftItems = new ArrayList<>();
    }

    public final CreativeTabs settlerCraftTab;

    public final List<Item> settlerCraftItems;

    public ItemSchematicCreator itemSchematicCreator;
    public ItemDebugger itemDebugger;
    public ItemBuildingPlanner itemBuildingPlanner;

    public void init() {
        itemSchematicCreator = new ItemSchematicCreator();
        itemDebugger = new ItemDebugger();
        itemBuildingPlanner = new ItemBuildingPlanner();
    }

    public void initRecipes() {
        settlerCraftItems.stream().filter(item -> item instanceof IItemWithRecipe).forEach(item ->
            ((IItemWithRecipe) item).getRecipes().forEach(GameRegistry::addRecipe));
    }

    @Override
    public CreativeTabs creativeTabSettlerCraft() {
        return settlerCraftTab;
    }

    @Override
    public List<Item> settlerCraftItems() {
        return settlerCraftItems;
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
