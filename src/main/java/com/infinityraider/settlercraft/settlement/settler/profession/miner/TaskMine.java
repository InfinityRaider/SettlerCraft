package com.infinityraider.settlercraft.settlement.settler.profession.miner;

import com.infinityraider.settlercraft.api.v1.IDialogueOption;
import com.infinityraider.settlercraft.api.v1.ISettlement;
import com.infinityraider.settlercraft.api.v1.ISettlementBuilding;
import com.infinityraider.settlercraft.api.v1.ISettler;
import com.infinityraider.settlercraft.settlement.building.quarry.BuildingQuarry;
import com.infinityraider.settlercraft.settlement.settler.ai.task.TaskBuildingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class TaskMine extends TaskBuildingBase<BuildingQuarry> {
    public TaskMine(ISettlement settlement, ISettler settler, ISettlementBuilding building, BuildingQuarry quarry) {
        super("mine", settler, settlement, building, quarry);
    }

    @Override
    public void onTaskStarted() {

    }

    @Override
    public void onTaskUpdated() {

    }

    @Override
    public void onTaskCancel() {

    }

    @Override
    public boolean isCompleted() {
        return false;
    }

    @Override
    public void onTaskCompleted() {

    }

    @Override
    public List<IDialogueOption> getTaskSpecificDialogueOptions(EntityPlayer player) {
        return null;
    }
}
