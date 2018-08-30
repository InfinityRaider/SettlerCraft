package com.infinityraide.settlercraft.network;

import com.infinityraide.settlercraft.settlement.settler.EntitySettler;
import com.infinityraider.infinitylib.network.MessageBase;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Message to fire the forge event when a settler right clicks in air with an item
 */
public class MessageSettlerRightClickAir extends MessageBase<IMessage> {
    private EntitySettler settler;
    private EnumHand hand;

    public MessageSettlerRightClickAir() {
        super();
    }

    public MessageSettlerRightClickAir(EntitySettler settler, EnumHand hand) {
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
        if(this.settler != null) {
            ForgeHooks.onEmptyClick(settler.getFakePlayerImplementation(), hand);
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }
}
