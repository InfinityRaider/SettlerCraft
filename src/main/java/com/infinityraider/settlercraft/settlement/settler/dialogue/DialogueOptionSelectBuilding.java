package com.infinityraider.settlercraft.settlement.settler.dialogue;

import com.infinityraider.settlercraft.api.v1.IBuilding;
import com.infinityraider.settlercraft.api.v1.IDialogueOption;
import com.infinityraider.settlercraft.api.v1.ISettler;
import com.infinityraider.settlercraft.item.ItemBuildingPlanner;
import com.infinityraider.settlercraft.registry.ItemRegistry;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.ArrayList;
import java.util.List;

public class DialogueOptionSelectBuilding extends DialogueOptionBase {
    private final IBuilding building;

    public DialogueOptionSelectBuilding(EntityPlayer player, ISettler settler, IBuilding building) {
        super(player, settler);
        this.building = building;
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
        ItemBuildingPlanner planner = ItemRegistry.getInstance().itemBuildingPlanner;
        ItemStack stack = new ItemStack(planner, 1, 0);
        planner.setBuilding(stack, building);
        planner.setSettlement(stack, settler.settlement());
        player.inventory.addItemStackToInventory(stack);
    }

    @Override
    public List<ITextComponent> getSettlerText() {
        List<ITextComponent> list = new ArrayList<>();
        list.add(new TextComponentTranslation(getDiscriminator() + "planBuild"));
        return list;
    }

    @Override
    public List<ITextComponent> getPlayerText() {
        List<ITextComponent> list = new ArrayList<>();
        list.add(new TextComponentTranslation(getDiscriminator() + "buildNew"));
        list.add(new TextComponentTranslation(building.name()));
        return list;
    }
}
