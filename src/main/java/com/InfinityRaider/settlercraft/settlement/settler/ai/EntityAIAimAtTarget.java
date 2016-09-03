package com.InfinityRaider.settlercraft.settlement.settler.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.Vec3d;

public class EntityAIAimAtTarget extends EntityAIBase {
    private final EntityLiving entity;

    private Vec3d target;

    public EntityAIAimAtTarget(EntityLiving entity) {
        this.entity = entity;
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
    public void startExecuting() {}

    /**
     * Resets the task
     */
    @Override
    public void resetTask() {}

    /**
     * Updates the task
     */
    @Override
    public void updateTask() {
        if (this.getTarget() == null) {
            return;
        }
        this.getEntity().getLookHelper().setLookPosition(this.getTarget().xCoord, this.getTarget().yCoord, this.getTarget().zCoord,
                (float) this.getEntity().getHorizontalFaceSpeed(), (float) this.getEntity().getVerticalFaceSpeed());
    }
}
