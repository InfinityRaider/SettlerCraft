package com.InfinityRaider.settlercraft.handler;

import com.InfinityRaider.settlercraft.settlement.settler.container.ContainerSettler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerTickHandler {
    private static final PlayerTickHandler INSTANCE = new PlayerTickHandler();

    public static PlayerTickHandler getInstance() {
        return INSTANCE;
    }

    private Map<UUID, ContainerSettler> containersToClose;

    private PlayerTickHandler() {
        this.containersToClose = new HashMap<>();
    }

    public void onContainerClosed(ContainerSettler container) {
        this.containersToClose.put(container.getPlayer().getUniqueID(), container);
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onTickEvent(TickEvent.PlayerTickEvent event) {
        if(containersToClose.containsKey(event.player.getUniqueID())) {
            containersToClose.get(event.player.getUniqueID()).afterContainerClosed();
            containersToClose.remove(event.player.getUniqueID());
        }
    }
}
