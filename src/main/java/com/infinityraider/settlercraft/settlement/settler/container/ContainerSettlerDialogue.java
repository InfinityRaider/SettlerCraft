package com.infinityraider.settlercraft.settlement.settler.container;

import com.infinityraider.settlercraft.api.v1.IDialogueOption;
import com.infinityraider.settlercraft.api.v1.ISettler;
import com.infinityraider.settlercraft.network.MessageSyncDialogueText;
import com.infinityraider.settlercraft.settlement.settler.dialogue.DialogueOptionDefault;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.List;

public class ContainerSettlerDialogue extends ContainerSettler {
    private IDialogueOption current;
    private List<IDialogueOption> dialogueOptions;

    public ContainerSettlerDialogue(EntityPlayer player, ISettler settler) {
        super(player, settler);
        initDialogueOptions();
        getSettler().setConversationPartner(player);
    }

    public IDialogueOption getCurrentDialogueOption() {
        return current;
    }

    public List<IDialogueOption> getDialogueOptions() {
        return dialogueOptions;
    }

    public void onDialogueOptionClicked(int index) {
        if(index >= 0 && index < dialogueOptions.size()) {
            current = dialogueOptions.get(index);
            if(current.onDialogueOptionSelected(this.getPlayer(), this.getSettler())) {
                this.closeContainer();
            } else {
                this.initDialogueOptions();
            }
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer player, ISettler settler) {
        if(!player.getEntityWorld().isRemote) {
            current.onContainerClosed(player, settler);
        }
    }

    private void initDialogueOptions() {
        if(!getPlayer().getEntityWorld().isRemote && (getPlayer() instanceof EntityPlayerMP)) {
            if (current == null) {
                current = new DialogueOptionDefault(getPlayer(), getSettler());
            }
            dialogueOptions = current.getDialogueOptions(getPlayer(), getSettler());
            new MessageSyncDialogueText(this).sendTo((EntityPlayerMP) getPlayer());
        }
    }
}
