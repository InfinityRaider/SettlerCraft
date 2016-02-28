package com.InfinityRaider.settlercraft.settlement.settler.ai;

import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAITalkToPlayer extends EntityAIBase {
    private EntitySettler settler;

    public EntityAITalkToPlayer(EntitySettler settler) {
        this.settler = settler;
        this.setMutexBits(5);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (!this.settler.isEntityAlive())  {
            return false;
        }
        else if (this.settler.isInWater()) {
            return false;
        }
        else if (!this.settler.onGround) {
            return false;
        }
        else if (this.settler.velocityChanged) {
            return false;
        }
        else {
            EntityPlayer player = this.settler.getConversationPartner();
            return player != null && (this.settler.getDistanceSqToEntity(player) <= 16.0D && player.openContainer != null);
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.settler.getNavigator().clearPathEntity();
    }

    /**
     * Resets the task
     */
    public void resetTask() {
        EntityPlayer player = this.settler.getConversationPartner();
        //Can be null if the container was closed from within the conversation
        if(player != null) {
            player.closeScreen();
        }
    }
}
