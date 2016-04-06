package com.InfinityRaider.settlercraft.settlement.settler.dialogue;

import com.InfinityRaider.settlercraft.api.v1.IBuildingStyle;
import com.InfinityRaider.settlercraft.api.v1.IDialogueOption;
import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.settlement.SettlementHandler;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.translation.I18n;

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
        if(!player.worldObj.isRemote) {
            ISettlement settlement = SettlementHandler.getInstance().startNewSettlement(player, style);
            settlement.addInhabitant(settler);
        }
    }

    @Override
    public List<String> getLocalizedSettlerTextString() {
        List<String> list = new ArrayList<>();
        list.add(I18n.translateToLocal(getDiscriminator() + "startSettlement"));
        return list;
    }

    @Override
    public List<String> getLocalizedPlayerTextString() {
        List<String> list = new ArrayList<>();
        list.add(I18n.translateToLocal(style.getName()));
        return list;
    }
}
