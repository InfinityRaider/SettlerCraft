package com.InfinityRaider.settlercraft.render;

import com.InfinityRaider.settlercraft.handler.ConfigurationHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class RenderBase {
    protected RenderBase() {}

    protected void correctViewBobbing(EntityPlayer player, float partialTicks, boolean inverse) {
        if (!Minecraft.getMinecraft().gameSettings.viewBobbing) {
            return;
        }
        float f = player.distanceWalkedModified - player.prevDistanceWalkedModified;
        float f1 = -(player.distanceWalkedModified + f * partialTicks);
        float f2 = player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * partialTicks;
        float f3 = player.prevCameraPitch + (player.cameraPitch - player.prevCameraPitch) * partialTicks;
        if(inverse) {
            GlStateManager.translate(MathHelper.sin(f1 * (float) Math.PI) * f2 * 0.5F, -Math.abs(MathHelper.cos(f1 * (float) Math.PI) * f2), 0.0F);
            GlStateManager.rotate(MathHelper.sin(f1 * (float)Math.PI) * f2 * 3.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(Math.abs(MathHelper.cos(f1 * (float)Math.PI - 0.2F) * f2) * 5.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(f3, 1.0F, 0.0F, 0.0F);
        } else {
            GlStateManager.rotate(-f3, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-Math.abs(MathHelper.cos(f1 * (float)Math.PI - 0.2F) * f2) * 5.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-MathHelper.sin(f1 * (float)Math.PI) * f2 * 3.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.translate(-MathHelper.sin(f1 * (float) Math.PI) * f2 * 0.5F, Math.abs(MathHelper.cos(f1 * (float) Math.PI) * f2), 0.0F);
        }
    }

    protected void renderCoordinateSystemDebug() {
        if(ConfigurationHandler.getInstance().debug) {
            Tessellator tessellator = Tessellator.getInstance();
            VertexBuffer buffer = tessellator.getBuffer();
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();

            buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
            for(int i = 0; i <= 16; i++) {
                buffer.pos(((float) i) / 16.0F, 0, 0).color(255, 0, 0, 255).endVertex();
            }
            tessellator.draw();

            buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
            for(int i = 0; i <= 16; i++) {
                buffer.pos(0, ((float) i) / 16.0F, 0).color(0, 255, 0, 255).endVertex();
            }
            tessellator.draw();

            buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
            for(int i = 0; i <= 16; i++) {
                buffer.pos(0, 0, ((float) i) / 16.0F).color(0, 0, 255, 255).endVertex();
            }
            tessellator.draw();

            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
        }
    }
}
