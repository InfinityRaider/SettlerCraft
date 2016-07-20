package com.InfinityRaider.settlercraft.settlement.settler.ai.pathfinding.astar;

import com.InfinityRaider.settlercraft.utility.BoundingBox;
import com.infinityraider.infinitylib.render.RenderUtilBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Iterator;
import java.util.List;


@SideOnly(Side.CLIENT)
public class RenderPath extends RenderUtilBase {
    private static final RenderPath INSTANCE = new RenderPath();

    public static RenderPath getInstance() {
        return INSTANCE;
    }

    private RenderPath() {}

    public void renderPath(RenderWorldLastEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if(player == null) {
            return;
        }

        float partialTick = event.getPartialTicks();

        double posX = player.prevPosX + (player.posX - player.prevPosX)*partialTick;
        double posY = player.prevPosY + (player.posY - player.prevPosY)*partialTick;
        double posZ = player.prevPosZ + (player.posZ - player.prevPosZ)*partialTick;

        GL11.glPushMatrix();

        GL11.glTranslated(-posX, -posY, -posZ);

        Node[][][] nodes = AStarTest.getInstance().getNodes();
        if(nodes != null) {
            for (Node[][] node : nodes) {
                for (Node[] aNode : node) {
                    for (Node anANode : aNode) {
                        this.renderNode(Tessellator.getInstance(), anANode);
                    }
                }
            }
        }

        List<Node> path = AStarTest.getInstance().getPath();
        if(path != null) {
            Tessellator tessellator = Tessellator.getInstance();
            VertexBuffer buffer = tessellator.getBuffer();
            Color color = Color.black;

            int red = color.getRed();
            int green = color.getGreen();
            int blue = color.getBlue();
            int alpha = color.getAlpha();

            Iterator<Node> it = path.iterator();

            buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
            while(it.hasNext()) {
                Node node = it.next();
                buffer.pos(node.getX() + 0.5, node.getY() + 0.5, node.getZ() + 0.5).color(red, green, blue, alpha).endVertex();
            }
            tessellator.draw();
        }

        GL11.glTranslated(posX, posY, posZ);

        GL11.glPopMatrix();
    }

    private void renderNode(Tessellator tessellator, Node node) {
        Color color;
        if(node instanceof Node.Ground) {
            color =  Color.green;
        }
        else if(node instanceof Node.Fall) {
            color = Color.blue;
        }
        else if(node instanceof Node.Climb) {
            color = Color.yellow;
        }
        else if(node instanceof Node.Swim) {
            color = Color.cyan;
        } else {
            color = Color.red;
        }

        VertexBuffer buffer = tessellator.getBuffer();

        new BoundingBox(node.getPosition(), node.getPosition()).renderWireFrame(tessellator, color);

        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();

        GL11.glTranslatef(node.getX(), node.getY(), node.getZ());

        for(Direction dir : node.getValidDirections()) {

            int x = dir.x();
            int y = dir.y();
            int z = dir.z();

            int red = color.getRed();
            int green = color.getGreen();
            int blue = color.getBlue();
            int alpha = color.getAlpha();

            buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
            buffer.pos(0.5, 0.5, 0.5).color(red, green, blue, alpha).endVertex();
            buffer.pos((x/2.0) + 0.5, (y/2.0) + 0.5, (z/2.0) + 0.5).color(red, green, blue, alpha).endVertex();
            tessellator.draw();
        }

        GL11.glTranslatef(-node.getX(), -node.getY(), -node.getZ());
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
    }
}
