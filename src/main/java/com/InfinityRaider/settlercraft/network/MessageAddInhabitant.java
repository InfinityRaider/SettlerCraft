package com.InfinityRaider.settlercraft.network;

import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.settlement.SettlementHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageAddInhabitant extends MessageBase {
    private int settlement_id;
    private EntityLivingBase settler;

    @SuppressWarnings("unused")
    public MessageAddInhabitant() {}

    public MessageAddInhabitant(ISettlement settlement, EntityLivingBase settler) {
        this.settlement_id = settlement.id();
        this.settler = settler;
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.settlement_id = buf.readInt();
        this.settler = (EntityLivingBase) readEntityFromByteBuf(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.settlement_id);
        writeEntityToByteBuf(buf, this.settler);
    }

    public static class MessageHandler implements IMessageHandler<MessageAddInhabitant, IMessage> {
        @Override
        public IMessage onMessage(MessageAddInhabitant message, MessageContext ctx) {
            if(ctx.side == Side.CLIENT && message.settler != null && message.settler instanceof ISettler) {
                ISettlement settlement = SettlementHandler.getInstance().getSettlement(message.settlement_id);
                if(settlement != null) {
                    settlement.addInhabitant((ISettler) message.settler);
                } else {
                    SettlementHandler.getInstance().addSettlerToInhabitantBuffer(message.settlement_id, (ISettler) message.settler);
                }
            }
            return null;
        }
    }
}
