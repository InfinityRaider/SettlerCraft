package com.infinityraide.settlercraft.settlement.settler.profession.builder;

import com.infinityraide.settlercraft.api.v1.IDialogueOption;
import com.infinityraide.settlercraft.api.v1.ISettler;
import com.infinityraide.settlercraft.settlement.settler.dialogue.DialogueOptionBase;
import com.infinityraide.settlercraft.settlement.settler.dialogue.DialogueOptionEndConversation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.ArrayList;
import java.util.List;

public class DialogueOptionTaskBuilder extends DialogueOptionBase {
    private final TaskBuildBuilding task;

    public DialogueOptionTaskBuilder(EntityPlayer player, ISettler settler, TaskBuildBuilding task) {
        super(player, settler);
        this.task = task;
    }

    @Override
    public List<IDialogueOption> getDialogueOptions(EntityPlayer player, ISettler settler) {
        List<IDialogueOption> list = new ArrayList<>();
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
        list.add(task.describeJob());
        return list;
    }

    @Override
    public List<ITextComponent> getPlayerText() {
        List<ITextComponent> list = new ArrayList<>();
        list.add(new TextComponentTranslation(getDiscriminator() + "task.builder"));
        return list;
    }
}
