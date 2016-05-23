package com.InfinityRaider.settlercraft.network;

import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
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
    protected void processMessage(MessageContext ctx) {
        if(ctx.side == Side.CLIENT && this.settler != null) {
            if(this.completed) {
                this.settler.setTaskCompleted();
            } else {
                this.settler.assignTask();
            }
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
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
}
