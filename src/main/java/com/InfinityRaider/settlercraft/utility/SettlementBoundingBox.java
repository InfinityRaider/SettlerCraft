package com.InfinityRaider.settlercraft.utility;

import com.InfinityRaider.settlercraft.api.v1.IBoundingBox;
import com.InfinityRaider.settlercraft.reference.Constants;
import net.minecraft.util.BlockPos;

public class SettlementBoundingBox implements IBoundingBox {
    private int minX;
    private int minY;
    private int minZ;
    private int maxX;
    private int maxY;
    private int maxZ;

    public SettlementBoundingBox(BlockPos center) {
        this(
                center.getX() - (Constants.SETTLEMENT_DEFAULT_WIDTH)/2,
                center.getY(),
                center.getZ() - (Constants.SETTLEMENT_DEFAULT_WIDTH)/2,
                center.getX() + (Constants.SETTLEMENT_DEFAULT_WIDTH)/2,
                center.getY() + Constants.SETTLEMENT_DEFAULT_HEIGHT,
                center.getZ() + (Constants.SETTLEMENT_DEFAULT_WIDTH)/2
        );
    }

    public SettlementBoundingBox(BlockPos min, BlockPos max) {
        this(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ());
    }

    public SettlementBoundingBox(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.minX = Math.min(minX, maxX);
        this.minY = Math.min(minY, maxX);
        this.minZ = Math.min(minZ, maxX);
        this.maxX = Math.max(minX, maxX);
        this.maxY = Math.max(minY, maxY);
        this.maxZ = Math.max(minZ, maxZ);
    }

    @Override
    public BlockPos getMinimumPosition() {
        return new BlockPos(minX, minY, minZ);
    }

    @Override
    public BlockPos getMaximumPosition() {
        return new BlockPos(maxX, maxY, maxZ);
    }

    @Override
    public int minX() {
        return minX;
    }

    @Override
    public int minY() {
        return minY;
    }

    @Override
    public int minZ() {
        return minZ;
    }

    @Override
    public int maxX() {
        return maxX;
    }

    @Override
    public int maxY() {
        return maxY;
    }

    @Override
    public int maxZ() {
        return maxZ;
    }

    @Override
    public int xSize() {
        return maxX - minX;
    }

    @Override
    public int ySize() {
        return maxY - minY;
    }

    @Override
    public int zSize() {
        return maxZ - minZ;
    }

    @Override
    public SettlementBoundingBox expandToFit(IBoundingBox inner) {
        minX = minX < inner.minX() ? minX : inner.minX();
        minY = minY < inner.minY() ? minY : inner.minY();
        minZ = minZ < inner.minZ() ? minZ : inner.minZ();
        maxX = maxX > inner.maxX() ? maxX : inner.maxX();
        maxY = maxY > inner.maxY() ? maxY : inner.maxY();
        maxZ = maxZ > inner.maxZ() ? maxZ : inner.maxZ();
        return this;
    }

    @Override
    public SettlementBoundingBox expandToFit(BlockPos pos) {
        if(pos.getX() > maxX) {
            maxX = pos.getX();
        } else if(pos.getX() < minX) {
            minX = pos.getX();
        }
        if(pos.getY() > maxY) {
            maxY = pos.getY();
        } else if(pos.getY() < minY) {
            minY = pos.getY();
        }
        if(pos.getZ() > maxZ) {
            maxZ = pos.getZ();
        } else if(pos.getZ() < minZ) {
            minZ = pos.getZ();
        }
        return this;
    }

    @Override
    public SettlementBoundingBox offset(BlockPos offset) {
        minX = minX + offset.getX();
        minY = minY + offset.getY();
        minZ = minZ + offset.getZ();
        maxX = maxX + offset.getX();
        maxY = maxY + offset.getY();
        maxZ = maxZ + offset.getZ();
        return this;
    }


    @Override
    public boolean isWithinBounds(BlockPos pos) {
        return isWithinBounds(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public boolean isWithinBounds(double x, double y, double z) {
        return (x >= minX && x <= maxX)
                && (y >= minY && y <= maxY)
                && (z >= minZ && z <= maxZ);
    }

    @Override
    public boolean intersects(IBoundingBox other) {
        return !(other.maxX() < this.minX() || other.maxY() < this.minY() || other.maxZ() < this.minZ())
                && !(this.maxX() < other.minX() || this.maxY() < other.minY() || this.maxZ() < other.minZ());
    }
}
