package com.infinityraide.settlercraft.settlement.settler.dialogue;

import com.infinityraide.settlercraft.SettlerCraft;
import com.infinityraide.settlercraft.api.v1.IDialogueOption;
import com.infinityraide.settlercraft.api.v1.IMissingResource;
import com.infinityraide.settlercraft.api.v1.ISettler;
import com.infinityraide.settlercraft.api.v1.ITask;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Optional<IMissingResource> resource = settler.getMissingResource();
        if(resource.isPresent()) {
            list.addAll(resource.get().getDialogueOptions(settler, player));
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
        switch (status) {
            case IDLE:
                if (task != null) {
                    SettlerCraft.instance.getLogger().debug("settler is idle but still has a task");
                    list.addAll(task.getTaskDescription());
                }
                list.add(new TextComponentTranslation(getDiscriminator() + "task.noTask"));
                break;
            case FOLLOWING_PLAYER:
                EntityPlayer player = getSettler().getCurrentlyFollowingPlayer();
                list.add(new TextComponentTranslation(getDiscriminator() + "task.followingPlayer")
                        .appendSibling(new TextComponentString(" "))
                        .appendSibling(player.getDisplayName())
                        .appendSibling(new TextComponentString(".")));
                break;
            case GETTING_FOOD:
                if (task != null) {
                    list.addAll(task.getTaskDescription());
                    list.add(new TextComponentTranslation(getDiscriminator() + "task.gettingFoodWithTask"));
                } else {
                    list.add(new TextComponentTranslation(getDiscriminator() + "task.gettingFood"));
                }
                break;
            case GOING_TO_BED:
                if (task != null) {
                    list.addAll(task.getTaskDescription());
                    list.add(new TextComponentTranslation(getDiscriminator() + "task.goingToBedWithTask"));
                } else {
                    list.add(new TextComponentTranslation(getDiscriminator() + "task.goingToBed"));
                }
                break;
            case PERFORMING_TASK:
                list.addAll(task.getTaskDescription());
                break;
            case FINDING_RESOURCE:
                if (task != null) {
                    list.addAll(task.getTaskDescription());
                    list.add(new TextComponentTranslation(getDiscriminator() + "task.needResourceWithTask"));
                } else {
                    list.add(new TextComponentTranslation(getDiscriminator() + "task.needResource"));
                }
                IMissingResource missing = getSettler().getMissingResource().get();
                list.addAll(missing.getDescription(getSettler()));
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
