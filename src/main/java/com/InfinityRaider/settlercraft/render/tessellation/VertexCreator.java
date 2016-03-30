package com.InfinityRaider.settlercraft.render.tessellation;

import com.InfinityRaider.settlercraft.reference.Constants;
import com.InfinityRaider.settlercraft.utility.TransformationMatrix;
import com.google.common.primitives.Ints;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Helper class to construct vertices
 */
@SideOnly(Side.CLIENT)
@SuppressWarnings("unused")
public class VertexCreator implements ITessellator {
    /** Draw mode when no vertices are being constructed */
    public static final int DRAW_MODE_NOT_DRAWING = -1;
    /** Draw mode when vertices are being constructed for quads */
    public static final int DRAW_MODE_QUADS = 4;

    /** Default color multiplier (white) */
    public static final int COLOR_MULTIPLIER_STANDARD = 16777215;

    /** The VertexCreator instance */
    private static final VertexCreator INSTANCE = new VertexCreator();

    /** Getter for the VertexCreator instance */
    public static VertexCreator getInstance() {
        return INSTANCE;
    }

    /** Currently constructed quads */
    private final List<BakedQuad> quads;
    /** Currently constructed vertices */
    private final List<int[]> vertexData;

    /** Current drawing mode */
    private int drawMode;
    /** Current vertex format */
    private VertexFormat format;
    /** Current transformation matrix */
    private final Deque<TransformationMatrix> matrices;

    /** Current color multiplier */
    private int colorMultiplier;
    /** Current tint index for the quad */
    private int tintIndex;
    /** Current face for the quad */
    private EnumFacing face;
    /** Current diffuse lighting setting for the quad */
    private boolean applyDiffuseLighting;

    /** Private constructor */
    private VertexCreator() {
        this.quads = new ArrayList<>();
        this.vertexData = new ArrayList<>();
        this.drawMode = DRAW_MODE_NOT_DRAWING;
        this.matrices = new ArrayDeque<>();
        this.resetMatrix();
        this.tintIndex = -1;
        this.colorMultiplier = COLOR_MULTIPLIER_STANDARD;
    }

    /**
     * Method to start constructing quads
     * @param vertexFormat vertex format
     */
    @Override
    public void startDrawingQuads(VertexFormat vertexFormat) {
        this.startDrawing(DRAW_MODE_QUADS, vertexFormat);
    }

    /**
     * Method to start constructing vertices
     * @param mode draw mode
     * @param format vertex format
     */
    public void startDrawing(int mode, VertexFormat format) {
        if(drawMode != DRAW_MODE_NOT_DRAWING) {
            this.drawMode = mode;
            this.format = format;
        } else {
            throw new RuntimeException("ALREADY CONSTRUCTING VERTICES");
        }
    }

    /**
     * Method to get all quads constructed
     * @return list of quads, may be emtpy but never null
     */
    @Override
    public List<BakedQuad> getQuads() {
        List<BakedQuad> list = new ArrayList<>();
        list.addAll(this.quads);
        return list;
    }

    /**
     * Method to finalize drawing
     */
    @Override
    public void draw() {
        if(drawMode != DRAW_MODE_NOT_DRAWING) {
            quads.clear();
            vertexData.clear();
            this.resetMatrix();
            this.drawMode = DRAW_MODE_NOT_DRAWING;
            this.format = null;
            this.tintIndex = -1;
            this.face = null;
            this.applyDiffuseLighting = false;
            this.colorMultiplier = COLOR_MULTIPLIER_STANDARD;
        } else {
            throw new RuntimeException("NOT CONSTRUCTING VERTICES");
        }
    }

    /**
     * Adds a vertex
     * @param x the x-coordinate for the vertex
     * @param y the y-coordinate for the vertex
     * @param z the z-coordinate for the vertex
     * @param icon the icon
     * @param u u value for the vertex
     * @param v v value for the vertex
     */
    public void addVertexWithUV(float x, float y, float z, TextureAtlasSprite icon, float u, float v) {
        this.addVertexWithUV(x, y, z, icon, u, v, colorMultiplier);
    }

    /**
     * Adds a vertex
     * @param x the x-coordinate for the vertex
     * @param y the y-coordinate for the vertex
     * @param z the z-coordinate for the vertex
     * @param icon the icon
     * @param u u value for the vertex
     * @param v v value for the vertex
     * @param color color modifier
     */
    public void addVertexWithUV(float x, float y, float z, TextureAtlasSprite icon, float u, float v, int color) {
        if(drawMode == DRAW_MODE_NOT_DRAWING) {
            throw new RuntimeException("NOT CONSTRUCTING VERTICES");
        }
        double[] coords = this.matrices.getFirst().transform(x, y, z);
        vertexData.add(vertexToIntArray(x, y, z, color, icon, u, v));
        if(vertexData.size() == drawMode) {
            quads.add(new BakedQuad(Ints.concat(
                    vertexData.get(0),
                    vertexData.get(1),
                    vertexData.get(2),
                    vertexData.get(3)
            ), this.tintIndex, this.face, icon, this.applyDiffuseLighting, this.format));
            vertexData.clear();
        }
    }

