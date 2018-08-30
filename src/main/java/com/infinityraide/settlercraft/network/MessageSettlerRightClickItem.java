package com.infinityraide.settlercraft.network;

import com.infinityraide.settlercraft.settlement.settler.EntitySettler;
import com.infinityraider.infinitylib.network.MessageBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageSettlerRightClickItem extends MessageBase<IMessage> {
    private EntitySettler settler;
    private EnumHand hand;

    public MessageSettlerRightClickItem() {
        super();
    }

    public MessageSettlerRightClickItem(EntitySettler settler, EnumHand hand) {
        this();
        this.settler = settler;
        this.hand = hand;
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        if(settler != null) {
            ItemStack stack = settler.getHeldItem(hand);
            if(stack != null) {
                settler.getInteractionController().processRightClick(stack, hand);
            }
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }
}
