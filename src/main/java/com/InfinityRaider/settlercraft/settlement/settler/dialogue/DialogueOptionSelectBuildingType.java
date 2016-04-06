package com.InfinityRaider.settlercraft.settlement.settler.dialogue;

import com.InfinityRaider.settlercraft.api.v1.IBuilding;
import com.InfinityRaider.settlercraft.api.v1.IBuildingType;
import com.InfinityRaider.settlercraft.api.v1.IDialogueOption;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.settlement.building.BuildingTypeRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.translation.I18n;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DialogueOptionSelectBuildingType extends DialogueOptionBase {
    private final IBuildingType type;
    private final List<IBuilding> buildings;

    public DialogueOptionSelectBuildingType(EntityPlayer player, ISettler settler, IBuildingType type) {
        super(player, settler);
        this.type = type;
        this.buildings =  settler.settlement().getBuildableBuildings(type);
    }

    @Override
    public List<IDialogueOption> getDialogueOptions(EntityPlayer player, ISettler settler) {
        List<IDialogueOption> list;
        if (buildings.size() > 0) {
            list = buildings.stream().map(building
                    -> new DialogueOptionSelectBuilding(player, settler, building)).collect(Collectors.toList());
        } else {
            list = BuildingTypeRegistry.getInstance().allBuildingTypes().stream().filter(type -> type != this.type).map(
                    type -> new DialogueOptionSelectBuildingType(player, settler, type)).collect(Collectors.toList());
        }
        list.add(new DialogueOptionEndConversation(player, settler));
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
        if(list.size() > 0) {
            list.add(I18n.translateToLocal(getDiscriminator() + "whatBuilding"));
        } else {
            list.add(I18n.translateToLocal(getDiscriminator() + "noBuilding"));
        }
        return list;
    }

    @Override
    public List<String> getLocalizedPlayerTextString() {
        List<String> list = new ArrayList<>();
        list.add(I18n.translateToLocal(type.unlocalizedName()));
        return list;

    }
}
