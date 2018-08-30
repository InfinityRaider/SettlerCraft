package com.infinityraider.settlercraft.settlement.settler.dialogue;

import com.infinityraider.settlercraft.api.v1.IDialogueOption;
import com.infinityraider.settlercraft.api.v1.IDialogueOptionCreator;
import com.infinityraider.settlercraft.api.v1.ISettlementBuilding;
import com.infinityraider.settlercraft.api.v1.ISettler;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public abstract class DialogueOptionBase implements IDialogueOption {
    protected static final IDialogueOptionCreator DIALOGUE_FACTORY = DialogueOptionCreator.getInstance();

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

        //mayor specific
        if(isMayor()) {
            list.add(DIALOGUE_FACTORY.dialogueOptionDescribeTask(player, settler, this));
            list.add(DIALOGUE_FACTORY.dialogueOptionShowInventory(player, settler));
            EntityAgeable entity = settler.getEntityImplementation();
            ISettlementBuilding building = settler.settlement().getBuildingForLocation(entity.posX + 0.5, entity.posY + 1, entity.posZ + 0.5);
            if(building != null) {
                if(building.canDoWorkHere(settler) && settler.workPlace() != building) {
                    list.add(DIALOGUE_FACTORY.dialogueOptionAssignWorkplace(player, settler, building));
                }
                if(building.canLiveHere(settler) && settler.home() != building) {
                    list.add(DIALOGUE_FACTORY.dialogueOptionAssignHouse(player, settler, building));
                }
            }
        }
        list.add(DIALOGUE_FACTORY.dialogueOptionRequestName(player, settler, this));

        //follow the player
        if(settler.settlement() == null || isMayor()) {
            EntityPlayer following = settler.getCurrentlyFollowingPlayer();
            if (following == null) {
                list.add(DIALOGUE_FACTORY.dialogueOptionFollowPlayer(player, settler, true));
            } else {
                if (following.getUniqueID().equals(player.getUniqueID())) {
                    list.add(DIALOGUE_FACTORY.dialogueOptionFollowPlayer(player, settler, false));
                }
            }
        }

        //end conversation
        list.add(DIALOGUE_FACTORY.dialogueOptionEndConversation(player, settler));

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
