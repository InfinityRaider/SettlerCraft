package com.InfinityRaider.settlercraft.utility;

import com.InfinityRaider.settlercraft.SettlerCraft;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class ChunkCoordinates {
    private int x;
    private int z;
    private int dim;

    public ChunkCoordinates(Chunk chunk) {
        this(chunk.xPosition, chunk.zPosition, chunk.getWorld().provider.getDimensionId());
    }

    public ChunkCoordinates(World world, BlockPos pos) {
        this(pos.getX() >> 4, pos.getZ() >> 4, world.provider.getDimensionId());
    }

    public ChunkCoordinates(int x, int z, int dim) {
        this.x = x;
        this.z = z;
        this.dim = dim;
    }

    public int x() {
        return x;
    }

    public int z() {
        return z;
    }

    public int dim() {
        return dim;
    }

    public World getWorld() {
        return SettlerCraft.proxy.getWorldByDimensionId(dim());
    }

    public Chunk getChunk() {
        return getWorld().getChunkFromChunkCoords(x(), z());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChunkCoordinates that = (ChunkCoordinates) o;
        return (this.x == that.x) && (this.z == that.z) && (this.dim == that.dim);
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + z;
        result = 31 * result + dim;
        return result;
    }
}
