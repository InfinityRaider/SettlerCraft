package com.InfinityRaider.settlercraft.settlement.settler.container.gui;

import com.InfinityRaider.settlercraft.settlement.settler.container.ContainerBuildNewBuilding;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiBuildNewBuilding extends GuiSettler<ContainerBuildNewBuilding> {
    public GuiBuildNewBuilding(ContainerBuildNewBuilding container) {
        super(container);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

    }
}
