package com.InfinityRaider.settlercraft.settlement.settler.container;

import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.settlement.SettlementHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

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
    
    public void closeContainer() {
        player.closeScreen();
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        SettlementHandler.getInstance().stopInteractingWithSettler(player);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.player.getUniqueID().equals(player.getUniqueID());
    }
}
