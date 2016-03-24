package com.InfinityRaider.settlercraft.render.schematic;

import com.InfinityRaider.settlercraft.SettlerCraft;
import com.InfinityRaider.settlercraft.api.v1.IBoundingBox;
import com.InfinityRaider.settlercraft.api.v1.IBuilding;
import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.item.ItemBuildingPlanner;
import com.InfinityRaider.settlercraft.render.RenderBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class SchematicInWorldPlannerRenderer extends RenderBase {
    private static final SchematicInWorldPlannerRenderer INSTANCE = new SchematicInWorldPlannerRenderer();

    public static SchematicInWorldPlannerRenderer getInstance() {
        return INSTANCE;
    }

    public static final Color BUILDING_VALID_COLOR = new Color(12, 100, 46);
    public static final Color BUILDING_INVALID_COLOR = new Color(198, 0, 14);

    private final SchematicRenderer renderer;

    private SchematicInWorldPlannerRenderer() {
        super();
        renderer = SchematicRenderer.getInstance();
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void renderSchematicOverlay(RenderHandEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if(player == null) {
            return;
        }
        ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
        if(stack == null || stack.getItem() == null || !(stack.getItem() instanceof ItemBuildingPlanner)) {
            stack = player.getHeldItem(EnumHand.OFF_HAND);
        }
        if(stack == null || stack.getItem() == null || !(stack.getItem() instanceof ItemBuildingPlanner)) {
            return;
        }
        ItemBuildingPlanner planner = (ItemBuildingPlanner) stack.getItem();
        IBuilding building = planner.getBuilding(stack);
        ISettlement settlement = planner.getSettlement(stack);
        if(building == null || settlement == null) {
            return;
        }
        RayTraceResult raytraced = player.rayTrace(5, event.partialTicks);
        if(raytraced == null || raytraced.getBlockPos() == null || raytraced.sideHit != EnumFacing.UP) {
            return;
        }
        BlockPos pos = raytraced.getBlockPos().offset(raytraced.sideHit);
        IBlockState state = SettlerCraft.proxy.getClientWorld().getBlockState(raytraced.getBlockPos());
        if(state.getBlock().getMaterial(state) == Material.air) {
            return;
        }
        renderer.setSchematicFromStack(stack, planner);

        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();
        GL11.glPushMatrix();

        int rotation = planner.getRotation(stack);
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

        IBoundingBox buildingBox = renderer.getBoundingBox().copy().offset(pos).rotate(rotation);
        Color color = planner.isValidBoundingBoxForBuilding(stack, player, settlement, building, buildingBox) ? BUILDING_VALID_COLOR : BUILDING_INVALID_COLOR;
        buildingBox.renderWireFrame(tessellator, color);

        GL11.glTranslated(pos.getX(), pos.getY(), pos.getZ());
        applyRotation(rotation, false);

        renderDebug();

        buffer.begin(7, DefaultVertexFormats.BLOCK);
        renderer.setOrigin(pos);
        renderer.doRender(buffer);
        tessellator.draw();

        applyRotation(rotation, true);
        GL11.glTranslated(-pos.getX(), -pos.getY(), -pos.getZ());
        GL11.glTranslated(posX, posY, posZ);
        GL11.glRotated(180, 0, 1, 0);
        GL11.glRotated(-yaw, 0, 1, 0);
        GL11.glRotated(-pitch, 1, 0, 0);
        this.correctViewBobbing(player, event.partialTicks, true);

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
}
