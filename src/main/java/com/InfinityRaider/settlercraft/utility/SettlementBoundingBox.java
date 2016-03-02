package com.InfinityRaider.settlercraft.utility;

import com.InfinityRaider.settlercraft.api.v1.IBoundingBox;
import com.InfinityRaider.settlercraft.reference.Constants;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class SettlementBoundingBox implements IBoundingBox {
    private int minX;
    private int minY;
    private int minZ;
    private int maxX;
    private int maxY;
    private int maxZ;

    public SettlementBoundingBox(IBoundingBox box) {
        this(box.minX(), box.minY(), box.minZ(), box.maxX(), box.maxY(), box.maxZ());
    }

    public SettlementBoundingBox(BlockPos center) {
        this(
                center.getX() - (Constants.SETTLEMENT_DEFAULT_WIDTH)/2,
                center.getY(),
                center.getZ() - (Constants.SETTLEMENT_DEFAULT_WIDTH)/2,
                center.getX() + (Constants.SETTLEMENT_DEFAULT_WIDTH)/2,
                center.getY() + (Constants.SETTLEMENT_DEFAULT_HEIGHT),
                center.getZ() + (Constants.SETTLEMENT_DEFAULT_WIDTH)/2
        );
    }

    public SettlementBoundingBox(BlockPos min, BlockPos max) {
        this(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ());
    }

    public SettlementBoundingBox(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.minX = Math.min(minX, maxX);
        this.minY = Math.min(minY, maxX);
        this.minZ = Math.min(minZ, maxX);
        this.maxX = Math.max(minX, maxX);
        this.maxY = Math.max(minY, maxY);
        this.maxZ = Math.max(minZ, maxZ);
    }

    @Override
    public BlockPos getMinimumPosition() {
        return new BlockPos(minX, minY, minZ);
    }

    @Override
    public BlockPos getMaximumPosition() {
        return new BlockPos(maxX, maxY, maxZ);
    }

    @Override
    public int minX() {
        return minX;
    }

    @Override
    public int minY() {
        return minY;
    }

    @Override
    public int minZ() {
        return minZ;
    }

    @Override
    public int maxX() {
        return maxX;
    }

    @Override
    public int maxY() {
        return maxY;
    }

    @Override
    public int maxZ() {
        return maxZ;
    }

    @Override
    public int xSize() {
        return maxX - minX;
    }

    @Override
    public int ySize() {
        return maxY - minY;
    }

    @Override
    public int zSize() {
        return maxZ - minZ;
    }

    @Override
    public SettlementBoundingBox copy() {
        return new SettlementBoundingBox(this);
    }

    @Override
    public SettlementBoundingBox expandToFit(IBoundingBox inner) {
        minX = minX < inner.minX() ? minX : inner.minX();
        minY = minY < inner.minY() ? minY : inner.minY();
        minZ = minZ < inner.minZ() ? minZ : inner.minZ();
        maxX = maxX > inner.maxX() ? maxX : inner.maxX();
        maxY = maxY > inner.maxY() ? maxY : inner.maxY();
        maxZ = maxZ > inner.maxZ() ? maxZ : inner.maxZ();
        return this;
    }

    @Override
    public SettlementBoundingBox expandToFit(BlockPos pos) {
        if(pos.getX() > maxX) {
            maxX = pos.getX();
        } else if(pos.getX() < minX) {
            minX = pos.getX();
        }
        if(pos.getY() > maxY) {
            maxY = pos.getY();
        } else if(pos.getY() < minY) {
            minY = pos.getY();
        }
        if(pos.getZ() > maxZ) {
            maxZ = pos.getZ();
        } else if(pos.getZ() < minZ) {
            minZ = pos.getZ();
        }
        return this;
    }

    @Override
    public SettlementBoundingBox offset(int x, int y, int z) {
        minX = minX + x;
        minY = minY + y;
        minZ = minZ + z;
        maxX = maxX + x;
        maxY = maxY + y;
        maxZ = maxZ + z;
        return this;
    }

    @Override
    public SettlementBoundingBox offset(BlockPos offset) {
        return offset(offset.getX(), offset.getY(), offset.getZ());
    }

    @Override
    public SettlementBoundingBox rotate(int amount) {
        amount = amount % 4;
        if(amount == 0) {
            return this;
        }
        int newX1 = this.minX;
        int newZ1 = this.minZ;
        this.offset(-newX1, 0, -newZ1);
        int newX2 = amount == 1 ? this.maxZ : amount == 2 ? - this.maxX : - this.maxZ;
        int newZ2 = amount == 1 ? - this.maxX : amount == 2 ? - this.maxZ : this.maxX;
        this.minX = Math.min(0, newX2);
        this.minZ = Math.min(0, newZ2);
        this.maxX = Math.max(0, newX2);
        this.maxZ = Math.max(0, newZ2);
        int dx = amount == 2 || amount == 3 ? 1 : 0;
        int dz = amount == 1 || amount == 2 ? 1 : 0;
        return this.offset(newX1 + dx, 0, newZ1 + dz);
    }

    @Override
    public boolean isWithinBounds(BlockPos pos) {
        return isWithinBounds(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public boolean isWithinBounds(double x, double y, double z) {
        return (x >= minX && x <= maxX)
                && (y >= minY && y <= maxY)
                && (z >= minZ && z <= maxZ);
    }

    @Override
    public boolean intersects(IBoundingBox other) {
        return !(other.maxX() <= this.minX() || other.maxY() <= this.minY() || other.maxZ() <= this.minZ())
                && !(this.maxX() <= other.minX() || this.maxY() <= other.minY() || this.maxZ() <= other.minZ());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderWireFrame(Tessellator tessellator, Color color) {
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GL11.glTranslatef(minX(), minY(), minZ());

        int x = xSize();
        int y = ySize();
        int z = zSize();

        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        int alpha = color.getAlpha();

        //x edges
        worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        for(int i = 0; i <= x; i++) {
            worldrenderer.pos(i, 0.001F, 0.001F).color(red, green, blue, alpha).endVertex();
        }
        tessellator.draw();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        for(int i = 0; i <= x; i++) {
            worldrenderer.pos(i, y - 0.001F, 0.001F).color(red, green, blue, alpha).endVertex();
        }
        tessellator.draw();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        for(int i = 0; i <= x; i++) {
            worldrenderer.pos(i, y - 0.001F, z - 0.001F).color(red, green, blue, alpha).endVertex();
        }
        tessellator.draw();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        for(int i = 0; i <= x; i++) {
            worldrenderer.pos(i, 0.001F, z - 0.001F).color(red, green, blue, alpha).endVertex();
        }
        tessellator.draw();

        //y edges
        worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        for(int i = 0; i <= y; i++) {
            worldrenderer.pos(0.001F, i,  0.001F).color(red, green, blue, alpha).endVertex();
        }
        tessellator.draw();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        for(int i = 0; i <= y; i++) {
            worldrenderer.pos(x - 0.001F, i, 0.001F).color(red, green, blue, alpha).endVertex();
        }
        tessellator.draw();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        for(int i = 0; i <= y; i++) {
            worldrenderer.pos(x - 0.001F, i, z - 0.001F).color(red, green, blue, alpha).endVertex();
        }
        tessellator.draw();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        for(int i = 0; i <= y; i++) {
            worldrenderer.pos(0.001F, i, z - 0.001F).color(red, green, blue, alpha).endVertex();
        }
        tessellator.draw();

        //z edges
        worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        for(int i = 0; i <= z; i++) {
            worldrenderer.pos(0.001F, 0.001F, i).color(red, green, blue, alpha).endVertex();
        }
        tessellator.draw();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        for(int i = 0; i <= z; i++) {
            worldrenderer.pos(x - 0.001F, 0.001F, i).color(red, green, blue, alpha).endVertex();
        }
        tessellator.draw();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        for(int i = 0; i <= z; i++) {
            worldrenderer.pos(x - 0.001F, y - 0.001F, i).color(red, green, blue, alpha).endVertex();
        }
        tessellator.draw();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        for(int i = 0; i <= z; i++) {
            worldrenderer.pos(0.001F, y - 0.001F, i).color(red, green, blue, alpha).endVertex();
        }
        tessellator.draw();

        GL11.glTranslatef(-minX(), -minY(), -minZ());
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
    }
}
