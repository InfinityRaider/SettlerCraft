package com.InfinityRaider.settlercraft.settlement.settler.profession.builder;

import com.InfinityRaider.settlercraft.api.v1.IDialogueOption;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.settlement.settler.dialogue.DialogueOptionBase;
import com.InfinityRaider.settlercraft.settlement.settler.dialogue.DialogueOptionEndConversation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.translation.I18n;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DialogueOptionTaskBuilder extends DialogueOptionBase {
    private final TaskBuildBuilding task;

    public DialogueOptionTaskBuilder(EntityPlayer player, ISettler settler, TaskBuildBuilding task) {
        super(player, settler);
        this.task = task;
    }

    @Override
    public List<IDialogueOption> getDialogueOptions(EntityPlayer player, ISettler settler) {
        List<IDialogueOption> list = new ArrayList<>();
        list.add(new DialogueOptionEndConversation(player, settler));
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
        if(task.getBuilding().getBlocksToClear().size() > 0) {
            list.add(I18n.translateToLocal(getDiscriminator() + "builder.clearingBlocks"));
        } else {
            list.add(I18n.translateToLocal(getDiscriminator() + "builder.buildingStructure"));
            if(task.hasMissingResources()) {
                list.add(I18n.translateToLocal(getDiscriminator() + "builder.missingResources"));
                list.addAll(task.getMissingResources().stream().map(
                        stack -> " - " + stack.stackSize + "x " + stack.getDisplayName()).collect(Collectors.toList()));
            }
        }
        return list;
    }

    @Override
    public List<String> getLocalizedDialogueQuestionString() {
        List<String> list = new ArrayList<>();
        list.add(I18n.translateToLocal(getDiscriminator() + "builder.task"));
        return list;
    }
}
