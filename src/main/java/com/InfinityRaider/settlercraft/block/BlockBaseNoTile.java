package com.InfinityRaider.settlercraft.block;

import com.InfinityRaider.settlercraft.block.tile.TileEntityBase;
import net.minecraft.block.material.Material;

public abstract class BlockBaseNoTile extends BlockBase<TileEntityBase> {
    public BlockBaseNoTile(String name, Material blockMaterial) {
        super(name, blockMaterial);
    }
}
