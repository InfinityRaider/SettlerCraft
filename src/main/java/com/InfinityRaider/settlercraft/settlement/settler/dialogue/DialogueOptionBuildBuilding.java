package com.InfinityRaider.settlercraft.settlement.settler.dialogue;

import com.InfinityRaider.settlercraft.api.v1.IDialogueOption;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.settlement.building.BuildingTypeRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

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
    public List<ITextComponent> getSettlerText() {
        List<ITextComponent> list = new ArrayList<>();
        list.add(new TextComponentTranslation(getDiscriminator() + "whatBuildingType"));
        return list;
    }

    @Override
    public List<ITextComponent> getPlayerText() {
        List<ITextComponent> list = new ArrayList<>();
        list.add(new TextComponentTranslation(getDiscriminator() + "buildBuilding"));
        return list;
    }
}
