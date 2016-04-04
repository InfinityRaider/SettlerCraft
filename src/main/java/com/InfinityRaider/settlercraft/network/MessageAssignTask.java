package com.InfinityRaider.settlercraft.network;

import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageAssignTask extends MessageBaseSettler {
    private EntitySettler settler;
    private boolean completed;

    @SuppressWarnings("unused")
    public MessageAssignTask() {
    }

    public MessageAssignTask(EntitySettler settler, boolean completed) {
        this.settler = settler;
        this.completed = completed;
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.settler = this.readSettlerFromByteBuf(buf);
        this.completed = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        this.writeSettlerToByteBuf(buf, this.settler);
        buf.writeBoolean(this.completed);
    }

    public static class MessageHandler implements IMessageHandler<MessageAssignTask, IMessage> {
        @Override
        public IMessage onMessage(MessageAssignTask message, MessageContext ctx) {
            if(ctx.side == Side.CLIENT && message.settler != null) {
                if(message.completed) {
                    message.settler.setTaskCompleted();
                } else {
                    message.settler.assignTask();
                }
            }
            return null;
        }
    }
}
