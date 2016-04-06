package com.InfinityRaider.settlercraft.settlement.settler.dialogue;

import com.InfinityRaider.settlercraft.api.v1.IDialogueOption;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.translation.I18n;

import java.util.ArrayList;
import java.util.List;

public class DialogueOptionReplyName extends DialogueOptionBase {
    private final IDialogueOption fallback;

    public DialogueOptionReplyName(EntityPlayer player, ISettler settler, IDialogueOption fallback) {
        super(player, settler);
        this.fallback = fallback;
    }

    @Override
     public List<IDialogueOption> getDialogueOptions(EntityPlayer player, ISettler settler) {
        List<IDialogueOption> list = fallback.getDialogueOptions(player, settler);
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
    public List<String> getLocalizedSettlerTextString() {
        List<String> list = new ArrayList<>();
        if(isMayor()) {
            list.add(I18n.translateToLocal(getDiscriminator() + "noProblem1") + ".");
            list.add(I18n.translateToLocal(getDiscriminator() + "noProblem2") + ".");
        } else {
            list.add(I18n.translateToLocal(getDiscriminator() + "likewise") + ".");
        }
        return list;
    }

    @Override
    public List<String> getLocalizedPlayerTextString() {
        List<String> list = new ArrayList<>();
        if(isMayor()) {
            list.add(I18n.translateToLocal(getDiscriminator() + "forgetful")+".");
        } else {
            list.add(I18n.translateToLocal(getDiscriminator() + "helloName") + " " + getSettler().getFirstName() + " " + getSettler().getSurname() + ".");
        }
        return list;
    }
}
