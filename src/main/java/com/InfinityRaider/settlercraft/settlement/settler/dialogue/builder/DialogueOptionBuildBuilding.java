package com.InfinityRaider.settlercraft.settlement.settler.dialogue.builder;

import com.InfinityRaider.settlercraft.api.v1.IDialogueOption;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.settlement.settler.dialogue.DialogueOptionBase;
import com.InfinityRaider.settlercraft.settlement.settler.dialogue.DialogueOptionEndConversation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DialogueOptionBuildBuilding extends DialogueOptionBase {
    public DialogueOptionBuildBuilding(EntityPlayer player, ISettler settler) {
        super(player, settler);
    }

    @Override
    public List<IDialogueOption> getDialogueOptions(EntityPlayer player, ISettler settler) {
        List<IDialogueOption> list = settler.settlement().getBuildableBuildings().stream().map(building
                -> new DialogueOptionSelectBuilding(player, settler, building)).collect(Collectors.toList());
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
        list.add(StatCollector.translateToLocal(getDiscriminator() + "whatBuilding"));
        return list;
    }

    @Override
    public List<String> getLocalizedDialogueQuestionString() {
        List<String> list = new ArrayList<>();
        list.add(StatCollector.translateToLocal(getDiscriminator() + "buildBuilding"));
        return list;
    }
}
