package com.infinityraide.settlercraft.settlement.settler.profession.farmer;

import com.infinityraide.settlercraft.api.v1.IDialogueOption;
import com.infinityraide.settlercraft.api.v1.ISettlement;
import com.infinityraide.settlercraft.api.v1.ISettlementBuilding;
import com.infinityraide.settlercraft.api.v1.ISettler;
import com.infinityraide.settlercraft.settlement.building.farm.BuildingCropFarm;
import com.infinityraide.settlercraft.settlement.settler.ai.task.TaskBuildingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class TaskFarmPlants extends TaskBuildingBase<BuildingCropFarm> {
    public TaskFarmPlants(ISettlement settlement, ISettler settler, ISettlementBuilding building, BuildingCropFarm farm) {
        super("farmPlants", settler, settlement, building, farm);
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
