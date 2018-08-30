package com.infinityraider.settlercraft.settlement.settler.container.gui;

import com.infinityraider.settlercraft.reference.Reference;
import com.infinityraider.settlercraft.settlement.settler.container.ContainerSettlerInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSettlerInventory extends GuiSettler<ContainerSettlerInventory> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/gui/settlerInventory.png");

    public static final int SIZE_X = 252;
    public static final int SIZE_Y = 179;

    public static final int POSITION_PLAYER_X = 53;
    public static final int POSITION_PLAYER_Y = 167;

    public static final int POSITION_SETTLER_X = 53;
    public static final int POSITION_SETTLER_Y = 74;

    public static final int SCALE = 30;

    public GuiSettlerInventory(ContainerSettlerInventory container) {
        super(container);
        this.xSize = SIZE_X;
        this.ySize = SIZE_Y;
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        this.drawEntityOnScreen(POSITION_PLAYER_X, POSITION_PLAYER_Y, SCALE, mouseX, mouseY, getContainer().getPlayer());
        this.drawEntityOnScreen(POSITION_SETTLER_X, POSITION_SETTLER_Y, SCALE, mouseX, mouseY, getContainer().getSettler().getEntityImplementation());
    }

    protected void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityLivingBase ent) {
        GuiInventory.drawEntityOnScreen(this.guiLeft + posX, this.guiTop + posY, scale, this.guiLeft + posX - mouseX, this.guiTop + posY - mouseY, ent);
    }
}
