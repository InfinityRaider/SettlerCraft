package com.InfinityRaider.settlercraft.settlement.settler.container.gui;


import com.InfinityRaider.settlercraft.settlement.settler.container.ContainerSettler;
import net.minecraft.client.gui.inventory.GuiContainer;

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
