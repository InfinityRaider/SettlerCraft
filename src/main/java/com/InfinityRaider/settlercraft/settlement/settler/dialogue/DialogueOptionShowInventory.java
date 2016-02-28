package com.InfinityRaider.settlercraft.settlement.settler.dialogue;

import com.InfinityRaider.settlercraft.api.v1.IDialogueOption;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.handler.GuiHandler;
import com.InfinityRaider.settlercraft.settlement.SettlementHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.List;

public class DialogueOptionShowInventory extends DialogueOptionBase {
    public DialogueOptionShowInventory(EntityPlayer player, ISettler settler) {
        super(player, settler);
    }

    @Override
    public boolean onDialogueOptionSelected(EntityPlayer player, ISettler settler) {
        return true;
    }

    @Override
    public List<IDialogueOption> getDialogueOptions(EntityPlayer player, ISettler settler) {
        return new ArrayList<>();
    }

    @Override
    public void onContainerClosed(EntityPlayer player, ISettler settler) {
        SettlementHandler.getInstance().interact(player, settler);
        if(!player.worldObj.isRemote) {
            GuiHandler.getInstance().openSettlerInventoryContainer(player);
        }
    }

    @Override
    public List<String> getLocalizedDialogueAnswerString() {
        List<String> list = new ArrayList<>();
        list.add(StatCollector.translateToLocal(getDiscriminator() + "showInventory"));
        return list;
    }

    @Override
    public List<String> getLocalizedDialogueQuestionString() {
        List<String> list = new ArrayList<>();
        list.add(StatCollector.translateToLocal(getDiscriminator() + "requestInventory"));
        return list;
    }
}
