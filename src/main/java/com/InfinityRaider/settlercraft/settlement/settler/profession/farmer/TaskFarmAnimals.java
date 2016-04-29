package com.InfinityRaider.settlercraft.settlement.settler.profession.farmer;

import com.InfinityRaider.settlercraft.api.v1.IDialogueOption;
import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.api.v1.ISettlementBuilding;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.settlement.building.farm.BuildingAnimalFarm;
import com.InfinityRaider.settlercraft.settlement.settler.profession.TaskBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class TaskFarmAnimals extends TaskBase {
    private final BuildingAnimalFarm farm;

    public TaskFarmAnimals(ISettlement settlement, ISettler settler, ISettlementBuilding building, BuildingAnimalFarm farm) {
        super("farmAnimals", settlement, settler, building);
        this.farm = farm;
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
    public boolean completed() {
        return false;
    }

    @Override
    public List<IDialogueOption> getTaskSpecificDialogueOptions(EntityPlayer player) {
        return null;
    }
}
