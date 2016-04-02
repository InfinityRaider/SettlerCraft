package com.InfinityRaider.settlercraft.settlement.settler.profession.builder;

import com.InfinityRaider.settlercraft.api.v1.*;
import com.InfinityRaider.settlercraft.settlement.SettlementBuildingIncomplete;
import com.InfinityRaider.settlercraft.settlement.settler.profession.TaskBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class TaskBuildBuilding extends TaskBase {
    private final StructureBuildProgress buildProgress;
    private StructureBuildProgress.Work job;

    public TaskBuildBuilding(ISettlement settlement, ISettler settler, SettlementBuildingIncomplete building, StructureBuildProgress buildProgress) {
        super("buildBuilding", settlement, settler, building);
        this.buildProgress = buildProgress;
    }

    @Override
    public SettlementBuildingIncomplete getBuilding() {
        return (SettlementBuildingIncomplete) super.getBuilding();
    }

    @Override
    public void startTask() {
        if(job == null) {
            job = buildProgress.getJob();
        }
    }

    @Override
    public void updateTask() {
        if(job == null) {
            job = buildProgress.getJob();
        } else {
            ItemStack stack = job.getResource();
            if(getSettler().getSettlerInventory().hasStack(stack)) {
                BlockPos target = job.getWorkPos();
                double reach = 2;
                if(getDistanceFromPositionSquared(target) <= reach * reach) {
                    getSettler().getSettlerInventory().consumeStack(stack);
                    buildProgress.doJob(job);
                } else {
                    getEntitySettler().getNavigator().tryMoveToXYZ(target.getX() + 0.5D, target.getY(), target.getZ() + 0.5D, getEntitySettler().getAIMoveSpeed());
                }
            } else {
                getSettler().setMissingResource(stack);
            }
        }
    }

    @Override
    public void cancelTask() {
        if(job != null) {
            buildProgress.cancelJob();
            job = null;
        }
    }

    @Override
    public boolean completed() {
        return buildProgress.isComplete();
    }

    @Override
    public List<IDialogueOption> getTaskSpecificDialogueOptions(EntityPlayer player) {
        List<IDialogueOption> list = new ArrayList<>();
        list.add(new DialogueOptionTaskBuilder(player, getSettler(), this));
        return list;
    }

    public String describeJob() {
        return job == null ? "builder.standby" : job.describeJob();
    }
}
