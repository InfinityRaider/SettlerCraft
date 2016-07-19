package com.InfinityRaider.settlercraft.settlement.settler;

import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;

import java.util.List;
import java.util.UUID;

public class EntitySettlerFakePlayer extends FakePlayer {
    private final ISettler settler;

    public EntitySettlerFakePlayer(WorldServer server, ISettler settler) {
        super(server, new GameProfile(UUID.fromString(settler.getFirstName() + " " + settler.getSurname()), settler.getFirstName() + " " + settler.getSurname()));
        this.settler = settler;
    }

    @Override
    public boolean isPlayerSleeping() {
        return settler.isSleeping();
    }

    @Override
    public Vec3d getPositionVector() {
        return settler.getEntityImplementation().getPositionVector();
    }

    @Override
    public boolean canAttackPlayer(EntityPlayer player){
        return true;
    }

    @Override
    public Entity changeDimension(int dim) {
        return settler.getEntityImplementation().changeDimension(dim);
    }

    @Override
    public EnumStatus trySleep(BlockPos bed) {
        return this.trySleepVanilla(bed);
    }

    private EnumStatus trySleepVanilla(BlockPos bedLocation) {
        EntityPlayer.EnumStatus ret = net.minecraftforge.event.ForgeEventFactory.onPlayerSleepInBed(this, bedLocation);
        if (ret != null) return ret;
        if (!this.worldObj.isRemote) {
            if (this.isPlayerSleeping() || !this.isEntityAlive()) {
                return EntityPlayer.EnumStatus.OTHER_PROBLEM;
            }
            if (!this.worldObj.provider.isSurfaceWorld()) {
                return EntityPlayer.EnumStatus.NOT_POSSIBLE_HERE;
            }
            if (this.worldObj.isDaytime()) {
                return EntityPlayer.EnumStatus.NOT_POSSIBLE_NOW;
            }
            if (Math.abs(this.posX - (double) bedLocation.getX()) > 3.0D || Math.abs(this.posY - (double) bedLocation.getY()) > 2.0D || Math.abs(this.posZ - (double) bedLocation.getZ()) > 3.0D) {
                return EntityPlayer.EnumStatus.TOO_FAR_AWAY;
            }
            double d0 = 8.0D;
            double d1 = 5.0D;
            List<EntityMob> list = this.worldObj.getEntitiesWithinAABB(EntityMob.class,
                    new AxisAlignedBB((double) bedLocation.getX() - d0, (double) bedLocation.getY() - d1, (double) bedLocation.getZ() - d0,
                            (double) bedLocation.getX() + d0, (double) bedLocation.getY() + d1, (double) bedLocation.getZ() + d0));
            if (!list.isEmpty()) {
                return EntityPlayer.EnumStatus.NOT_SAFE;
            }
        }
        if (this.isRiding()) {
            this.dismountRidingEntity();
        }
        this.setSize(0.2F, 0.2F);
        IBlockState state = null;
        if (this.worldObj.isBlockLoaded(bedLocation)) state = this.worldObj.getBlockState(bedLocation);
        if (state != null && state.getBlock().isBed(state, this.worldObj, bedLocation, this)) {
            EnumFacing enumfacing = state.getBlock().getBedDirection(state, this.worldObj, bedLocation);
            float f = 0.5F;
            float f1 = 0.5F;
            switch (enumfacing) {
                case SOUTH:
                    f1 = 0.9F;
                    break;
                case NORTH:
                    f1 = 0.1F;
                    break;
                case WEST:
                    f = 0.1F;
                    break;
                case EAST:
                    f = 0.9F;
            }
            this.setRenderOffsetForSleep(enumfacing);
            this.setPosition((double) ((float) bedLocation.getX() + f), (double) ((float) bedLocation.getY() + 0.6875F), (double) ((float) bedLocation.getZ() + f1));
        } else {
            this.setPosition((double) ((float) bedLocation.getX() + 0.5F), (double) ((float) bedLocation.getY() + 0.6875F), (double) ((float) bedLocation.getZ() + 0.5F));
        }
        this.sleeping = true;
        this.playerLocation = bedLocation;
        this.motionX = this.motionZ = this.motionY = 0.0D;
        if (!this.worldObj.isRemote) {
            this.worldObj.updateAllPlayersSleepingFlag();
        }
        return EntityPlayer.EnumStatus.OK;
    }

    private void setRenderOffsetForSleep(EnumFacing facing) {
        this.renderOffsetX = 0.0F;
        this.renderOffsetZ = 0.0F;
        switch (facing) {
            case SOUTH:
                this.renderOffsetZ = -1.8F;
                break;
            case NORTH:
                this.renderOffsetZ = 1.8F;
                break;
            case WEST:
                this.renderOffsetX = 1.8F;
                break;
            case EAST:
                this.renderOffsetX = -1.8F;
        }
    }
}