package com.InfinityRaider.settlercraft.settlement.settler.container;

import com.InfinityRaider.settlercraft.api.v1.IInventorySettler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

public class ContainerSettlerInventory extends ContainerSettler {
    public static final int INVENTORY_SETTLER_X = 84;
    public static final int INVENTORY_SETTLER_Y = 8;

    public static final int INVENTORY_PLAYER_X = 84;
    public static final int INVENTORY_PLAYER_Y = 98;

    private final InventoryPlayer playerInventory;
    private final IInventorySettler settlerInventory;

    public ContainerSettlerInventory(EntityPlayer player) {
        super(player);
        this.playerInventory = getPlayer().inventory;
        this.settlerInventory = getSettler().getSettlerInventory();
        addSlotsToContainer();
    }

    private void addSlotsToContainer() {
        int xOffset = INVENTORY_SETTLER_X;
        int yOffset = INVENTORY_SETTLER_Y;
        // add settler inventory to the container
        for(int i=0;i<3;i++) {
            for(int j=0;j<9;j++) {
                //new Slot(inventory, slot index, x coordinate, y coordinate)
                this.addSlotToContainer(new Slot(settlerInventory, j+i*9+5, xOffset+j*18, yOffset+i*18));
            }
        }
        //add player's inventory to the container
        xOffset = INVENTORY_PLAYER_X;
        yOffset = INVENTORY_PLAYER_Y;
        for(int i=0;i<3;i++) {
            for(int j=0;j<9;j++) {
                //new Slot(inventory, slot index, x coordinate, y coordinate)
                this.addSlotToContainer(new Slot(playerInventory, j+i*9+9, xOffset+j*18, yOffset+i*18));
            }
        }
        //add player's hot bar to the container
        for(int i=0;i<9;i++) {
            //new Slot(inventory, slot index, x coordinate, y coordinate)
            this.addSlotToContainer(new Slot(playerInventory, i, xOffset + i*18, 58+yOffset));
        }
    }
}
