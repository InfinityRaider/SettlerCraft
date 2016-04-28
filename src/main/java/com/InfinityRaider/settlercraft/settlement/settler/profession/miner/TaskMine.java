package com.InfinityRaider.settlercraft.settlement.settler.profession.miner;

import com.InfinityRaider.settlercraft.api.v1.IDialogueOption;
import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.api.v1.ISettlementBuilding;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.settlement.settler.profession.TaskBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class TaskMine extends TaskBase {
    public TaskMine(ISettlement settlement, ISettler settler, ISettlementBuilding building) {
        super("mine", settlement, settler, building);
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
