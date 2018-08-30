package com.infinityraide.settlercraft.settlement.settler;

import net.minecraft.util.EntityDamageSource;

public class DamageSourceSettler extends EntityDamageSource {
    public DamageSourceSettler(EntitySettler entity) {
        super("settler", entity);
    }

    @Override
    public EntitySettler getTrueSource() {
        return (EntitySettler) super.getTrueSource();
    }
}
