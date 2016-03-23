package com.InfinityRaider.settlercraft.render.schematic;

import com.InfinityRaider.settlercraft.utility.SettlementBoundingBox;
import com.InfinityRaider.settlercraft.utility.schematic.Schematic;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

@SideOnly(Side.CLIENT)
public class SchematicWorld implements IBlockAccess {
    private final World world;
    private final SettlementBoundingBox box;
    private final Map<BlockPos, IBlockState> blockMap;
    private final Map<BlockPos, TileEntity> tileMap;

    private BlockPos origin;

    protected SchematicWorld(World world, BlockPos origin, Schematic schematic, int rotation) {
        this.world = world;
        this.box = schematic.getBoundingBox(new BlockPos(0, 0, 0), rotation);
        this.blockMap = schematic.getBlockStateMap();
        this.tileMap = schematic.getTileEntityMap(world);
        this.origin = origin;
    }

    public void setOrigin(BlockPos pos) {
        this.origin = pos;
    }

    public BlockPos getOrigin() {
        return origin;
    }

    public int sizeX() {
        return box.xSize();
    }

    public int sizeY() {
        return box.ySize();
    }

    public int sizeZ() {
        return box.zSize();
    }

    public SettlementBoundingBox getBoundingBox() {
        return box;
    }

    @Override
    public TileEntity getTileEntity(BlockPos pos) {
        return tileMap.get(pos);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getCombinedLight(BlockPos pos, int lightValue) {
        return world.getCombinedLight(pos.add(getOrigin()), lightValue);
    }

    @Override
    public IBlockState getBlockState(BlockPos pos) {
        if(blockMap.containsKey(pos)) {
            return blockMap.get(pos);
        } else {
            return Blocks.air.getDefaultState();
        }
    }

    @Override
    public boolean isAirBlock(BlockPos pos) {
        IBlockState state = getBlockState(pos);
        return !blockMap.containsKey(pos) || blockMap.get(pos).getBlock().getMaterial(state) == Material.air;
    }

    @Override
    public BiomeGenBase getBiomeGenForCoords(BlockPos pos) {
        return world.getBiomeGenForCoords(pos.add(getOrigin()));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean extendedLevelsInChunkCache() {
        return false;
    }

    @Override
    public int getStrongPower(BlockPos pos, EnumFacing direction) {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public WorldType getWorldType() {
        return world.getWorldType();
    }

    @Override
    public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean defaultValue) {
        if (!this.isValid(pos)) {
            return defaultValue;
        }
        IBlockState state = getBlockState(pos);
        return state.getBlock().isSideSolid(state, this, pos, side);
    }

    private boolean isValid(BlockPos pos) {
        return box.isWithinBounds(pos);
    }
}
