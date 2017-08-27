package com.InfinityRaider.settlercraft.network;

import com.InfinityRaider.settlercraft.SettlerCraft;
import com.infinityraider.infinitylib.network.MessageBase;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageCloseContainer extends MessageBase<IMessage> {
    public MessageCloseContainer() {
        super();
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        SettlerCraft.proxy.getClientPlayer().closeScreen();
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }
}
