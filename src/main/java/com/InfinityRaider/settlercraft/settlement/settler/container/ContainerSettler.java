package com.InfinityRaider.settlercraft.settlement.settler.container;

import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.handler.PlayerTickHandler;
import com.InfinityRaider.settlercraft.network.MessageCloseContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;

public abstract class ContainerSettler extends Container {
    private final ISettler settler;
    private final EntityPlayer player;

    public ContainerSettler(EntityPlayer player, ISettler settler) {
        this.settler = settler;
        this.player = player;
    }

    public ISettler getSettler() {
        return settler;
    }

    public EntityPlayer getPlayer() {
        return player;
    }
    
    public final void closeContainer() {
        //Really important to close container from client side, else weird bugs happen
        if(!player.worldObj.isRemote) {
            if(player instanceof EntityPlayerMP) {
                new MessageCloseContainer().sendTo((EntityPlayerMP) player);
            }
        } else {
            player.closeScreen();
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        this.settler.setConversationPartner(null);
        PlayerTickHandler.getInstance().onContainerClosed(this);
    }

    public final void afterContainerClosed() {
        this.onContainerClosed(player, settler);
    }

    protected abstract void onContainerClosed(EntityPlayer player, ISettler settler);

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.player.getUniqueID().equals(player.getUniqueID());
    }
}