package com.infinityraide.settlercraft.handler;

import com.infinityraide.settlercraft.settlement.settler.container.ContainerSettler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerTickHandler {
    private static final PlayerTickHandler INSTANCE = new PlayerTickHandler();

    public static PlayerTickHandler getInstance() {
        return INSTANCE;
    }

    private Map<EntityPlayer, ContainerSettler> containersToClose;

    private PlayerTickHandler() {
        this.containersToClose = new HashMap<>();
    }

    public void onContainerClosed(ContainerSettler container) {
        this.containersToClose.put(container.getPlayer(), container);
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onTickEvent(TickEvent.PlayerTickEvent event) {
        if(containersToClose.containsKey(event.player)) {
            containersToClose.get(event.player).afterContainerClosed();
            containersToClose.remove(event.player);
        }
    }
}
