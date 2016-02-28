package com.InfinityRaider.settlercraft.network;

import com.InfinityRaider.settlercraft.settlement.settler.container.ContainerSettlerDialogue;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageDialogueOptionSelected extends MessageBase {
    private int id;

    @SuppressWarnings("unused")
    public MessageDialogueOptionSelected() {}

    public MessageDialogueOptionSelected(int id) {
        this.id = id;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.id = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.id);
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.SERVER;
    }

    public static class MessageHandler implements IMessageHandler<MessageDialogueOptionSelected, IMessage> {
        @Override
        public IMessage onMessage(MessageDialogueOptionSelected message, MessageContext ctx) {
            if(ctx.side == Side.SERVER) {
                EntityPlayer player = ctx.getServerHandler().playerEntity;
                Container container = player.openContainer;
                if(container instanceof ContainerSettlerDialogue) {
                    ((ContainerSettlerDialogue) container).onDialogueOptionClicked(message.id);
                }
            }
            return null;
        }
    }
}
