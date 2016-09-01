package com.InfinityRaider.settlercraft.settlement.settler;

import net.minecraft.util.EntityDamageSource;

public class DamageSourceSettler extends EntityDamageSource {
    public DamageSourceSettler(EntitySettler entity) {
        super("settler", entity);
    }

    @Override
    public EntitySettler getEntity() {
        return (EntitySettler) super.getEntity();
    }
}