    /**
     * Adds a vertex scaled by 1/16th of a block
     * @param x the x-coordinate for the vertex
     * @param y the y-coordinate for the vertex
     * @param z the z-coordinate for the vertex
     * @param icon the icon
     * @param u u value for the vertex
     * @param v v value for the vertex
     */
    public void addScaledVertexWithUV(float x, float y, float z, TextureAtlasSprite icon, float u, float v) {
        addScaledVertexWithUV(x, y, z, icon, u, v, colorMultiplier);
    }

    /**
     * Adds a vertex scaled by 1/16th of a block
     * @param x the x-coordinate for the vertex
     * @param y the y-coordinate for the vertex
     * @param z the z-coordinate for the vertex
     * @param icon the icon
     * @param u u value for the vertex
     * @param v v value for the vertex
     * @param color color modifier
     */
    public void addScaledVertexWithUV(float x, float y, float z, TextureAtlasSprite icon, float u, float v, int color) {
        addVertexWithUV(x * Constants.UNIT, y * Constants.UNIT, z * Constants.UNIT, icon, u, v, color);
    }

    /**
     * Adds a quad for a scaled face, the face is defined by minimum and maximum coordinates
     * @param minX minimum 2D x-coordinate of the face
     * @param minY minimum 2D y-coordinate of the face
     * @param maxX maximum 2D x-coordinate of the face
     * @param maxY maximum 2D y-coordinate of the face
     * @param face orientation of the face
     * @param icon icon to render the face with
     * @param offset offset of the face along its normal
     */
    public void drawScaledFace(float minX, float minY, float maxX, float maxY, EnumFacing face, TextureAtlasSprite icon, float offset) {
        this.drawScaledFace(minX, minY, maxX, maxY, face, icon, offset, colorMultiplier);
    }

    /**
     * Adds a quad for a scaled face, the face is defined by minimum and maximum 2D coordinates
     * @param minX minimum 2D x-coordinate of the face
     * @param minY minimum 2D y-coordinate of the face
     * @param maxX maximum 2D x-coordinate of the face
     * @param maxY maximum 2D y-coordinate of the face
     * @param face orientation of the face
     * @param icon icon to render the face with
     * @param offset offset of the face along its normal
     * @param color color multiplier
     */
    public void drawScaledFace(float minX, float minY, float maxX, float maxY, EnumFacing face, TextureAtlasSprite icon, float offset, int color) {
        float x1, x2, x3, x4;
        float y1, y2, y3, y4;
        float z1, z2, z3, z4;
        final float min = 0.0F;
        final float max = 1.0F;
        switch (face) {
            case UP: {
                x1 = x2 = maxX;
                x3 = x4 = minX;
                z1 = z4 = maxY;
                z2 = z3 = minY;
                y1 = y2 = y3 = y4 = max + offset;
                break;
            }
            case DOWN: {
                x1 = x2 = maxX;
                x3 = x4 = minX;
                z1 = z4 = minY;
                z2 = z3 = maxX;
                y1 = y2 = y3 = y4 = min - offset;
                break;
            }
            case WEST: {
                z1 = z2 = maxX;
                z3 = z4 = minX;
                y1 = y4 = minY;
                y2 = y3 = maxY;
                x1 = x2 = x3 = x4 = min - offset;
                break;
            }
            case EAST: {
                z1 = z2 = minX;
                z3 = z4 = maxX;
                y1 = y4 = minY;
                y2 = y3 = maxY;
                x1 = x2 = x3 = x4 = max + offset;
                break;
            }
            case NORTH: {
                x1 = x2 = minX;
                x3 = x4 = maxX;
                y1 = y4 = minY ;
                y2 = y3 = maxY;
                z1 = z2 = z3 = z4 = min - offset;
                break;
            }
            case SOUTH: {
                x1 = x2 = maxX;
                x3 = x4 = minX;
                y1 = y4 = minY;
                y2 = y3 = maxY;
                z1 = z2 = z3 = z4 = max + offset;
                break;
            }
            default: return;
        }
        this.setFace(face);
        addScaledVertexWithUV(x1, y1, z1, icon, 16, 16, color);
        addScaledVertexWithUV(x2, y2, z2, icon, 16, 0, color);
        addScaledVertexWithUV(x3, y3, z3, icon, 0, 0, color);
        addScaledVertexWithUV(x4, y4, z4, icon, 0, 16, color);
    }

