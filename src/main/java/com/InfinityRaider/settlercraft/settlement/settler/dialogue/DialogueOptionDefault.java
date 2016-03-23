package com.InfinityRaider.settlercraft.settlement.settler.dialogue;

import com.InfinityRaider.settlercraft.api.v1.ISettler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.translation.I18n;

import java.util.ArrayList;
import java.util.List;

public class DialogueOptionDefault extends DialogueOptionBase {
    public DialogueOptionDefault(EntityPlayer player, ISettler settler) {
        super(player, settler);
    }

    @Override
    public boolean onDialogueOptionSelected(EntityPlayer player, ISettler settler) {
        return false;
    }

    @Override
    public void onContainerClosed(EntityPlayer player, ISettler settler) {

    }

    @Override
    public List<String> getLocalizedDialogueAnswerString() {
        List<String> list = new ArrayList<>();
        if(getSettler().settlement() == null) {
            list.add(I18n.translateToLocal(getDiscriminator() + "greetPlayer"));
        } else {
            if(isMayor()) {
                list.add(I18n.translateToLocal(getDiscriminator() + "greetMayor1"));
                list.add(I18n.translateToLocal(getDiscriminator() + "greetMayor2"));
            } else {
                list.add(I18n.translateToLocal(getDiscriminator() + "greetOtherPlayer"));
            }
        }
        return list;
    }

    @Override
    public List<String> getLocalizedDialogueQuestionString() {
        List<String> list = new ArrayList<>();
        list.add(I18n.translateToLocal(getDiscriminator() + "greetSettler"));
        return list;
    }
}
