package com.InfinityRaider.settlercraft.settlement.settler.container;

import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.settlement.SettlementHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;

public abstract class ContainerSettler extends Container {
    private final ISettler settler;
    private final EntityPlayer player;

    public ContainerSettler(EntityPlayer player) {
        this.settler = SettlementHandler.getInstance().getSettlerInteractingWith(player);
        this.player = player;
    }

    public ISettler getSettler() {
        return settler;
    }

    public EntityPlayer getPlayer() {
        return player;
    }
    
    public final void closeContainer() {
        //Really important to close container from client side, weird bugs happen if this check is not here
        if(player.worldObj.isRemote) {
            player.closeScreen();
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        if(stopInteracting()) {
            SettlementHandler.getInstance().stopInteractingWithSettler(player);
        }
        SettlementHandler.getInstance().onContainerClosed(this);
    }

    @Override
    public void onCraftGuiOpened(ICrafting listener) {
        super.onCraftGuiOpened(listener);
    }

    public final void afterContainerClosed() {
        this.onContainerClosed(player, settler);
    }

    protected abstract void onContainerClosed(EntityPlayer player, ISettler settler);

    protected abstract boolean stopInteracting();

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.player.getUniqueID().equals(player.getUniqueID());
    }
}