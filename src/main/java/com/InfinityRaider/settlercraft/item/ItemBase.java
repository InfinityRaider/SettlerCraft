package com.InfinityRaider.settlercraft.item;

import com.InfinityRaider.settlercraft.registry.ItemRegistry;
import com.InfinityRaider.settlercraft.utility.RegisterHelper;
import net.minecraft.item.Item;

public abstract class ItemBase extends Item {
    public ItemBase(String name) {
        super();
        this.setCreativeTab(ItemRegistry.getInstance().settlerCraftTab);
        RegisterHelper.registerItem(this, name);
    }
}
