package com.InfinityRaider.settlercraft.settlement.settler.container;

import com.InfinityRaider.settlercraft.api.v1.IDialogueOption;
import com.InfinityRaider.settlercraft.settlement.settler.dialogue.DialogueOptionDefault;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerSettlerDialogue extends ContainerSettler {
    private IDialogueOption current;
    private List<IDialogueOption> dialogueOptions;

    public ContainerSettlerDialogue(EntityPlayer player) {
        super(player);
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
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        player.openContainer = player.inventoryContainer;
        if(current != null) {
            current.onContainerClosed(player, getSettler());
        }
        getSettler().setConversationPartner(null);
    }

    private void initDialogueOptions() {
        if(current == null) {
            current = new DialogueOptionDefault(getPlayer(), getSettler());
        }
        dialogueOptions = current.getDialogueOptions(getPlayer(), getSettler());
    }
}
