package com.InfinityRaider.settlercraft.settlement.settler.dialogue;

import com.InfinityRaider.settlercraft.api.v1.IDialogueOption;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.api.v1.ITask;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.translation.I18n;

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
    public List<String> getLocalizedDialogueAnswerString() {
        List<String> list = new ArrayList<>();
        switch (getSettler().getSettlerStatus()) {
            case IDLE:
                list.add(I18n.translateToLocal(getDiscriminator() + "task.noTask"));
                break;
            case FOLLOWING_PLAYER:
                EntityPlayer player = getSettler().getCurrentlyFollowingPlayer();
                if(player == null) {
                    //DataWatcher is slow :s
                    list.add(I18n.translateToLocal(getDiscriminator() + "task.noTask"));
                } else {
                    list.add(I18n.translateToLocal(getDiscriminator() + "task.followingPlayer") + " "
                            + player.getDisplayName().getFormattedText() + ".");
                }
                break;
            case GETTING_FOOD:
                list.add(I18n.translateToLocal(getDiscriminator() + "task.gettingFood"));
                break;
            case GOING_TO_BED:
                list.add(I18n.translateToLocal(getDiscriminator() + "task.goingToBed"));
                break;
            case PERFORMING_TASK:
                if(task != null) {
                    list.add(task.getTaskDescription());
                } else {
                    list.add(I18n.translateToLocal(getDiscriminator() + "task.noTask"));
                }
                break;
            case FINDING_RESOURCE:
                if(task != null) {
                    list.add(task.getTaskDescription());
                } else {
                    list.add(I18n.translateToLocal(getDiscriminator() + "task.findingResource") + " " + getSettler().getMissingResource().getDisplayName());
                }
                break;
        }
        return list;
    }

    @Override
    public List<String> getLocalizedDialogueQuestionString() {
        List<String> list = new ArrayList<>();
        list.add(I18n.translateToLocal(getDiscriminator() + "describeTask"));
        return list;
    }
}
