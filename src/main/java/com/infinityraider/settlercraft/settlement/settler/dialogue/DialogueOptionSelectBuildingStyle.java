package com.infinityraider.settlercraft.settlement.settler.dialogue;

import com.infinityraider.settlercraft.api.v1.IBuildingStyle;
import com.infinityraider.settlercraft.api.v1.IDialogueOption;
import com.infinityraider.settlercraft.api.v1.ISettlement;
import com.infinityraider.settlercraft.api.v1.ISettler;
import com.infinityraider.settlercraft.settlement.SettlementHandler;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.ArrayList;
import java.util.List;

public class DialogueOptionSelectBuildingStyle extends DialogueOptionBase {
    private final IBuildingStyle style;

    public DialogueOptionSelectBuildingStyle(EntityPlayer player, ISettler settler, IBuildingStyle style) {
        super(player, settler);
        this.style = style;
    }

    @Override
    public List<IDialogueOption> getDialogueOptions(EntityPlayer player, ISettler settler) {
        return ImmutableList.of();
    }

    @Override
    public boolean onDialogueOptionSelected(EntityPlayer player, ISettler settler) {
        return true;
    }

    @Override
    public void onContainerClosed(EntityPlayer player, ISettler settler) {
        if(!player.getEntityWorld().isRemote) {
            ISettlement settlement = SettlementHandler.getInstance().startNewSettlement(player, style);
            if(settlement != null) {
                settlement.addInhabitant(settler);
            }
        }
    }

    @Override
    public List<ITextComponent> getSettlerText() {
        List<ITextComponent> list = new ArrayList<>();
        list.add(new TextComponentTranslation(getDiscriminator() + "startSettlement"));
        return list;
    }

    @Override
    public List<ITextComponent> getPlayerText() {
        List<ITextComponent> list = new ArrayList<>();
        list.add(new TextComponentTranslation(style.getName()));
        return list;
    }
}
