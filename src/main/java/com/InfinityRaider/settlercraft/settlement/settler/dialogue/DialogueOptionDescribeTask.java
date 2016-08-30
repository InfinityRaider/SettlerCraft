package com.InfinityRaider.settlercraft.settlement.settler.dialogue;

import com.InfinityRaider.settlercraft.api.v1.IDialogueOption;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.api.v1.ITask;
import com.InfinityRaider.settlercraft.utility.LogHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.ArrayList;
import java.util.List;

public class DialogueOptionDescribeTask extends DialogueOptionBase {
    private final IDialogueOption previous;
    private final ITask task;

    public DialogueOptionDescribeTask(EntityPlayer player, ISettler settler, IDialogueOption previous) {
        super(player, settler);
        this.previous = previous;
        this.task = getSettler().getCurrentTask();
    }

    @Override
    public List<IDialogueOption> getDialogueOptions(EntityPlayer player, ISettler settler) {
        List<IDialogueOption> list = previous.getDialogueOptions(player, settler);
        if(task != null) {
            list.addAll(0, task.getTaskSpecificDialogueOptions(player));
        }
        int index = -1;
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i) instanceof DialogueOptionDescribeTask) {
                index = i;
                break;
            }
        }
        if(index >= 0) {
            list.remove(index);
        }
        return list;
    }

    @Override
    public boolean onDialogueOptionSelected(EntityPlayer player, ISettler settler) {
        return false;
    }

    @Override
    public void onContainerClosed(EntityPlayer player, ISettler settler) {}

    @Override
    public List<ITextComponent> getSettlerText() {
        List<ITextComponent> list = new ArrayList<>();
        ITask task = getSettler().getCurrentTask();
        ISettler.SettlerStatus status = getSettler().getSettlerStatus();
        if(task == null) {
            list.add(new TextComponentTranslation(getDiscriminator() + "task.noTask"));
        }
        switch (status) {
            case IDLE:
                if(task != null) {
                    LogHelper.debug("settler is idle but still has a task");
                    list.addAll(task.getTaskDescription());
                }
                break;
            case FOLLOWING_PLAYER:
                EntityPlayer player = getSettler().getCurrentlyFollowingPlayer();
                if (player != null) {
                    //Data watcher is slow :s
                    list.add(new TextComponentTranslation(getDiscriminator() + "task.followingPlayer")
                            .appendSibling(new TextComponentString(" ")
                            .appendSibling(player.getDisplayName())
                            .appendSibling(new TextComponentString("."))));
                }
                break;
            case GETTING_FOOD:
                if(task != null) {
                    list.addAll(task.getTaskDescription());
                }
                list.add(new TextComponentTranslation(getDiscriminator() + "task.gettingFood"));
                break;
            case GOING_TO_BED:
                if(task != null) {
                    list.addAll(task.getTaskDescription());
                }
                list.add(new TextComponentTranslation(getDiscriminator() + "task.goingToBed"));
                break;
            case PERFORMING_TASK:
                if(task != null) {
                    list.addAll(task.getTaskDescription());
                }
                break;
            case FINDING_RESOURCE:
                if(task != null) {
                    list.addAll(task.getTaskDescription());
                }
                ItemStack missing = getSettler().getMissingResource();
                if (missing != null) {
                    list.add(new TextComponentTranslation(getDiscriminator() + "task.needResource"));
                    list.add(new TextComponentTranslation(getDiscriminator() + "task.findingResource")
                            .appendSibling(new TextComponentString(missing.getDisplayName())));
                }
                break;

        }
        return list;
    }

    @Override
    public List<ITextComponent> getPlayerText() {
        List<ITextComponent> list = new ArrayList<>();
        list.add(new TextComponentTranslation(getDiscriminator() + "describeTask"));
        return list;
    }
}
