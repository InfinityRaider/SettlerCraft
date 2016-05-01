package com.InfinityRaider.settlercraft.settlement.settler;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;

import java.util.UUID;

public class EntitySettlerFakePlayer extends FakePlayer {
    private final EntitySettler settler;

    public EntitySettlerFakePlayer(WorldServer server, EntitySettler settler) {
        super(server, new GameProfile(UUID.fromString(settler.getFirstName() + " " + settler.getSurname()), settler.getFirstName() + " " + settler.getSurname()));
        this.settler = settler;
    }

    @Override
    public Vec3d getPositionVector() {
        return settler.getPositionVector();
    }

    @Override public boolean canAttackPlayer(EntityPlayer player){
        return true;
    }

    @Override public Entity changeDimension(int dim) {
        return settler.changeDimension(dim);
    }
}