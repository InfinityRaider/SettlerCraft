package com.infinityraider.settlercraft.settlement.settler.ai;

import com.infinityraider.settlercraft.api.v1.ISettler;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.Vec3d;

public class EntityAIAimAtTarget extends EntityAIBase {
    private final ISettler settler;

    public EntityAIAimAtTarget(ISettler entity) {
        this.settler = entity;
        this.setMutexBits(4);
    }

    public EntityLiving getEntity() {
        return this.settler.getEntityImplementation();
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        return settler.getLookTarget() != null;
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
        if (this.settler.getLookTarget() == null) {
            return;
        }
        Vec3d target = this.settler.getLookTarget().getTarget(this.settler);
        this.getEntity().getLookHelper().setLookPosition(target.x, target.y, target.z,
                (float) this.getEntity().getHorizontalFaceSpeed(), (float) this.getEntity().getVerticalFaceSpeed());
    }
}
