package com.InfinityRaider.settlercraft.settlement.settler.dialogue.builder;

import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.settlement.settler.dialogue.DialogueOptionBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.List;

public class DialogueOptionStay extends DialogueOptionBase {
    public DialogueOptionStay(EntityPlayer player, ISettler settler) {
        super(player, settler);
    }

    @Override
    public boolean onDialogueOptionSelected(EntityPlayer player, ISettler settler) {
        return true;
    }

    @Override
    public void onContainerClosed(EntityPlayer player, ISettler settler) {
        EntityPlayer current = settler.getCurrentlyFollowingPlayer();
        if(current != null && current.getUniqueID().equals(player.getUniqueID())) {
            settler.followPlayer(null);
        }
    }

    @Override
    public List<String> getLocalizedDialogueAnswerString() {
        List<String> list = new ArrayList<>();
        if(isMayor()) {
            list.add(StatCollector.translateToLocal(getDiscriminator() + "backToWork"));
        } else {
            list.add(StatCollector.translateToLocal(getDiscriminator() + "stayingPut"));

        }
        return list;
    }

    @Override
    public List<String> getLocalizedDialogueQuestionString() {
        List<String> list = new ArrayList<>();
        if(isMayor()) {
            list.add(StatCollector.translateToLocal(getDiscriminator() + "stayCitizen"));
        } else {
            list.add(StatCollector.translateToLocal(getDiscriminator() + "stay"));
        }
        return list;
    }
}
