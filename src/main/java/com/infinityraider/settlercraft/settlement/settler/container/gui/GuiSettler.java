package com.infinityraider.settlercraft.settlement.settler.container.gui;


import com.infinityraider.settlercraft.settlement.settler.container.ContainerSettler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class GuiSettler<T  extends ContainerSettler> extends GuiContainer {
    private final T container;

    public GuiSettler(T container) {
        super(container);
        this.container = container;
    }

    protected T getContainer() {
        return container;
    }
}
