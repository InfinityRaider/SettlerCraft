package com.InfinityRaider.settlercraft.api.v1;

import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;

/**
 * IInventory extended with methods to read/write to/from nbt
 */
public interface IInventorySerializable extends IInventory {
    /**
     * Write this inventory's data to a new NBT tag
     * @return a new NBT tag holding the data for this inventory
     */
    NBTTagCompound writeToNBT();

    /**
     * Read this inventory's data from an NBT tag
     * @param tag an NBT tag holding the data for this inventory
     */
    void readFromNBT(NBTTagCompound tag);
}
