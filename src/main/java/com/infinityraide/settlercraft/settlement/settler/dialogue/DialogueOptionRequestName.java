package com.infinityraide.settlercraft.settlement.settler.dialogue;

import com.infinityraide.settlercraft.api.v1.IDialogueOption;
import com.infinityraide.settlercraft.api.v1.ISettler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

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
    public List<ITextComponent> getSettlerText() {
        List<ITextComponent> list = new ArrayList<>();
        list.add(new TextComponentTranslation(getDiscriminator() + "giveSettlerName")
                .appendSibling(new TextComponentString(" " + getSettler().getFirstName() + " " + getSettler().getSurname()+".")));
        return list;
    }

    @Override
    public List<ITextComponent> getPlayerText() {
        List<ITextComponent> list = new ArrayList<>();
        if(isMayor()) {
            list.add(new TextComponentTranslation(getDiscriminator() + "getSettlerName3"));
        } else {
            list.add(new TextComponentTranslation(getDiscriminator() + "getSettlerName1")
                            .appendSibling(new TextComponentString(" ")
                            .appendSibling(getPlayer().getDisplayName())
                            .appendSibling(new TextComponentString(","))));
            list.add(new TextComponentTranslation(getDiscriminator() + "getSettlerName2"));
        }
        return list;
    }
}
