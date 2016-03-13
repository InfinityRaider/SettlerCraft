package com.InfinityRaider.settlercraft.settlement.settler.dialogue;

import com.InfinityRaider.settlercraft.api.v1.IDialogueOption;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.settlement.settler.dialogue.builder.DialogueOptionFollow;
import com.InfinityRaider.settlercraft.settlement.settler.dialogue.builder.DialogueOptionStay;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public abstract class DialogueOptionBase implements IDialogueOption {
    private final EntityPlayer player;
    private final ISettler settler;

    public DialogueOptionBase(EntityPlayer player, ISettler settler) {
        this.player = player;
        this.settler = settler;
    }

    @Override
    public List<IDialogueOption> getDialogueOptions(EntityPlayer player, ISettler settler) {
        List<IDialogueOption> list = new ArrayList<>();

        //profession
        if(settler.settlement() == null || isMayor()) {
            if (settler.profession() != null) {
                list.addAll(settler.profession().getProfessionSpecificDialogueOptions(player, settler));
            }
        }

        //inventory
        if(isMayor()) {
            list.add(new DialogueOptionShowInventory(player, settler));
        }
        list.add(new DialogueOptionRequestName(player, settler, this));

        //follow the player
        if(settler.settlement() == null || isMayor()) {
            EntityPlayer following = settler.getCurrentlyFollowingPlayer();
            if (following == null) {
                list.add(new DialogueOptionFollow(player, settler));
            } else {
                if (following.getUniqueID().equals(player.getUniqueID())) {
                    list.add(new DialogueOptionStay(player, settler));
                }
            }
        }

        //end conversation
        list.add(new DialogueOptionEndConversation(player, settler));

        return list;
    }

    @Override
    public boolean shouldEndInteraction() {
        return true;
    }

    protected EntityPlayer getPlayer() {
        return player;
    }

    protected ISettler getSettler() {
        return settler;
    }

    protected String getDiscriminator() {
        return "settlercraft.dialogue.";
    }

    protected boolean isMayor() {
        return getSettler().isMayor(getPlayer());
    }
}
