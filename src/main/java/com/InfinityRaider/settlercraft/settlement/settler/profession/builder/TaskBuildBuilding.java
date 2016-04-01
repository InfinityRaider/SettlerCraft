package com.InfinityRaider.settlercraft.settlement.settler.profession.builder;

import com.InfinityRaider.settlercraft.api.v1.*;
import com.InfinityRaider.settlercraft.settlement.SettlementBuildingIncomplete;
import com.InfinityRaider.settlercraft.settlement.settler.profession.TaskBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TaskBuildBuilding extends TaskBase {
    private final StructureBuildProgress buildProgress;

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

    }

    @Override
    public void updateTask() {

    }

    @Override
    public void cancelTask() {

    }

    @Override
    public boolean completed() {
        return false;
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public List<IDialogueOption> getTaskSpecificDialogueOptions(EntityPlayer player) {
        List<IDialogueOption> list = new ArrayList<>();
        list.add(new DialogueOptionTaskBuilder(player, getSettler(), this));
        return list;
    }

    //TODO
    public boolean hasMissingResources() {
        return true;
    }

    //TODO
    public List<ItemStack> getMissingResources() {
        List<ItemStack> resources = new ArrayList<>();

        return resources;
    }
}
