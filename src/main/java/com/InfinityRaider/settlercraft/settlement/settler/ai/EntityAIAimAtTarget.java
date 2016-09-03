package com.InfinityRaider.settlercraft.settlement.settler.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.Vec3d;

public class EntityAIAimAtTarget extends EntityAIBase {
    private final EntityLiving entity;
    private final PathNavigate petPathfinder;
    private final double moveSpeed;
    private final double rangeSq;
    private float waterPriority;
    private int pathingTimer;
    private Vec3d target;

    public EntityAIAimAtTarget(EntityLiving entity, double moveSpeed, double range) {
        this.entity = entity;
        this.petPathfinder = entity.getNavigator();
        this.moveSpeed = moveSpeed;
        this.rangeSq = range * range;
        this.setMutexBits(4);
    }

    public EntityLiving getEntity() {
        return this.entity;
    }

    public void setTarget(Vec3d target) {
        this.target = target;
    }

    public Vec3d getTarget() {
        return this.target;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        return target != null;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting() {
        return shouldExecute();
    }

    /**
     * Determine if this AI Task is interruptible by a higher (= lower value) priority task. All vanilla AITasks have
     * this value set to true.
     */
    @Override
    public boolean isInterruptible() {
        return true;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting() {
        this.pathingTimer = 0;
        this.waterPriority = getEntity().getPathPriority(PathNodeType.WATER);
        this.getEntity().setPathPriority(PathNodeType.WATER, 0.0F);
    }

    /**
     * Resets the task
     */
    @Override
    public void resetTask() {
        this.petPathfinder.clearPathEntity();
        this.getEntity().setPathPriority(PathNodeType.WATER, this.waterPriority);}

    /**
     * Updates the task
     */
    @Override
    public void updateTask() {
        if (this.getTarget() == null) {
            return;
        }
        if (this.getEntity().getDistanceSq(this.getTarget().xCoord, this.getTarget().yCoord, this.getTarget().zCoord) <= this.rangeSq) {
            this.getEntity().getLookHelper().setLookPosition(this.getTarget().xCoord, this.getTarget().yCoord, this.getTarget().zCoord,
                    (float) this.getEntity().getHorizontalFaceSpeed(), (float) this.getEntity().getVerticalFaceSpeed());
        } else {
            if (--this.pathingTimer <= 0) {
                this.pathingTimer = 10;
                if (!this.getEntity().getLeashed()) {
                    this.petPathfinder.tryMoveToXYZ(this.getTarget().xCoord, this.getTarget().yCoord, this.getTarget().zCoord, this.moveSpeed);
                }
            }
        }
    }
}