    /**
     * Adds two quads for a scaled face, this face will have both sides drawn.
     * The face is defined by minimum and maximum coordinates
     * @param minX minimum 2D x-coordinate of the face
     * @param minY minimum 2D y-coordinate of the face
     * @param maxX maximum 2D x-coordinate of the face
     * @param maxY maximum 2D y-coordinate of the face
     * @param face orientation of the face
     * @param icon icon to render the face with
     * @param offset offset of the face along its normal
     */
    public void drawScaledFaceDouble(float minX, float minY, float maxX, float maxY, EnumFacing face, TextureAtlasSprite icon, float offset) {
        this.drawScaledFaceDouble(minX, minY, maxX, maxY, face, icon, offset, colorMultiplier);
    }

    /**
     * Adds two quads for a scaled face, this face will have both sides drawn.
     * The face is defined by minimum and maximum coordinates
     * @param minX minimum 2D x-coordinate of the face
     * @param minY minimum 2D y-coordinate of the face
     * @param maxX maximum 2D x-coordinate of the face
     * @param maxY maximum 2D y-coordinate of the face
     * @param face orientation of the face
     * @param icon icon to render the face with
     * @param offset offset of the face along its normal
     * @param color color multiplier
     */
    public void drawScaledFaceDouble(float minX, float minY, float maxX, float maxY, EnumFacing face, TextureAtlasSprite icon, float offset, int color) {
        EnumFacing opposite;
        switch(face) {
            case NORTH: opposite = EnumFacing.SOUTH; break;
            case SOUTH: opposite = EnumFacing.NORTH; break;
            case EAST: opposite = EnumFacing.WEST; break;
            case WEST: opposite = EnumFacing.EAST; break;
            case UP: opposite = EnumFacing.DOWN; break;
            case DOWN: opposite = EnumFacing.UP; break;
            default:
                return;
        }
        this.drawScaledFace(minX, minY, maxX, maxY, face, icon, offset, color);
        this.drawScaledFace(minX, minY, maxX, maxY, opposite, icon, -offset, color);
    }

    /**
     * Adds 6 quads for a scaled prism, the prism is defined by maximum and minimum 3D coordinates
     * @param minX minimum x-coordinate of the face
     * @param minY minimum y-coordinate of the face
     * @param minZ maximum z-coordinate of the face
     * @param maxX maximum x-coordinate of the face
     * @param maxY maximum y-coordinate of the face
     * @param maxZ maximum z-coordinate of the face
     * @param icon icon to render the prism with
     */
    public void drawScaledPrism(float minX, float minY, float minZ, float maxX, float maxY, float maxZ, TextureAtlasSprite icon) {
        this.drawScaledPrism(minX, minY, minZ, maxX, maxY, maxZ, icon, colorMultiplier);
    }

    /**
     * Adds 6 quads for a scaled prism, the prism is defined by maximum and minimum 3D coordinates
     * @param minX minimum x-coordinate of the face
     * @param minY minimum y-coordinate of the face
     * @param minZ maximum z-coordinate of the face
     * @param maxX maximum x-coordinate of the face
     * @param maxY maximum y-coordinate of the face
     * @param maxZ maximum z-coordinate of the face
     * @param icon icon to render the prism with
     * @param color color multiplier
     */
    public void drawScaledPrism(float minX, float minY, float minZ, float maxX, float maxY, float maxZ, TextureAtlasSprite icon, int color) {
        //bottom
        drawScaledFace(minX, minZ, maxX, maxZ, EnumFacing.DOWN, icon, minY, color);
        //top
        drawScaledFace(minX, minZ, maxX, maxZ, EnumFacing.UP, icon, maxY, color);
        //north
        drawScaledFace(minX, minY, maxX, maxY, EnumFacing.NORTH, icon, minZ, color);
        //south
        drawScaledFace(minX, minY, maxX, maxY, EnumFacing.SOUTH, icon, maxZ, color);
        //west
        drawScaledFace(minZ, minY, maxZ, maxY, EnumFacing.WEST, icon, minX, color);
        //east
        drawScaledFace(minZ, minY, maxZ, maxY, EnumFacing.EAST, icon, maxX, color);
    }

    /**
     * Sets the translation components relative to the absolute coordinate system
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param z the z-coordinate
     * @return this
     */
    public VertexCreator setTranslation(double x, double y, double z) {
        this.matrices.getFirst().setTranslation(x, y, z);
        return this;
    }

