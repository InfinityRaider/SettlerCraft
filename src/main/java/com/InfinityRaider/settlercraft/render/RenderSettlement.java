package com.InfinityRaider.settlercraft.render;

import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.api.v1.ISettlementBuilding;
import com.InfinityRaider.settlercraft.api.v1.IItemRenderSettlementBoxes;
import com.InfinityRaider.settlercraft.settlement.SettlementHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

@SideOnly(Side.CLIENT)
public class RenderSettlement extends RenderBase {
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
    public void renderSchematicOverlay(RenderHandEvent event) {
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

        Tessellator tessellator = Tessellator.getInstance();
        GL11.glPushMatrix();

        double posX = player.prevPosX + (player.posX - player.prevPosX)*event.partialTicks;
        double posY = player.prevPosY + (player.posY - player.prevPosY)*event.partialTicks + player.getEyeHeight();
        double posZ = player.prevPosZ + (player.posZ - player.prevPosZ)*event.partialTicks;
        double yaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw)*event.partialTicks;
        double pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch)*event.partialTicks;

        this.correctViewBobbing(player, event.partialTicks, false);
        GL11.glRotated(pitch, 1, 0, 0);
        GL11.glRotated(yaw, 0, 1, 0);
        GL11.glRotated(-180, 0, 1, 0);
        GL11.glTranslated(-posX, -posY, -posZ);

        renderDebug();

        SettlementHandler handler = SettlementHandler.getInstance();
        List<ISettlement> settlementList = handler.getSettlementsForWorld(Minecraft.getMinecraft().theWorld);

        SettlementHandler.getInstance().getSettlementsForWorld(Minecraft.getMinecraft().theWorld).stream().filter(
                settlement -> item.shouldRenderSettlementBoxes(settlement, player, finalStack)).forEach(
                settlement -> renderSettlementBoundingBoxes(tessellator, settlement));

        GL11.glTranslated(posX, posY, posZ);
        GL11.glRotated(180, 0, 1, 0);
        GL11.glRotated(-yaw, 0, 1, 0);
        GL11.glRotated(-pitch, 1, 0, 0);
        this.correctViewBobbing(player, event.partialTicks, true);

        GL11.glPopMatrix();
    }

    public void renderSettlementBoundingBoxes(Tessellator tessellator, ISettlement settlement) {
        settlement.getBoundingBox().renderWireFrame(tessellator, SETTLEMENT_COLOR);
        for(ISettlementBuilding building : settlement.getBuildings()) {
            building.getBoundingBox().renderWireFrame(tessellator, BUILDING_COLOR);
        }
    }
}
