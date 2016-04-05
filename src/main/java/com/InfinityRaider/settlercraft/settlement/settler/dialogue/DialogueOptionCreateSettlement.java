package com.InfinityRaider.settlercraft.settlement.settler.dialogue;

import com.InfinityRaider.settlercraft.api.v1.IDialogueOption;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.settlement.building.BuildingStyleRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.translation.I18n;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DialogueOptionCreateSettlement extends DialogueOptionBase {
    public DialogueOptionCreateSettlement(EntityPlayer player, ISettler settler) {
        super(player, settler);
    }

    @Override
    public boolean onDialogueOptionSelected(EntityPlayer player, ISettler settler) {
        return false;
    }

    @Override
    public List<IDialogueOption> getDialogueOptions(EntityPlayer player, ISettler settler) {
        return BuildingStyleRegistry.getInstance().getBuildingStyles().stream().map(
                style -> new DialogueOptionSelectBuildingStyle(getPlayer(), getSettler(), style)).collect(Collectors.toList());
    }

    @Override
    public void onContainerClosed(EntityPlayer player, ISettler settler) {}

    @Override
    public List<String> getLocalizedDialogueAnswerString() {
        List<String> list = new ArrayList<>();
        list.add(I18n.translateToLocal(getDiscriminator() + "selectStyle"));
        return list;
    }

    @Override
    public List<String> getLocalizedDialogueQuestionString() {
        List<String> list = new ArrayList<>();
        list.add(I18n.translateToLocal(getDiscriminator() + "createSettlement1"));
        list.add(I18n.translateToLocal(getDiscriminator() + "createSettlement2"));
        return list;
    }
}
