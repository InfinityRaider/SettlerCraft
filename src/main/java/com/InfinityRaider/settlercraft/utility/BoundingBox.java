package com.InfinityRaider.settlercraft.utility;

import com.InfinityRaider.settlercraft.api.v1.IBoundingBox;
import com.InfinityRaider.settlercraft.reference.Constants;
import net.minecraft.util.math.BlockPos;

public class BoundingBox extends com.infinityraider.infinitylib.utility.BoundingBox implements IBoundingBox {
    public BoundingBox(BlockPos min, BlockPos max) {
        this(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ());
    }

    public BoundingBox(BoundingBox box) {
        super(box);
    }

    public BoundingBox(BlockPos center) {
        this(
                center.getX() - (Constants.SETTLEMENT_DEFAULT_WIDTH)/2,
                center.getY(),
                center.getZ() - (Constants.SETTLEMENT_DEFAULT_WIDTH)/2,
                center.getX() + (Constants.SETTLEMENT_DEFAULT_WIDTH)/2,
                center.getY() + (Constants.SETTLEMENT_DEFAULT_HEIGHT),
                center.getZ() + (Constants.SETTLEMENT_DEFAULT_WIDTH)/2
        );
    }

    public BoundingBox(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        super(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    public BoundingBox copy() {
        return new BoundingBox(this);
    }

    @Override
    public BoundingBox expand(int amount) {
        super.expand(amount);
        return this;
    }

    @Override
    public BoundingBox expandToFit(BlockPos pos) {
        super.expandToFit(pos);
        return this;
    }

    @Override
    public BoundingBox expandToFit(int x, int y, int z) {
        super.expandToFit(x, y, z);
        return this;
    }

    @Override
    public BoundingBox expandToFit(IBoundingBox inner) {
        this.expandToFit(inner.getMinimumPosition()).expandToFit(inner.getMaximumPosition());
        return this;
    }

    @Override
    public BoundingBox offset(int x, int y, int z) {
        super.offset(x, y, z);
        return this;
    }

    @Override
    public BoundingBox offset(BlockPos offset) {
        super.offset(offset);
        return this;
    }

    @Override
    public BoundingBox rotate(int amount) {
        super.rotate(amount);
        return this;
    }

    @Override
    public boolean intersects(IBoundingBox other) {
        if(other instanceof BoundingBox) {
            return super.intersects((BoundingBox) other);
        }
        return !(other.maxX()+1 <= this.minX() || other.maxY()+1 <= this.minY() || other.maxZ()+1 <= this.minZ())
                && !(this.maxX()+1 <= other.minX() || this.maxY()+1 <= other.minY() || this.maxZ()+1 <= other.minZ());
    }
}
