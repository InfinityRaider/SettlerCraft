package com.InfinityRaider.settlercraft.settlement.settler.ai;

import com.InfinityRaider.settlercraft.api.v1.ITask;
import net.minecraft.entity.ai.EntityAIBase;

import java.util.ArrayList;
import java.util.List;

public class EntityAIPerformJob extends EntityAIBase {
    private List<ITask> tasks;

    public EntityAIPerformJob() {
        super();
        this.tasks = new ArrayList<>();
    }

    @Override
    public boolean shouldExecute() {
        return tasks.size() > 0;
    }
}
