package com.InfinityRaider.settlercraft.handler;

import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SettlerTargetingHandler {
    private static final SettlerTargetingHandler INSTANCE = new SettlerTargetingHandler();

    public static SettlerTargetingHandler getInstance() {
        return INSTANCE;
    }

    private SettlerTargetingHandler() {}

    @SubscribeEvent
    @SuppressWarnings({"unused", "unchecked"})
    public void onZombieSpawn(LivingSpawnEvent event) {
        if(!(event.getEntity() instanceof EntityCreature)) {
            return;
        }
        EntityCreature entity = (EntityCreature) event.getEntity();
        if(entity instanceof EntityZombie) {
            entity.targetTasks.addTask(2, new EntityAINearestAttackableTarget(entity, EntitySettler.class, true));
        }
        else if(entity instanceof EntitySkeleton) {
            entity.targetTasks.addTask(2, new EntityAINearestAttackableTarget(entity, EntitySettler.class, true));
        }
    }
}
