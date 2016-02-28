package com.InfinityRaider.settlercraft.api.v1;

import net.minecraft.util.BlockPos;

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
     * @param offset the offset
     * @return this
     */
    IBoundingBox offset(BlockPos offset);

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

}
