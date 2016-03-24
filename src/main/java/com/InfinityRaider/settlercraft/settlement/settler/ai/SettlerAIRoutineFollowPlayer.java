package com.InfinityRaider.settlercraft.settlement.settler.ai;

import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.api.v1.ITask;
import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class SettlerAIRoutineFollowPlayer extends SettlerAIRoutine {
    private EntityLivingBase theOwner;
    World theWorld;
    private double followSpeed;
    private PathNavigate petPathfinder;
    private int field_75343_h;
    private float waterPriority;
    float maxDist;
    float minDist;

    protected SettlerAIRoutineFollowPlayer(EntitySettler settler, double followSpeedIn, float minDistIn, float maxDistIn) {
        super(settler, ISettler.SettlerStatus.FOLLOWING_PLAYER);
        this.theWorld = settler.worldObj;
        this.followSpeed = followSpeedIn;
        this.petPathfinder = settler.getNavigator();
        this.minDist = minDistIn;
        this.maxDist = maxDistIn;
    }

    @Override
    public ITask getActiveTask() {
        return null;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
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
    @Override
    public boolean continueExecutingRoutine() {
        return !this.petPathfinder.noPath()
                && this.getSettler().getDistanceSqToEntity(this.theOwner) > (double) (this.maxDist * this.maxDist);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecutingRoutine() {
        this.field_75343_h = 0;
        this.waterPriority = getSettler().getPathPriority(PathNodeType.WATER);
        this.getSettler().setPathPriority(PathNodeType.WATER, 0.0F);
    }

    /**
     * Resets the task
     */
    @Override
    public void resetRoutine() {
        this.theOwner = null;
        this.petPathfinder.clearPathEntity();
        this.getSettler().setPathPriority(PathNodeType.WATER, this.waterPriority);
    }

    private boolean func_181065_a(BlockPos pos) {
        IBlockState state = this.theWorld.getBlockState(pos);
        Block block = state.getBlock();
        return block == Blocks.air || !block.isFullCube(state);
    }

    /**
     * Updates the task
     */
    @Override
    public void updateRoutine() {
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
                                if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.theWorld.getBlockState(new BlockPos(i + l, k - 1, j + i1)).isFullyOpaque() && this.func_181065_a(new BlockPos(i + l, k, j + i1)) && this.func_181065_a(new BlockPos(i + l, k + 1, j + i1))) {
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
}
