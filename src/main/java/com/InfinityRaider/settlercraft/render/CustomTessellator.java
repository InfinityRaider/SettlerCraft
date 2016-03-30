package com.InfinityRaider.settlercraft.render;

import com.InfinityRaider.settlercraft.utility.ForgeDirection;
import com.InfinityRaider.settlercraft.utility.TransformationMatrix;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class CustomTessellator {
    private static final TransformationMatrix MATRIX_BLOCK_CENTER = new TransformationMatrix(.5, .5, .5);
    private static final TransformationMatrix MATRIX_BLOCK_ORIGIN = new TransformationMatrix(-.5, -.5, -.5);

    private static final Map<VertexBuffer, CustomTessellator> instances = new HashMap<>();

    private final Tessellator tessellator;
    private final VertexBuffer buffer;

    private final Deque<TransformationMatrix> matrices;

    private CustomTessellator(VertexBuffer buffer, Tessellator tessellator) {
        this.buffer = buffer;
        this.tessellator = tessellator;
        this.matrices = new ArrayDeque<>();
        this.matrices.add(new TransformationMatrix());
    }

    public static CustomTessellator getInstance() {
        return getInstance(Tessellator.getInstance());
    }

    public static CustomTessellator getInstance(Tessellator tessellator) {
        final VertexBuffer buffer = tessellator.getBuffer();
        if (instances.containsKey(buffer)) {
            return instances.get(buffer).reset();
        } else {
            final CustomTessellator tess = new CustomTessellator(buffer, tessellator);
            instances.put(buffer, tess);
            return tess;
        }
    }

    public static CustomTessellator getInstance(VertexBuffer buffer) {
        if (instances.containsKey(buffer)) {
            return instances.get(buffer).reset();
        } else {
            final CustomTessellator tess = new CustomTessellator(buffer, null);
            instances.put(buffer, tess);
            return tess;
        }
    }

    /**
     * Color values
     */
    float red;
    float green;
    float blue;
    float alpha;

    /**
     * Brightness value
     */
    int light1;
    int light2;

    public VertexBuffer getVertexBuffer() {
        return buffer;
    }

    public void draw() {
        if (tessellator != null) {
            tessellator.draw();
        } else {
            buffer.finishDrawing();
        }
    }

    /**
     * Sets draw mode in the buffer to draw quads.
     */
    public void startDrawingQuads() {
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
    }

    /**
     * Adds a vertex specifying both x,y,z and the texture u,v for it.
     */
    public void addVertexWithUV(double x, double y, double z, float u, float v) {
        double[] coords = this.matrices.getFirst().transform(x, y, z);
        buffer.pos(coords[0], coords[1], coords[2]);
        buffer.color(red, green, blue, alpha);
        buffer.tex(u, v);
        buffer.lightmap(light1, light2);
        buffer.endVertex();
    }

    /**
     * Sets the translation relative to the absolute coordinates
     */
    public void setTranslation(double x, double y, double z) {
        this.matrices.getFirst().setTranslation(x, y, z);
    }

    /**
     * Sets the rotation relative to the absolute coordinates
     */
    public void setRotation(double angle, double x, double y, double z) {
        this.matrices.getFirst().setRotation(angle, x, y, z);
    }

    /**
     * Adds a translation to the current coordinate system
     */
    public void translate(BlockPos pos) {
        this.translate(pos.getX(), pos.getY(), pos.getZ());
    }

    public void translate(double x, double y, double z) {
        this.matrices.getFirst().multiplyRightWith(new TransformationMatrix(x, y, z));
    }

    /**
     * Rotates around the current coordinate system
     */
    public void rotate(double angle, double x, double y, double z) {
        this.matrices.getFirst().multiplyRightWith(new TransformationMatrix(angle, x, y, z));
    }

    /*
     * Rotate around block center.
     */
    public void rotateBlock(final double angle, final double x, final double y, final double z) {
        final TransformationMatrix tm = this.matrices.getFirst();
        tm.multiplyRightWith(MATRIX_BLOCK_CENTER);
        tm.multiplyRightWith(new TransformationMatrix(angle, x, y, z));
        tm.multiplyRightWith(MATRIX_BLOCK_ORIGIN);
    }

    /*
     * Rotate around block center. 
     */
    public void rotateBlock(final ForgeDirection dir) {
        switch (dir) {
            case EAST:
                this.rotateBlock(90, 0, 1, 0);
                break;
            case SOUTH:
                this.rotateBlock(180, 0, 1, 0);
                break;
            case WEST:
                this.rotateBlock(270, 0, 1, 0);
                break;
            case UP:
                this.rotateBlock(90, 1, 0, 0);
                break;
            case DOWN:
                this.rotateBlock(-90, 1, 0, 0);
                break;
        }
    }

    public void scale(double x, double y, double z) {
        this.matrices.getFirst().scale(x, y, z);
    }

    /**
     * Applies a coordinate transformation
     */
    @SuppressWarnings("unused")
    public void applyTranformation(TransformationMatrix transformationMatrix) {
        this.matrices.getFirst().multiplyRightWith(transformationMatrix);
    }

    public TransformationMatrix getTransformationMatrix() {
        return this.matrices.getFirst();
    }

    public void setBrightness(int value) {
        light1 = value >> 16 & 65535; // 0b1111111111111111 // 0d65535
        light2 = value & 65535;
    }

    /**
     * Sets the RGB values as specified, converting from floats between 0 and 1
     * to integers from 0-255.
     */
    public void setColorOpaque_F(float red, float green, float blue) {
        this.setColorRGBA_F(red, green, blue, 1);
    }

    /**
     * Sets the RGBA values for the color, converting from floats between 0 and
     * 1 to integers from 0-255.
     */
    public void setColorRGBA_F(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    /**
     * Sets the RGB values as specified, and sets alpha to opaque.
     */
    public void setColorOpaque_I(int red, int green, int blue) {
        this.setColorRGBA_I(red, green, blue, 255);
    }

    /*
     * Sets the RGBA values for the color. Also clamps them to 0-255.
     */
    public void setColorRGBA_I(int red, int green, int blue, int alpha) {
        this.setColorRGBA_F(((float) red) / 255.0F, ((float) green) / 255.0F, ((float) blue) / 255.0F, ((float) alpha) / 255.0F);
    }

    /**
     * Push & Pop are better, because then you don't accrue roundoff errors.
     */
    public void pushMatrix() {
        this.matrices.push(new TransformationMatrix(this.matrices.getFirst()));
    }

    public void popMatrix() {
        if (this.matrices.size() > 1) {
            this.matrices.pop();
        }
    }

    /*
     * Resets the matrices.
     */
    public CustomTessellator reset() {
        this.matrices.clear();
        this.matrices.push(new TransformationMatrix());
        this.setColorRGBA_F(1, 1, 1, 1);
        this.setBrightness(15 << 24);
        return this;
    }
    
    
}
