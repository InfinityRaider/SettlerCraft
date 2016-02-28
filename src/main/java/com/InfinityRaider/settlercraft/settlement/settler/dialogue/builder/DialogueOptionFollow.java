package com.InfinityRaider.settlercraft.settlement.settler.dialogue.builder;

import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.settlement.settler.dialogue.DialogueOptionBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.List;

public class DialogueOptionFollow extends DialogueOptionBase {
    public DialogueOptionFollow(EntityPlayer player, ISettler settler) {
        super(player, settler);
    }

    @Override
    public boolean onDialogueOptionSelected(EntityPlayer player, ISettler settler) {
        return true;
    }

    @Override
    public void onContainerClosed(EntityPlayer player, ISettler settler) {
        if(settler.getCurrentlyFollowingPlayer() == null) {
            settler.followPlayer(player);
        }
    }

    @Override
    public List<String> getLocalizedDialogueAnswerString() {
        List<String> list = new ArrayList<>();
        list.add(StatCollector.translateToLocal(getDiscriminator() + "following"));
        return list;
    }

    @Override
    public List<String> getLocalizedDialogueQuestionString() {
        List<String> list = new ArrayList<>();
        if(isMayor()) {
            list.add(StatCollector.translateToLocal(getDiscriminator() + "followMeCitizen"));
        } else {
            list.add(StatCollector.translateToLocal(getDiscriminator() + "followMe"));
        }
        return list;
    }
}
