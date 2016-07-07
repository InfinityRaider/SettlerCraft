package com.InfinityRaider.settlercraft.network;

import com.InfinityRaider.settlercraft.SettlerCraft;
import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.api.v1.ISettler;
import com.InfinityRaider.settlercraft.settlement.SettlementHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageAddInhabitant extends MessageBase<IMessage> {
    private int settlement_id;
    private EntityLivingBase settler;

    public MessageAddInhabitant() {
        super();
    }

    public MessageAddInhabitant(ISettlement settlement, EntityLivingBase settler) {
        this();
        this.settlement_id = settlement.id();
        this.settler = settler;
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        if(ctx.side == Side.CLIENT && this.settler != null && this.settler instanceof ISettler) {
            ISettlement settlement = SettlementHandler.getInstance().getSettlement(SettlerCraft.proxy.getClientWorld(), this.settlement_id);
            settlement.addInhabitant((ISettler) this.settler);
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
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
}
