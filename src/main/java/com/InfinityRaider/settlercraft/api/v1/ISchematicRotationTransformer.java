package com.InfinityRaider.settlercraft.api.v1;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

/**
 * Class used to change the metadata on some blocks when building a building.
 * The building might be rotated a multiple of 90° around the y axis,
 * so some blocks need to have their metadata changed (torches, stairs, ...)
 *
 * This class keeps track of all these meta data transformations and applies them.
 * The instance of this interface can be retrieved via APIv1.getSchematicRotationTransformer()
 *
 * Transformations are defined per block with an array of 4 meta data values,
 * this array represents how the metadata changes after a 90° rotation.
 * Meaning if the structure is rotated n.90°, then the meta will be transformed by (i + n) % 4,
 * where i is the index of the current metadata in the array
 */
public interface ISchematicRotationTransformer {
    /**
     * Rotates the passed coordinates a multiple of 90° around the y-axis, where the position defined by start is the origin
     * @param start the position considered the origin for the rotation
     * @param x the x-coordinate to be rotated
     * @param y the y-coordinate to be rotated
     * @param z the z-coordinate to be rotated
     * @param rotation the amount of 90° rotations (0 -> 0°, 1 -> 90°, 2 -> 180°, ...)
     * @return The resulting position as a BlockPos
     */
    BlockPos applyRotation(BlockPos start, int x, int y, int z, int rotation);

    /**
     * Registers a meta transformation
     * @param block the block which should have this transformation
     * @param meta an array of length 4 containing the correct metadata values for incrementing 90° rotations.
     */
    void registerMetaTransforms(Block block, int[] meta);

    /**
     * Registers a block to need a support block, this means that the block cannot be placed without having a support block next to it.
     * An example of this are minecraft torches.
     * @param block the block to be registered as a block which needs a support block
     */
    void registerBlockWithSupport(Block block);

    /**
     * This method performs the metadata transformation
     * @param block the block which should have its metadata transformed
     * @param meta the metadata to be transformed
     * @param rotation the amount of 90° rotations (0 -> 0°, 1 -> 90°, 2 -> 180°, ...)
     * @return the transformed metadata
     */
    int transformMeta(Block block, int meta, int rotation);

    /**
     * This method returns the relevant transformation data for a Block and meta value,
     * If there is no rotation data known, it returns null, else it will return an array with length 4.
     * An increase in index corresponds to a 90° rotation.
     * @param block the Block
     * @param meta the meta value
     * @return null, or an array of length 4
     */
    @Nullable
    int[] getRotationData(Block block, int meta);

    /**
     * This method checks if a block needs a support block, this means that the block cannot be placed without having a support block next to it.
     * An example of this are minecraft torches.
     * @param block the block to check
     * @return true if the block needs a support block
     */
    boolean needsSupportBlock(Block block);

    /**
     * Sets a TileEntity's NBT tag entry for the correct rotation.
     * The passed tag contains the original rotation value, so don't assume the rotation is 0°,
     * the resulting position should be: original (retrieve from NBT) + rotation * 90°
     *
     * @param tile the TileEntity
     * @param tag the original NBT tag
     * @param rotation the amount of 90° rotations to apply
     * @return the tag
     */
    NBTTagCompound rotateTileTag(TileEntity tile, NBTTagCompound tag, int rotation);
}
