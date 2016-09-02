package com.InfinityRaider.settlercraft.settlement.settler.ai.task;

import com.InfinityRaider.settlercraft.api.v1.ISettler;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class TaskUseItemOnBlock extends TaskUseItem {
    private final BlockPos pos;
    private final EnumFacing face;

    public TaskUseItemOnBlock(String taskName, ISettler settler, BlockPos pos, EnumFacing face, int slot, int priority) {
        this(taskName, settler, pos, face, slot, priority, 0);
    }

    public TaskUseItemOnBlock(String taskName, ISettler settler, BlockPos pos, EnumFacing face, int slot, int priority, int useTicks) {
        super(taskName, settler, slot, priority, useTicks);
        this.pos = pos;
        this.face = face;
    }

    @Override
    public void updateTask() {
        if(isInReach()) {
            super.updateTask();
        } else {
            this.resetTimer();
            this.moveToPosition();
        }
    }

    protected void moveToPosition() {
        //TODO
    }

    protected boolean isInReach() {
        //TODO
        return true;
    }

}
