package com.InfinityRaider.settlercraft.handler;

import com.InfinityRaider.settlercraft.SettlerCraft;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.settlement.settler.container.ContainerSettlerDialogue;
import com.InfinityRaider.settlercraft.settlement.settler.container.ContainerSettlerInventory;
import com.InfinityRaider.settlercraft.settlement.settler.container.gui.GuiSettlerDialogue;
import com.InfinityRaider.settlercraft.settlement.settler.container.gui.GuiSettlerInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandlerSettler implements IGuiHandler {
    private static final GuiHandlerSettler INSTANCE = new GuiHandlerSettler();

    public static GuiHandlerSettler getInstance() {
        return INSTANCE;
    }

    private final int SETTLER_DIALOGUE_ID = 0;
    private final int SETTLER_INVENTORY_ID = 1;

    private GuiHandlerSettler() {}

    public void openSettlerDialogueContainer(EntityPlayer player, ISettler settler) {
        openContainer(player, settler, SETTLER_DIALOGUE_ID);
    }

    public void openSettlerInventoryContainer(EntityPlayer player, ISettler settler) {
        openContainer(player, settler, SETTLER_INVENTORY_ID);

    }

    private void openContainer(EntityPlayer player, ISettler settler, int id) {
        player.openGui(SettlerCraft.instance, id, player.getEntityWorld(), settler.getEntityImplementation().getEntityId(), 0, 0);
    }

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        Entity entity = world.getEntityByID(x);
        if(entity != null && (entity instanceof ISettler)) {
            ISettler settler = (ISettler) entity;
            switch (id) {
                case (SETTLER_DIALOGUE_ID):
                    return new ContainerSettlerDialogue(player, settler);
                case (SETTLER_INVENTORY_ID):
                    return new ContainerSettlerInventory(player, settler);
            }
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        Entity entity = world.getEntityByID(x);
        if(entity != null && (entity instanceof ISettler)) {
            ISettler settler = (ISettler) entity;
            switch (id) {
                case (SETTLER_DIALOGUE_ID):
                    return new GuiSettlerDialogue(new ContainerSettlerDialogue(player, settler));
                case (SETTLER_INVENTORY_ID):
                    return new GuiSettlerInventory(new ContainerSettlerInventory(player, settler));
            }
        }
        return null;
    }
}
