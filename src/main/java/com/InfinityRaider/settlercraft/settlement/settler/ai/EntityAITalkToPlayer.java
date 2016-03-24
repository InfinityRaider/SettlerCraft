package com.InfinityRaider.settlercraft.settlement.settler.ai;

import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAITalkToPlayer extends EntityAIBase {
    private final EntitySettler settler;

    public EntityAITalkToPlayer(EntitySettler settler) {
        this.settler = settler;
        this.setMutexBits(5);
    }

    public EntitySettler getSettler() {
        return settler;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        if (!this.getSettler().isEntityAlive())  {
            return false;
        }
        else if (this.getSettler().isInWater()) {
            return false;
        }
        else if (!this.getSettler().onGround) {
            return false;
        }
        else if (this.getSettler().velocityChanged) {
            return false;
        }
        else {
            EntityPlayer player = this.getSettler().getConversationPartner();
            return player != null && (this.getSettler().getDistanceSqToEntity(player) <= 16.0D && player.openContainer != null);
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting() {
        this.getSettler().getNavigator().clearPathEntity();
    }

    /**
     * Resets the task
     */
    @Override
    public void resetTask() {
        EntityPlayer player = this.getSettler().getConversationPartner();
        //Can be null if the container was closed from within the conversation
        if(player != null) {
            player.closeScreen();
        }
    }
}
