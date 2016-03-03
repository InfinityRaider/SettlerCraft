package com.InfinityRaider.settlercraft.registry;


import com.InfinityRaider.settlercraft.SettlerCraft;
import com.InfinityRaider.settlercraft.handler.ConfigurationHandler;
import com.InfinityRaider.settlercraft.reference.Names;
import com.InfinityRaider.settlercraft.reference.Reference;
import com.InfinityRaider.settlercraft.settlement.Settlement;
import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import com.InfinityRaider.settlercraft.utility.BiomeHelper;
import net.minecraft.entity.EnumCreatureType;

import java.awt.*;

public class EntityRegistry {
    private static final EntityRegistry INSTANCE = new EntityRegistry();

    public static EntityRegistry getInstance() {
        return INSTANCE;
    }

    private EntityRegistry() {}

    public final int ID_SETTLER = 0;
    public final int ID_SETTLEMENT = 1;

    public void init() {
        //Settler
        net.minecraftforge.fml.common.registry.EntityRegistry.registerModEntity(
                EntitySettler.class,
                Names.Entities.SETTLER, ID_SETTLER,
                SettlerCraft.instance, 64, 1, true);
        net.minecraftforge.fml.common.registry.EntityRegistry.registerEgg(
                EntitySettler.class, new Color(8, 8, 255).getRGB(),
                new Color(51, 255, 247).getRGB());
        net.minecraftforge.fml.common.registry.EntityRegistry.addSpawn(
                Reference.MOD_ID + "." + Names.Entities.SETTLER,
                ConfigurationHandler.getInstance().settlerSpawnWeight, 1, 1,
                EnumCreatureType.CREATURE,
                BiomeHelper.getInstance().convertBiomeNamesList(ConfigurationHandler.getInstance().settlerSpawnBiomes));

        //Settlement
        net.minecraftforge.fml.common.registry.EntityRegistry.registerModEntity(
                Settlement.class,
                Names.Entities.SETTLEMENT, ID_SETTLEMENT,
                SettlerCraft.instance, 64, 200, false);
    }
}
