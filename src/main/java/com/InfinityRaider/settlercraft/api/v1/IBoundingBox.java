package com.InfinityRaider.settlercraft.api.v1;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

/**
 * Interface used to define settlements and buildings inside settlements,
 * If you want to create your own implementation, that's fine but the suggested way
 * to create new instances is to call api.createNewBoundingBox()
 */
public interface IBoundingBox {
    /**
     * @return the minimum position as a BlockPos
     */
    BlockPos getMinimumPosition();

    /**
     * @return the maximum position as a BlockPos
     */
    BlockPos getMaximumPosition();

    /**
     * @return minimum x coordinate
     */
    int minX();

    /**
     * @return minimum y coordinate
     */
    int minY();

    /**
     * @return minimum z coordinate
     */
    int minZ();

    /**
     * @return maximum x coordinate
     */
    int maxX();

    /**
     * @return maximum y coordinate
     */
    int maxY();

    /**
     * @return maximum z coordinate
     */
    int maxZ();

    /**
     * @return the size in the x direction of this bounding box
     */
    int xSize();

    /**
     * @return the size in the x direction of this bounding box
     */
    int ySize();

    /**
     * Copies this bounding box into a new instance
     * @return a new IBoundingBox which is a copy of this
     */
    IBoundingBox copy();

    /**
     * @return the size in the x direction of this bounding box
     */
    int zSize();

    /**
     * Expands this bounding box to make the argument fit into it
     * @param inner the bounding box to make fit in this one
     * @return this
     */
    IBoundingBox expandToFit(IBoundingBox inner);

    /**
     * Expands this bounding box to make the argument fit into it
     * @param pos the BlockPos to make fit in this one
     * @return this
     */
    IBoundingBox expandToFit(BlockPos pos);

    /**
     * Translates this bounding box by an offset
     * @param x offset x
     * @param y offset y
     * @param z offset z
     * @return this
     */
    IBoundingBox offset(int x, int y, int z);

    /**
     * Translates this bounding box by an offset
     * @param offset the offset
     * @return this
     */
    IBoundingBox offset(BlockPos offset);

    /**
     * Rotates this bounding box around the y-axis in it's minimum point by amount x 90°
     * @param amount the amount of 90° to rotate
     * @return this
     */
    IBoundingBox rotate(int amount);

    /**
     * Checks if a BlockPos is within this bounding box
     * @param pos the position to check
     * @return if the BlockPos is inside the bounds
     */
    boolean isWithinBounds(BlockPos pos);

    /**
     * Checks if a set of coordinates is within this bounding box
     * @param x the x-coordinate to check
     * @param y the x-coordinate to check
     * @param z the x-coordinate to check
     * @return if the coordinates is inside the bounds
     */
    boolean isWithinBounds(double x, double y, double z);

    /**
     * Checks if a bounding box intersects with this one
     * @param other the bounding box to check
     * @return if the two boxes intersect
     */
    boolean intersects(IBoundingBox other);

    /**
     * Renders this bounding box as a wireframe,
     * WorldRenderer.begin() is called internally in this method, so make sure you are not tessellating when calling this
     * @param tessellator the Tessellator instance to draw this box with
     * @param color the color of the wireframe
     */
    @SideOnly(Side.CLIENT)
    void renderWireFrame(Tessellator tessellator, Color color);
}
