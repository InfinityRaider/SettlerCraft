package com.InfinityRaider.settlercraft.settlement.settler.profession.farmer;

import com.InfinityRaider.settlercraft.api.v1.IDialogueOption;
import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.api.v1.ISettlementBuilding;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.settlement.building.farm.BuildingCropFarm;
import com.InfinityRaider.settlercraft.settlement.settler.ai.task.TaskBuildingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class TaskFarmPlants extends TaskBuildingBase<BuildingCropFarm> {
    public TaskFarmPlants(ISettlement settlement, ISettler settler, ISettlementBuilding building, BuildingCropFarm farm) {
        super("farmPlants", settler, settlement, building, farm);
    }

    @Override
    public void startTask() {

    }

    @Override
    public void updateTask() {

    }

    @Override
    public void cancelTask() {

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
