package com.InfinityRaider.settlercraft.settlement.settler.ai.routines;

import com.InfinityRaider.settlercraft.api.v1.ISettler;
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
    private int pathingTimer;
    private float waterPriority;
    float maxDist;
    float minDist;

    public SettlerAIRoutineFollowPlayer(EntitySettler settler, double followSpeedIn, float minDistIn, float maxDistIn) {
        super(settler, ISettler.SettlerStatus.FOLLOWING_PLAYER);
        this.theWorld = settler.getWorld();
        this.followSpeed = followSpeedIn;
        this.petPathfinder = settler.getNavigator();
        this.minDist = minDistIn;
        this.maxDist = maxDistIn;
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
        return this.theOwner != null;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecutingRoutine() {
        this.pathingTimer = 0;
        this.waterPriority = getSettler().getPathPriority(PathNodeType.WATER);
        this.getSettler().setPathPriority(PathNodeType.WATER, 0.0F);
    }

    /**
     * Resets the task
     */
    @Override
    public void interruptRoutine() {
        this.theOwner = null;
        this.petPathfinder.clearPathEntity();
        this.getSettler().setPathPriority(PathNodeType.WATER, this.waterPriority);
    }

    private boolean func_181065_a(BlockPos pos) {
        IBlockState state = this.theWorld.getBlockState(pos);
        Block block = state.getBlock();
        return block == Blocks.AIR || !block.isFullCube(state);
    }

    /**
     * Updates the task, copied from vanilla
     */
    @Override
    public void updateRoutine() {
        if(this.theOwner == null) {
            return;
        }
        if (this.getSettler().getDistanceSqToEntity(this.theOwner) < (double) (this.minDist * this.minDist)) {
            return;
        }
        this.getSettler().getLookHelper().setLookPositionWithEntity(this.theOwner, 10.0F, (float) this.getSettler().getVerticalFaceSpeed());
        if (--this.pathingTimer <= 0) {
            this.pathingTimer = 10;
            if (!this.petPathfinder.tryMoveToEntityLiving(this.theOwner, this.followSpeed)) {
                if (!this.getSettler().getLeashed()) {
                    if (this.getSettler().getDistanceSqToEntity(this.theOwner) >= 144.0D) {
                        int i = MathHelper.floor(this.theOwner.posX) - 2;
                        int j = MathHelper.floor(this.theOwner.posZ) - 2;
                        int k = MathHelper.floor(this.theOwner.getEntityBoundingBox().minY);
                        for (int l = 0; l <= 4; ++l) {
                            for (int i1 = 0; i1 <= 4; ++i1) {
                                if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.theWorld.getBlockState(new BlockPos(i + l, k - 1, j + i1)).isOpaqueCube() && this.func_181065_a(new BlockPos(i + l, k, j + i1)) && this.func_181065_a(new BlockPos(i + l, k + 1, j + i1))) {
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
