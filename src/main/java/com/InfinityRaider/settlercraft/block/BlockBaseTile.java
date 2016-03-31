package com.InfinityRaider.settlercraft.block;

import com.InfinityRaider.settlercraft.block.tile.TileEntityBase;
import com.InfinityRaider.settlercraft.reference.Reference;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class BlockBaseTile<T extends TileEntityBase> extends BlockBase<T> implements ITileEntityProvider{
    private final String tileName;

    public BlockBaseTile(String name, String tileName, Material blockMaterial, MapColor blockMapColor) {
        super(name, blockMaterial, blockMapColor);
        this.tileName = Reference.MOD_ID.toLowerCase() + ":tileEntity." + tileName;
        TileEntity tile = this.createNewTileEntity(null, 0);
        GameRegistry.registerTileEntity(tile.getClass(), getTileName());
    }

    public final String getTileName() {
        return tileName;
    }

    @Override
    public abstract T createNewTileEntity(World worldIn, int meta);
}
