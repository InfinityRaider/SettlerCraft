package com.infinityraider.settlercraft.settlement.settler.profession.builder;

import com.infinityraider.settlercraft.api.v1.IBuilding;
import com.infinityraider.settlercraft.api.v1.ISettlement;
import com.infinityraider.settlercraft.api.v1.ISettler;
import com.infinityraider.settlercraft.settlement.SettlementBuilding;
import com.infinityraider.settlercraft.settlement.building.StructureBuildProgress;
import com.infinityraider.settlercraft.settlement.settler.ai.task.TaskBuildingBase;
import com.infinityraider.settlercraft.api.v1.IDialogueOption;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.ArrayList;
import java.util.List;

public class TaskBuildBuilding extends TaskBuildingBase<IBuilding> {
    private final StructureBuildProgress buildProgress;
    private StructureBuildProgress.Work job;
    private boolean taskAssigned;

    public TaskBuildBuilding(ISettlement settlement, ISettler settler, SettlementBuilding building, StructureBuildProgress buildProgress) {
        super("buildBuilding", settler, settlement, building, building.building());
        this.buildProgress = buildProgress;
        this.taskAssigned = false;
    }

    public void onSubTaskCancelled() {
        this.job.onSubTaskCancelled(this);
        this.job = null;
        this.taskAssigned = false;
    }

    public void onSubTaskCompleted() {
        this.job.onSubTaskCompleted(this);
        this.job = null;
        this.taskAssigned = false;
    }

    @Override
    public void onTaskStarted() {
        if(job == null) {
            job = buildProgress.getNextJob();
        }
    }

    @Override
    public void onTaskUpdated() {
        if(job == null) {
            job = buildProgress.getNextJob();
        } else {
            if(!taskAssigned) {
                this.getSettler().assignTask(this.job.getTask(this));
            }
        }
    }

    @Override
    public void onTaskCancel() {
        if(this.job != null) {
            this.buildProgress.cancelJob(this.job);
        }
    }

    @Override
    public boolean isCompleted() {
        return buildProgress.isComplete();
    }

    @Override
    public void onTaskCompleted() {
        getSettler().setWorkPlace(null);
    }

    @Override
    public List<IDialogueOption> getTaskSpecificDialogueOptions(EntityPlayer player) {
        List<IDialogueOption> list = new ArrayList<>();
        list.add(new DialogueOptionTaskBuilder(player, getSettler(), this));
        return list;
    }

    public ITextComponent describeJob() {
        return new TextComponentTranslation("settlercraft.dialogue.job.building")
                .appendText(" ")
                .appendSibling(new TextComponentTranslation(getBuilding().name()));
    }
}
