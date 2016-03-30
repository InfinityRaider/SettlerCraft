package com.InfinityRaider.settlercraft.render.tessellation;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
@SuppressWarnings("unused")
public interface ITessellator {
    /**
     * Method to start constructing quads
     * @param format vertex format
     */
    void startDrawingQuads(VertexFormat format);

    /**
     * Method to get all quads constructed
     * @return list of quads, may be emtpy but never null
     */
    List<BakedQuad> getQuads();

    /**
     * Method to finalize drawing
     */
    void draw();

    /**
     * Sets the translation components relative to the absolute coordinate system
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param z the z-coordinate
     * @return this
     */
    ITessellator setTranslation(double x, double y, double z);

    /**
     * Sets the rotation components relative to the absolute coordinate system
     * @param angle rotation ange
     * @param x the x-direction
     * @param y the y-direction
     * @param z the z-direction
     * @return this
     */
    ITessellator setRotation(double angle, double x, double y, double z) ;

    /**
     * Translates the matrix by a vector defined by a BlockPos
     * @param pos the BlockPos
     * @return this
     */
    ITessellator translate(BlockPos pos);

    /**
     * Translates the matrix by a vector defined by 3 coordinates
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @return this
     */
    ITessellator translate(double x, double y, double z);

    /**
     * Rotates the matrix by an angle around the given direction, rotation center is the current origin
     * @param angle angle to rotate by
     * @param x the x direction
     * @param y the y direction
     * @param z the z direction
     * @return this
     */
    ITessellator rotate(double angle, double x, double y, double z);

    /**
     * Scales along each axis with the corresponding factor
     * @param x the x-axis scale factor
     * @param y the y-axis scale factor
     * @param z the z-axis scale factor
     * @return this
     */
    ITessellator scale(double x, double y, double z);
}

