package com.InfinityRaider.settlercraft.settlement.settler.ai;

import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityAIFollowPlayer extends EntityAISettler {
    private EntityLivingBase theOwner;
    World theWorld;
    private double followSpeed;
    private PathNavigate petPathfinder;
    private int field_75343_h;
    float maxDist;
    float minDist;

    public EntityAIFollowPlayer(EntitySettler settler, double followSpeedIn, float minDistIn, float maxDistIn) {
        super(settler);
        this.theWorld = settler.worldObj;
        this.followSpeed = followSpeedIn;
        this.petPathfinder = settler.getNavigator();
        this.minDist = minDistIn;
        this.maxDist = maxDistIn;
        this.setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecuteRoutine() {
        EntityPlayer player = this.getSettler().getCurrentlyFollowingPlayer();
        if (player == null) {
            return false;
        } else if (player.isSpectator()) {
            return false;
        } else if (this.getSettler().getDistanceSqToEntity(player) < (double) (this.minDist * this.minDist)) {
            return false;
        } else {
            this.theOwner = player;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecutingRoutine() {
        return !this.petPathfinder.noPath()
                && this.getSettler().getDistanceSqToEntity(this.theOwner) > (double) (this.maxDist * this.maxDist);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecutingRoutine() {
        this.field_75343_h = 0;
        ((PathNavigateGround) this.getSettler().getNavigator()).setAvoidsWater(false);
    }

    /**
     * Resets the task
     */
    public void resetTask() {
        this.theOwner = null;
        this.petPathfinder.clearPathEntity();
        ((PathNavigateGround) this.getSettler().getNavigator()).setAvoidsWater(true);
    }

    private boolean func_181065_a(BlockPos pos) {
        IBlockState iblockstate = this.theWorld.getBlockState(pos);
        Block block = iblockstate.getBlock();
        return block == Blocks.air || !block.isFullCube();
    }

    /**
     * Updates the task
     */
    public void updateTask() {
        this.getSettler().getLookHelper().setLookPositionWithEntity(this.theOwner, 10.0F, (float) this.getSettler().getVerticalFaceSpeed());
        if (--this.field_75343_h <= 0) {
            this.field_75343_h = 10;
            if (!this.petPathfinder.tryMoveToEntityLiving(this.theOwner, this.followSpeed)) {
                if (!this.getSettler().getLeashed()) {
                    if (this.getSettler().getDistanceSqToEntity(this.theOwner) >= 144.0D) {
                        int i = MathHelper.floor_double(this.theOwner.posX) - 2;
                        int j = MathHelper.floor_double(this.theOwner.posZ) - 2;
                        int k = MathHelper.floor_double(this.theOwner.getEntityBoundingBox().minY);
                        for (int l = 0; l <= 4; ++l) {
                            for (int i1 = 0; i1 <= 4; ++i1) {
                                if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && World.doesBlockHaveSolidTopSurface(this.theWorld, new BlockPos(i + l, k - 1, j + i1)) && this.func_181065_a(new BlockPos(i + l, k, j + i1)) && this.func_181065_a(new BlockPos(i + l, k + 1, j + i1))) {
                                    this.getSettler().setLocationAndAngles((double) ((float) (i + l) + 0.5F), (double) k, (double) ((float) (j + i1) + 0.5F), this.getSettler().rotationYaw, this.getSettler().rotationPitch);
                                    this.petPathfinder.clearPathEntity();
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public ISettler.SettlerStatus getStatusForRoutine() {
        return ISettler.SettlerStatus.FOLLOWING_PLAYER;
    }
}
