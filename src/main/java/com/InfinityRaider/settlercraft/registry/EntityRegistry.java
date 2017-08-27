package com.InfinityRaider.settlercraft.registry;

import com.InfinityRaider.settlercraft.api.v1.ISettlerCraftEntityRegistry;
import com.InfinityRaider.settlercraft.handler.ConfigurationHandler;
import com.InfinityRaider.settlercraft.reference.Reference;
import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import com.InfinityRaider.settlercraft.utility.BiomeHelper;
import com.infinityraider.infinitylib.entity.EntityRegistryEntry;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityMob;

public class EntityRegistry implements ISettlerCraftEntityRegistry {
    private static final EntityRegistry INSTANCE = new EntityRegistry();

    public static EntityRegistry getInstance() {
        return INSTANCE;
    }

    public static final String SETTLER = "entity.settler";

    @SuppressWarnings("unchecked")
    private EntityRegistry() {
        this.settlerEntityRegistryEntry = new EntityRegistryEntry<>(EntitySettler.class, SETTLER)
                .setTrackingDistance(64)
                .setUpdateFrequency(1)
                .setVelocityUpdates(true)
                .setSpawnEgg(8, 8, 255, 51, 255, 247)
                .setCreatureSpawn(1, 2, ConfigurationHandler.getInstance().settlerSpawnWeight, EnumCreatureType.CREATURE,
                        BiomeHelper.getInstance().convertBiomeNamesList(ConfigurationHandler.getInstance().settlerSpawnBiomes))
                .setRenderFactory(EntitySettler.RENDER_FACTORY)
                .setEntityTargetedBy(EntityMob.class);
    }

    public final EntityRegistryEntry<EntitySettler> settlerEntityRegistryEntry;

    @Override
    public Class<? extends EntityAgeable> entitySettlerClass() {
        return EntitySettler.class;
    }

    @Override
    public String entitySettlerId() {
        return  Reference.MOD_ID + "." + SETTLER;
    }
}
