package com.infinityraide.settlercraft.settlement.settler.dialogue;

import com.infinityraide.settlercraft.api.v1.ISettler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

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
    public List<ITextComponent> getSettlerText() {
        List<ITextComponent> list = new ArrayList<>();
        if(getSettler().settlement() == null) {
            list.add(new TextComponentTranslation(getDiscriminator() + "greetPlayer"));
        } else {
            if(isMayor()) {
                list.add(new TextComponentTranslation(getDiscriminator() + "greetMayor1"));
                list.add(new TextComponentTranslation(getDiscriminator() + "greetMayor2"));
            } else {
                list.add(new TextComponentTranslation(getDiscriminator() + "greetOtherPlayer"));
                list.add(new TextComponentString(getSettler().settlement().name()));
            }
        }
        return list;
    }

    @Override
    public List<ITextComponent> getPlayerText() {
        List<ITextComponent> list = new ArrayList<>();
        list.add(new TextComponentTranslation(getDiscriminator() + "greetSettler"));
        return list;
    }
}
