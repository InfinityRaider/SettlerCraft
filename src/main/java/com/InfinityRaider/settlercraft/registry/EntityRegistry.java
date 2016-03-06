package com.InfinityRaider.settlercraft.registry;


import com.InfinityRaider.settlercraft.SettlerCraft;
import com.InfinityRaider.settlercraft.handler.ConfigurationHandler;
import com.InfinityRaider.settlercraft.reference.Reference;
import com.InfinityRaider.settlercraft.settlement.Settlement;
import com.InfinityRaider.settlercraft.settlement.SettlementBuildingComplete;
import com.InfinityRaider.settlercraft.settlement.SettlementBuildingIncomplete;
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

    public static final String SETTLER = "entity.settler";
    public static final String SETTLEMENT = "settlement";
    public static final String BUILDING_COMPLETE = "building_complete";
    public static final String BUILDING_INCOMPLETE = "building_incomplete";

    public final int ID_SETTLER = 0;
    public final int ID_SETTLEMENT = 1;
    public final int ID_BUILDING_COMPLETE = 2;
    public final int ID_BUILDING_INCOMPLETE = 3;

    public void init() {
        //Settler
        net.minecraftforge.fml.common.registry.EntityRegistry.registerModEntity(
                EntitySettler.class,
                SETTLER, ID_SETTLER,
                SettlerCraft.instance, 64, 1, true);
        net.minecraftforge.fml.common.registry.EntityRegistry.registerEgg(
                EntitySettler.class, new Color(8, 8, 255).getRGB(),
                new Color(51, 255, 247).getRGB());
        net.minecraftforge.fml.common.registry.EntityRegistry.addSpawn(
                Reference.MOD_ID + "." + SETTLER,
                ConfigurationHandler.getInstance().settlerSpawnWeight, 1, 1,
                EnumCreatureType.CREATURE,
                BiomeHelper.getInstance().convertBiomeNamesList(ConfigurationHandler.getInstance().settlerSpawnBiomes));

        //Settlement
        net.minecraftforge.fml.common.registry.EntityRegistry.registerModEntity(
                Settlement.class,
                SETTLEMENT, ID_SETTLEMENT,
                SettlerCraft.instance, 64, 200, false);

        //Buildings
        net.minecraftforge.fml.common.registry.EntityRegistry.registerModEntity(
                SettlementBuildingComplete.class,
                BUILDING_COMPLETE, ID_BUILDING_COMPLETE,
                SettlerCraft.instance, 64, 200, false);
        net.minecraftforge.fml.common.registry.EntityRegistry.registerModEntity(
                SettlementBuildingIncomplete.class,
                BUILDING_INCOMPLETE, ID_BUILDING_INCOMPLETE,
                SettlerCraft.instance, 64, 200, false);

    }
}
