package com.InfinityRaider.settlercraft.render.schematic;

import com.InfinityRaider.settlercraft.SettlerCraft;
import com.InfinityRaider.settlercraft.api.v1.IBoundingBox;
import com.InfinityRaider.settlercraft.api.v1.IBuilding;
import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.handler.ConfigurationHandler;
import com.InfinityRaider.settlercraft.item.ItemBuildingPlanner;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class SchematicInWorldPlannerRenderer {
    private static final SchematicInWorldPlannerRenderer INSTANCE = new SchematicInWorldPlannerRenderer();

    public static SchematicInWorldPlannerRenderer getInstance() {
        return INSTANCE;
    }

    public static final Color BUILDING_VALID_COLOR = new Color(12, 100, 46);
    public static final Color BUILDING_INVALID_COLOR = new Color(198, 0, 14);

    private final SchematicRenderer renderer;

    private SchematicInWorldPlannerRenderer() {
        renderer = SchematicRenderer.getInstance();
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void renderSchematicOverlay(RenderHandEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if(player == null) {
            return;
        }
        ItemStack stack = player.getCurrentEquippedItem();
        if(stack == null || stack.getItem() == null || !(stack.getItem() instanceof ItemBuildingPlanner)) {
            return;
        }
        ItemBuildingPlanner planner = (ItemBuildingPlanner) stack.getItem();
        IBuilding building = planner.getBuilding(stack);
        ISettlement settlement = planner.getSettlement(stack);
        if(building == null || settlement == null) {
            return;
        }
        MovingObjectPosition raytraced = player.rayTrace(5, event.partialTicks);
        if(raytraced == null || raytraced.getBlockPos() == null || raytraced.sideHit != EnumFacing.UP) {
            return;
        }
        BlockPos pos = raytraced.getBlockPos().offset(raytraced.sideHit);
        IBlockState state = SettlerCraft.proxy.getClientWorld().getBlockState(raytraced.getBlockPos());
        if(state.getBlock().getMaterial() == Material.air) {
            return;
        }
        renderer.setSchematicFromStack(stack, planner);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GL11.glPushMatrix();

        int rotation = planner.getRotation(stack);
        double posX = player.prevPosX + (player.posX - player.prevPosX)*event.partialTicks;
        double posY = player.prevPosY + (player.posY - player.prevPosY)*event.partialTicks;
        double posZ = player.prevPosZ + (player.posZ - player.prevPosZ)*event.partialTicks;

        GL11.glTranslated(-posX, -posY, -posZ);

        IBoundingBox settlementBox = settlement.getBoundingBox();
        IBoundingBox buildingBox = renderer.getBoundingBox().copy().offset(pos).rotate(rotation);
        buildingBox.renderWireFrame(tessellator, settlementBox.intersects(buildingBox) ? BUILDING_VALID_COLOR : BUILDING_INVALID_COLOR);

        GL11.glTranslated(pos.getX(), pos.getY(), pos.getZ());
        applyRotation(rotation, false);

        renderDebug();

        worldrenderer.begin(7, DefaultVertexFormats.BLOCK);
        renderer.setOrigin(pos);
        renderer.doRender(worldrenderer);
        tessellator.draw();


        applyRotation(rotation, true);
        GL11.glTranslated(-pos.getX(), -pos.getY(), -pos.getZ());
        GL11.glTranslated(posX, posY, posZ);

        GL11.glPopMatrix();
    }

    private void applyRotation(int rotation, boolean inverse) {
        int dx = rotation == 1 || rotation == 2 ? -1 : 0;
        int dz = rotation == 2 || rotation == 3 ? -1 : 0;
        if(inverse) {
            GL11.glTranslatef(-dx, 0, -dz);
            GL11.glRotatef(-90*rotation, 0, 1, 0);
        } else {
            GL11.glRotatef(90*rotation, 0, 1, 0);
            GL11.glTranslatef(dx, 0, dz);
        }
    }

    private void renderDebug() {
        if(ConfigurationHandler.getInstance().debug) {
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();

            worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
            for(int i = 0; i <= 16; i++) {
                worldrenderer.pos(((float) i) / 16.0F, 0, 0).color(255, 0, 0, 255).endVertex();
            }
            tessellator.draw();

            worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
            for(int i = 0; i <= 16; i++) {
                worldrenderer.pos(0, ((float) i) / 16.0F, 0).color(0, 255, 0, 255).endVertex();
            }
            tessellator.draw();

            worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
            for(int i = 0; i <= 16; i++) {
                worldrenderer.pos(0, 0, ((float) i) / 16.0F).color(0, 0, 255, 255).endVertex();
            }
            tessellator.draw();

            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
        }
    }
}
