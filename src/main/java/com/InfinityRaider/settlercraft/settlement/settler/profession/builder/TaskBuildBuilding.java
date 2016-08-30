package com.InfinityRaider.settlercraft.settlement.settler.profession.builder;

import com.InfinityRaider.settlercraft.api.v1.*;
import com.InfinityRaider.settlercraft.settlement.SettlementBuilding;
import com.InfinityRaider.settlercraft.settlement.StructureBuildProgress;
import com.InfinityRaider.settlercraft.settlement.settler.profession.TaskBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.ArrayList;
import java.util.List;

public class TaskBuildBuilding extends TaskBase {
    private final StructureBuildProgress buildProgress;
    private StructureBuildProgress.Work job;
    private boolean pathFinding;

    public TaskBuildBuilding(ISettlement settlement, ISettler settler, SettlementBuilding building, StructureBuildProgress buildProgress) {
        super("buildBuilding", settlement, settler, building);
        this.buildProgress = buildProgress;
    }

    @Override
    public void startTask() {
        if(job == null) {
            job = buildProgress.getNextJob();
        }
    }

    @Override
    public void updateTask() {
        if(job == null) {
            job = buildProgress.getNextJob();
            if(job == null) {
                getSettler().setWorkPlace(null);
            }
        } else {
            if(!buildProgress.validateJob(job)) {
                job = null;
                pathFinding = false;
                return;
            }
            ItemStack stack = job.getResource();
            if(getSettler().getSettlerInventory().hasStack(stack)) {
                BlockPos target = job.getWorkPos();
                double reach = 2.5;
                double distance = getDistanceFromPositionSquared(target);
                if(distance <= reach * reach) {
                    getSettler().getSettlerInventory().consumeStack(stack);
                    for(ItemStack gained : job.getGainedResources()) {
                        ItemStack remaining = getSettler().getSettlerInventory().addStackToInventory(gained);
                        if(remaining != null) {
                            EntityLivingBase entity = getEntitySettler();
                            EntityItem item = new EntityItem(getSettler().getWorld(), entity.posX, entity.posY, entity.posZ, remaining);
                            getSettler().getWorld().spawnEntityInWorld(item);
                        }
                    }
                    buildProgress.doJob(job);
                    job = null;
                    pathFinding = false;
                } else if(!pathFinding || this.getEntitySettler().getNavigator().noPath()){
                    getEntitySettler().getNavigator().tryMoveToXYZ(target.getX() + 0.5D, target.getY(), target.getZ() + 0.5D, 1.1F);
                    pathFinding = true;
                }
            } else {
                getSettler().setMissingResource(stack);
            }
        }
    }

    @Override
    public void resetTask() {

    }

    @Override
    public void cancelTask() {
        if(job != null) {
            buildProgress.cancelJob(job);
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

    public ITextComponent describeJob() {
        return new TextComponentTranslation("settlercraft.dialogue.task.building")
                .appendText(" ")
                .appendSibling(new TextComponentTranslation(getBuilding().building().name()));
    }
}
