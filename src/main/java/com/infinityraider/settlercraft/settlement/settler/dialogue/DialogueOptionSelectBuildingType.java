package com.infinityraider.settlercraft.settlement.settler.dialogue;

import com.infinityraider.settlercraft.api.v1.IBuilding;
import com.infinityraider.settlercraft.api.v1.IBuildingType;
import com.infinityraider.settlercraft.api.v1.IDialogueOption;
import com.infinityraider.settlercraft.api.v1.ISettler;
import com.infinityraider.settlercraft.settlement.building.BuildingTypeRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

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
    public List<ITextComponent> getSettlerText() {
        List<ITextComponent> list = new ArrayList<>();
        if(buildings.size() > 0) {
            list.add(new TextComponentTranslation(getDiscriminator() + "whatBuilding"));
        } else {
            list.add(new TextComponentTranslation(getDiscriminator() + "noBuilding"));
        }
        return list;
    }

    @Override
    public List<ITextComponent> getPlayerText() {
        List<ITextComponent> list = new ArrayList<>();
        list.add(new TextComponentTranslation(type.unlocalizedName()));
        return list;
    }
}
