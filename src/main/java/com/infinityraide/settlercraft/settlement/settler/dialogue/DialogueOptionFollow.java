package com.infinityraide.settlercraft.settlement.settler.dialogue;

import com.infinityraide.settlercraft.api.v1.ISettler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

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
    public List<ITextComponent> getSettlerText() {
        List<ITextComponent> list = new ArrayList<>();
        if(this.follow) {
            list.add(new TextComponentTranslation(getDiscriminator() + "following"));
        } else {
            if(isMayor()) {
                list.add(new TextComponentTranslation(getDiscriminator() + "backToWork"));
            } else {
                list.add(new TextComponentTranslation(getDiscriminator() + "stayingPut"));

            }
        }
        return list;
    }

    @Override
    public List<ITextComponent> getPlayerText() {
        List<ITextComponent> list = new ArrayList<>();
        if(follow) {
            if (isMayor()) {
                list.add(new TextComponentTranslation(getDiscriminator() + "followMeCitizen"));
            } else {
                list.add(new TextComponentTranslation(getDiscriminator() + "followMe"));
            }
        } else {
            if(isMayor()) {
                list.add(new TextComponentTranslation(getDiscriminator() + "stayCitizen"));
            } else {
                list.add(new TextComponentTranslation(getDiscriminator() + "stay"));
            }
        }
        return list;
    }
}
