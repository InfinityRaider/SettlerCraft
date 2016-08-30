package com.InfinityRaider.settlercraft.settlement.settler.dialogue;

import com.InfinityRaider.settlercraft.api.v1.IDialogueOption;
import com.InfinityRaider.settlercraft.api.v1.ISettlementBuilding;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DialogueOptionAssignWorkplace extends DialogueOptionBase {
    private final ISettlementBuilding building;

    public DialogueOptionAssignWorkplace(EntityPlayer player, ISettler settler, ISettlementBuilding building) {
        super(player, settler);
        this.building = building;
    }

    @Override
    public List<IDialogueOption> getDialogueOptions(EntityPlayer player, ISettler settler) {
        return Collections.emptyList();
    }

    @Override
    public boolean onDialogueOptionSelected(EntityPlayer player, ISettler settler) {
        return true;
    }

    @Override
    public void onContainerClosed(EntityPlayer player, ISettler settler) {
        this.building.addWorker(settler);
    }

    @Override
    public List<ITextComponent> getSettlerText() {
        return Collections.emptyList();
    }

    @Override
    public List<ITextComponent> getPlayerText() {
        List<ITextComponent> list = new ArrayList<>();
        list.add(new TextComponentTranslation(getDiscriminator() + "assignWorkplace"));
        return list;
    }
}
