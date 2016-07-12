package com.InfinityRaider.settlercraft.settlement.settler.dialogue;

import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.settlement.settler.dialogue.DialogueOptionBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.translation.I18n;

import java.util.ArrayList;
import java.util.List;

public class DialogueOptionFollow extends DialogueOptionBase {
    private final boolean follow;

    public DialogueOptionFollow(EntityPlayer player, ISettler settler, boolean shouldFollow) {
        super(player, settler);
        this.follow = shouldFollow;
    }

    @Override
    public boolean onDialogueOptionSelected(EntityPlayer player, ISettler settler) {
        return true;
    }

    @Override
    public void onContainerClosed(EntityPlayer player, ISettler settler) {
        EntityPlayer current = settler.getCurrentlyFollowingPlayer();
        if(follow) {
            if (current == null) {
                settler.followPlayer(player);
            }
        } else {
            if(current != null && current.getUniqueID().equals(player.getUniqueID())) {
                settler.followPlayer(null);
            }
        }
    }

    @Override
    public List<String> getLocalizedSettlerTextString() {
        List<String> list = new ArrayList<>();
        if(this.follow) {
            list.add(I18n.translateToLocal(getDiscriminator() + "following"));
        } else {
            if(isMayor()) {
                list.add(I18n.translateToLocal(getDiscriminator() + "backToWork"));
            } else {
                list.add(I18n.translateToLocal(getDiscriminator() + "stayingPut"));

            }
        }
        return list;
    }

    @Override
    public List<String> getLocalizedPlayerTextString() {
        List<String> list = new ArrayList<>();
        if(follow) {
            if (isMayor()) {
                list.add(I18n.translateToLocal(getDiscriminator() + "followMeCitizen"));
            } else {
                list.add(I18n.translateToLocal(getDiscriminator() + "followMe"));
            }
        } else {
            if(isMayor()) {
                list.add(I18n.translateToLocal(getDiscriminator() + "stayCitizen"));
            } else {
                list.add(I18n.translateToLocal(getDiscriminator() + "stay"));
            }
        }
        return list;
    }
}
