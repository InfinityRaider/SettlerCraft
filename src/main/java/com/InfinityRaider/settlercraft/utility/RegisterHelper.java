package com.InfinityRaider.settlercraft.utility;

import com.InfinityRaider.settlercraft.reference.Reference;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class RegisterHelper {

    public static void registerItem(Item item,String name) {
        item.setUnlocalizedName(Reference.MOD_ID.toLowerCase()+':'+name);
        LogHelper.info("registering " + item.getUnlocalizedName());
        GameRegistry.registerItem(item, name);
    }
}
