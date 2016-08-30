package com.InfinityRaider.settlercraft.settlement.settler.profession.miner;

import com.InfinityRaider.settlercraft.api.v1.IDialogueOption;
import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.api.v1.ISettlementBuilding;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.settlement.building.quarry.BuildingQuarry;
import com.InfinityRaider.settlercraft.settlement.settler.profession.TaskBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class TaskMine extends TaskBase {
    private final BuildingQuarry quarry;

    public TaskMine(ISettlement settlement, ISettler settler, ISettlementBuilding building, BuildingQuarry quarry) {
        super("mine", settlement, settler, building);
        this.quarry = quarry;
    }

    @Override
    public void startTask() {

    }

    @Override
    public void updateTask() {

    }

    @Override
    public void resetTask() {

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
