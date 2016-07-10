package com.InfinityRaider.settlercraft.settlement.settler.dialogue;

import com.InfinityRaider.settlercraft.api.v1.IDialogueOption;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.settlement.building.BuildingTypeRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.translation.I18n;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DialogueOptionBuildBuilding extends DialogueOptionBase {
    public DialogueOptionBuildBuilding(EntityPlayer player, ISettler settler) {
        super(player, settler);
    }

    @Override
    public List<IDialogueOption> getDialogueOptions(EntityPlayer player, ISettler settler) {
        return BuildingTypeRegistry.getInstance().allBuildingTypes().stream().filter(
                type -> settler.settlement().getBuildableBuildings(type).size() > 0).map(
                type -> new DialogueOptionSelectBuildingType(player, settler, type)).collect(Collectors.toList());
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
        list.add(I18n.translateToLocal(getDiscriminator() + "whatBuildingType"));
        return list;
    }

    @Override
    public List<String> getLocalizedPlayerTextString() {
        List<String> list = new ArrayList<>();
        list.add(I18n.translateToLocal(getDiscriminator() + "buildBuilding"));
        return list;
    }
}
