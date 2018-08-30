package com.infinityraider.settlercraft.settlement.settler.dialogue;

import com.infinityraider.settlercraft.api.v1.IDialogueOption;
import com.infinityraider.settlercraft.api.v1.ISettler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

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
    public List<ITextComponent> getSettlerText() {
        List<ITextComponent> list = new ArrayList<>();
        if(isMayor()) {
            list.add(new TextComponentTranslation(getDiscriminator() + "noProblem1"));
            list.add(new TextComponentTranslation(getDiscriminator() + "noProblem2"));
        } else {
            list.add(new TextComponentTranslation(getDiscriminator() + "likewise"));
        }
        return list;
    }

    @Override
    public List<ITextComponent> getPlayerText() {
        List<ITextComponent> list = new ArrayList<>();
        if(isMayor()) {
            list.add(new TextComponentTranslation(getDiscriminator() + "forgetful"));
        } else {
            list.add(new TextComponentTranslation(getDiscriminator() + "helloName")
                    .appendSibling(new TextComponentString(" " + getSettler().getFirstName() + " " + getSettler().getSurname() + ".")));
        }
        return list;
    }
}
