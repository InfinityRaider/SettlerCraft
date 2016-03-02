package com.InfinityRaider.settlercraft.render;

import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.api.v1.ISettlementBuilding;
import com.InfinityRaider.settlercraft.item.ItemDebugger;
import com.InfinityRaider.settlercraft.settlement.SettlementHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class RenderSettlement {
    private static final RenderSettlement INSTANCE = new RenderSettlement();

    public static RenderSettlement getInstance() {
        return INSTANCE;
    }

    public static final Color SETTLEMENT_COLOR = new Color(255, 217, 49);
    public static final Color BUILDING_COLOR = new Color(12, 14, 255);

    private RenderSettlement() {}
    @SubscribeEvent
    @SuppressWarnings("unused")
    public void renderSchematicOverlay(RenderHandEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if(player == null) {
            return;
        }
        ItemStack stack = player.getCurrentEquippedItem();
        if(stack == null || stack.getItem() == null || !(stack.getItem() instanceof ItemDebugger)) {
            return;
        }

        Tessellator tessellator = Tessellator.getInstance();
        GL11.glPushMatrix();

        double posX = player.prevPosX + (player.posX - player.prevPosX)*event.partialTicks;
        double posY = player.prevPosY + (player.posY - player.prevPosY)*event.partialTicks;
        double posZ = player.prevPosZ + (player.posZ - player.prevPosZ)*event.partialTicks;

        GL11.glTranslated(-posX, -posY, -posZ);

        for(ISettlement settlement : SettlementHandler.getInstance().getSettlementsForWorld(Minecraft.getMinecraft().theWorld)) {
            renderSettlementBoundingBoxes(tessellator, settlement);
        }

        GL11.glTranslated(posX, posY, posZ);

        GL11.glPopMatrix();
    }

    public void renderSettlementBoundingBoxes(Tessellator tessellator, ISettlement settlement) {
        settlement.getBoundingBox().renderWireFrame(tessellator, SETTLEMENT_COLOR);
        for(ISettlementBuilding building : settlement.getBuildings()) {
            building.getBoundingBox().renderWireFrame(tessellator, BUILDING_COLOR);
        }
    }
}
