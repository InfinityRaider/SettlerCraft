package com.InfinityRaider.settlercraft.handler;

import com.InfinityRaider.settlercraft.SettlerCraft;
import com.InfinityRaider.settlercraft.settlement.settler.container.ContainerSettlerDialogue;
import com.InfinityRaider.settlercraft.settlement.settler.container.ContainerSettlerInventory;
import com.InfinityRaider.settlercraft.settlement.settler.container.gui.GuiSettlerDialogue;
import com.InfinityRaider.settlercraft.settlement.settler.container.gui.GuiSettlerInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
    private static final GuiHandler INSTANCE = new GuiHandler();

    public static GuiHandler getInstance() {
        return INSTANCE;
    }

    private final int SETTLER_DIALOGUE_ID = 0;
    private final int SETTLER_INVENTORY_ID = 1;

    private GuiHandler() {}

    public void openSettlerDialogueContainer(EntityPlayer player) {
        openContainer(player, SETTLER_DIALOGUE_ID);
    }

    public void openSettlerInventoryContainer(EntityPlayer player) {
        openContainer(player, SETTLER_INVENTORY_ID);

    }

    private void openContainer(EntityPlayer player, int id) {
        player.openGui(SettlerCraft.instance, id, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
    }

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        switch(id) {
            case(SETTLER_DIALOGUE_ID):
                return new ContainerSettlerDialogue(player);
            case(SETTLER_INVENTORY_ID):
                return new ContainerSettlerInventory(player);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        switch(id) {
            case(SETTLER_DIALOGUE_ID):
                return new GuiSettlerDialogue(new ContainerSettlerDialogue(player));
            case(SETTLER_INVENTORY_ID):
                return new GuiSettlerInventory(new ContainerSettlerInventory(player));
        }
        return null;
    }
}
