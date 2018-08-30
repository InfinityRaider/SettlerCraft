package com.infinityraider.settlercraft.settlement.settler.dialogue;

import com.infinityraider.settlercraft.api.v1.ISettlement;
import com.infinityraider.settlercraft.api.v1.ISettler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.ArrayList;
import java.util.List;

public class DialogueOptionJoinSettlement extends DialogueOptionBase {
    private final ISettlement settlement;

    public DialogueOptionJoinSettlement(EntityPlayer player, ISettler settler, ISettlement settlement) {
        super(player, settler);
        this.settlement = settlement;
    }

    @Override
    public boolean onDialogueOptionSelected(EntityPlayer player, ISettler settler) {
        return true;
    }

    @Override
    public void onContainerClosed(EntityPlayer player, ISettler settler) {
        settlement.addInhabitant(settler);
    }

    @Override
    public List<ITextComponent> getSettlerText() {
        List<ITextComponent> list = new ArrayList<>();
        list.add(new TextComponentTranslation(getDiscriminator() + "aNewHome"));
        return list;
    }

    @Override
    public List<ITextComponent> getPlayerText() {
        List<ITextComponent> list = new ArrayList<>();
        list.add(new TextComponentTranslation(getDiscriminator() + "joinSettlement"));
        return list;
    }
}
