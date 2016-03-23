package com.InfinityRaider.settlercraft.settlement.settler.dialogue;

import com.InfinityRaider.settlercraft.api.v1.IDialogueOption;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.translation.I18n;

import java.util.ArrayList;
import java.util.List;

public class DialogueOptionRequestName extends DialogueOptionBase {
    private final IDialogueOption previous;

    public DialogueOptionRequestName(EntityPlayer player, ISettler settler, IDialogueOption previous) {
        super(player, settler);
        this.previous = previous;
    }

    @Override
    public List<IDialogueOption> getDialogueOptions(EntityPlayer player, ISettler settler) {
        List<IDialogueOption> list = previous.getDialogueOptions(player, settler);
        list.add(0, new DialogueOptionReplyName(player, settler, previous));
        int index = -1;
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i) instanceof DialogueOptionRequestName) {
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
        list.add(I18n.translateToLocal(getDiscriminator() + "giveSettlerName")+" "+getSettler().getFirstName() + " " + getSettler().getSurname()+".");
        return list;
    }

    @Override
    public List<String> getLocalizedDialogueQuestionString() {
        List<String> list = new ArrayList<>();
        if(isMayor()) {
            list.add(I18n.translateToLocal(getDiscriminator() + "getSettlerName3"));
        } else {
            list.add(I18n.translateToLocal(getDiscriminator() + "getSettlerName1") + " " + getPlayer().getDisplayName().getUnformattedText() + ",");
            list.add(I18n.translateToLocal(getDiscriminator() + "getSettlerName2"));
        }
        return list;
    }
}
