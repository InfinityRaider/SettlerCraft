package com.InfinityRaider.settlercraft.settlement.settler.ai;

import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAITalkToPlayer extends EntityAISettler {
    public EntityAITalkToPlayer(EntitySettler settler) {
        super(settler);
        this.setMutexBits(5);
    }

    @Override
    public ISettler.SettlerStatus getStatusForRoutine() {
        return getSettler().getSettlerStatus();
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecuteRoutine() {
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
    public void startExecutingRoutine() {
        this.getSettler().getNavigator().clearPathEntity();
    }

    /**
     * Resets the task
     */
    public void resetTask() {
        EntityPlayer player = this.getSettler().getConversationPartner();
        //Can be null if the container was closed from within the conversation
        if(player != null) {
            player.closeScreen();
        }
    }
}