    /**
     * Sets the rotation components relative to the absolute coordinate system
     * @param angle rotation ange
     * @param x the x-direction
     * @param y the y-direction
     * @param z the z-direction
     * @return this
     */
    public VertexCreator setRotation(double angle, double x, double y, double z) {
        this.matrices.getFirst().setRotation(angle, x, y, z);
        return this;
    }

    /**
     * Translates the matrix by a vector defined by a BlockPos
     * @param pos the BlockPos
     * @return this
     */
    public VertexCreator translate(BlockPos pos) {
        this.translate(pos.getX(), pos.getY(), pos.getZ());
        return this;
    }

    /**
     * Translates the matrix by a vector defined by 3 coordinates
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @return this
     */
    public VertexCreator translate(double x, double y, double z) {
        this.matrices.getFirst().multiplyRightWith(new TransformationMatrix(x, y, z));
        return this;
    }

    /**
     * Rotates the matrix by an angle around the given direction, rotation center is the current origin
     * @param angle angle to rotate by
     * @param x the x direction
     * @param y the y direction
     * @param z the z direction
     * @return this
     */
    public VertexCreator rotate(double angle, double x, double y, double z) {
        this.matrices.getFirst().multiplyRightWith(new TransformationMatrix(angle, x, y, z));
        return this;
    }

    /**
     * Scales along each axis with the corresponding factor
     * @param x the x-axis scale factor
     * @param y the y-axis scale factor
     * @param z the z-axis scale factor
     * @return this
     */
    public VertexCreator scale(double x, double y, double z) {
        this.matrices.getFirst().scale(x, y, z);
        return this;
    }

    /**
     * Applies a custom transformation
     * @param transformationMatrix transformation matrix defining the custom transformation
     * @return this
     */
    public VertexCreator applyTransformation(TransformationMatrix transformationMatrix) {
        this.matrices.getFirst().multiplyRightWith(transformationMatrix);
        return this;
    }

    /**
     * Gets the current transformation matrix
     * @return the transformation matrix
     */
    public TransformationMatrix getTransformationMatrix() {
        return this.matrices.getFirst();
    }

    /**
     * Copied from TheGreyGhost, converts the vertex information to the int array format expected by BakedQuads.
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @param color RGBA colour format - white for no effect, non-white to tint the face with the specified colour
     * @param texture the texture to use for the face
     * @param u u-coordinate of the texture (0 - 16) corresponding to [x,y,z]
     * @param v v-coordinate of the texture (0 - 16) corresponding to [x,y,z]
     * @return int array
     */
    public int[] vertexToIntArray(float x, float y, float z, int color, TextureAtlasSprite texture, float u, float v) {
        return new int[]{
                Float.floatToRawIntBits(x),
                Float.floatToRawIntBits(y),
                Float.floatToRawIntBits(z),
                color,
                Float.floatToRawIntBits(texture.getInterpolatedU(u)),
                Float.floatToRawIntBits(texture.getInterpolatedV(v)),
                0
        };
    }

    /**
     * Sets the tint index value to use for the quads
     * @param index the tint index
     * @return this
     */
    public VertexCreator setTintIndex(int index) {
        this.tintIndex = index;
        return this;
    }

    /**
     * Gets the current tint index value to use for the quads
     * @return the tint index
     */
    public int getTintIndex() {
        return this.tintIndex;
    }

    /**
     * Sets the current face being used for the quads
     * @param face the current face
     * @return this
     */
    public VertexCreator setFace(EnumFacing face) {
        this.face = face;
        return this;
    }

    /**
     * Gets the current face being used for the quads
     * @return the current face
     */
    public EnumFacing getFace() {
        return this.face;
    }

    /**
     * Sets if diffuse lighting should be applied to the quads
     * @param value the diffuse lighting setting
     * @return this
     */
    public VertexCreator setApplyDiffuseLighting(boolean value) {
        this.applyDiffuseLighting = value;
        return this;
    }

    /**
     * Gets if diffuse lighting is applied to the quads
     * @return the diffuse lighting setting
     */
    public boolean getApplyDiffuseLighting() {
        return this.applyDiffuseLighting;
    }

    /**
     * Sets the current color multiplier for the quads
     * @param color the color multiplier
     * @return this
     */
    public VertexCreator setColorMultiplier(int color) {
        this.colorMultiplier = color;
        return this;
    }

    /**
     * Gets the current color multiplier
     * @return the color multiplier
     */
    public int getColorMultiplier() {
        return this.colorMultiplier;
    }

    /**
     * Resets the transformation matrix
     * @return this
     */
    public VertexCreator resetMatrix() {
        this.matrices.clear();
        this.matrices.push(new TransformationMatrix());
        return this;
    }
}
