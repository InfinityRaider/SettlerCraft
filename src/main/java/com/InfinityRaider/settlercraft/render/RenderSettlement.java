package com.InfinityRaider.settlercraft.render;

import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.api.v1.ISettlementBuilding;
import com.InfinityRaider.settlercraft.api.v1.IItemRenderSettlementBoxes;
import com.InfinityRaider.settlercraft.settlement.SettlementHandler;
import com.InfinityRaider.settlercraft.settlement.settler.ai.pathfinding.astar.RenderPath;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

/**
 * This class handles all the rendering of settlement bounding boxes based upon the item in the player's hand
 */
@SideOnly(Side.CLIENT)
public class RenderSettlement extends RenderUtilBase {
    private static final RenderSettlement INSTANCE = new RenderSettlement();

    public static RenderSettlement getInstance() {
        return INSTANCE;
    }

    public static final Color SETTLEMENT_COLOR = new Color(255, 217, 49);
    public static final Color BUILDING_COLOR = new Color(12, 14, 255);

    private RenderSettlement() {
        super();
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void renderSchematicOverlay(RenderWorldLastEvent event) {
        RenderPath.getInstance().renderPath(event);

        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if(player == null) {
            return;
        }

        ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
        if(stack == null || stack.getItem() == null || !(stack.getItem() instanceof IItemRenderSettlementBoxes)) {
            stack = player.getHeldItem(EnumHand.OFF_HAND);
        }
        if(stack == null || stack.getItem() == null || !(stack.getItem() instanceof IItemRenderSettlementBoxes)) {
            return;
        }

        final ItemStack finalStack = stack;
        IItemRenderSettlementBoxes item = (IItemRenderSettlementBoxes) finalStack.getItem();

        GL11.glPushMatrix();

        float partialTick = event.getPartialTicks();
        Tessellator tessellator = Tessellator.getInstance();

        double posX = player.prevPosX + (player.posX - player.prevPosX)*partialTick;
        double posY = player.prevPosY + (player.posY - player.prevPosY)*partialTick;
        double posZ = player.prevPosZ + (player.posZ - player.prevPosZ)*partialTick;

        GL11.glTranslated(-posX, -posY, -posZ);

        SettlementHandler handler = SettlementHandler.getInstance();
        List<ISettlement> settlementList = handler.getSettlementsForWorld(Minecraft.getMinecraft().theWorld);

        SettlementHandler.getInstance().getSettlementsForWorld(Minecraft.getMinecraft().theWorld).stream().filter(
                settlement -> item.shouldRenderSettlementBoxes(settlement, player, finalStack)).forEach(
                settlement -> renderSettlementBoundingBoxes(tessellator, settlement));

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
