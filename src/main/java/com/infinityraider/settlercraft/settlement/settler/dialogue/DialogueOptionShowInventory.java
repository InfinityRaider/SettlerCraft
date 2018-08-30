package com.infinityraider.settlercraft.settlement.settler.dialogue;

import com.infinityraider.settlercraft.api.v1.IDialogueOption;
import com.infinityraider.settlercraft.api.v1.ISettler;
import com.infinityraider.settlercraft.handler.GuiHandlerSettler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

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
    public boolean shouldEndInteraction() {
        return false;
    }

    @Override
    public void onContainerClosed(EntityPlayer player, ISettler settler) {
        if(!player.getEntityWorld().isRemote) {
            GuiHandlerSettler.getInstance().openSettlerInventoryContainer(player, settler);
        }
    }

    @Override
    public List<ITextComponent> getSettlerText() {
        List<ITextComponent> list = new ArrayList<>();
        list.add(new TextComponentTranslation(getDiscriminator() + "showInventory"));
        return list;
    }

    @Override
    public List<ITextComponent> getPlayerText() {
        List<ITextComponent> list = new ArrayList<>();
        list.add(new TextComponentTranslation(getDiscriminator() + "requestInventory"));
        return list;
    }
}
