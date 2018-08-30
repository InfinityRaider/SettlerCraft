package com.infinityraider.settlercraft.api.v1;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Implement this in TileEntities which Block handles rotation from the TileEntity instead of the BlockState
 */
public interface IRotatableTile {
    /**
     * This is called right before the tile entity will have the readFromNBT method called with the given tag,
     * use it to set the tag's rotation entry according to the performed rotation.
     * The tag should contain the original rotation value, so don't assume the rotation is 0�,
     * the resulting position should be: original (retrieve from NBT) + rotation * 90�
     *
     * @param tag the NBT tag
     * @param rotation the amount of 90� rotation (0 -> 0�, 1 -> 90�, 2 -> 180�, 3 -> 270�)
     */
    void setRotationTag(NBTTagCompound tag, int rotation);
}
