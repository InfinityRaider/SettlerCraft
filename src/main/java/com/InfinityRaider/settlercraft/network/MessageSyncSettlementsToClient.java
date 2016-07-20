package com.InfinityRaider.settlercraft.network;

import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.settlement.SettlementHandler;
import com.infinityraider.infinitylib.network.MessageBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MessageSyncSettlementsToClient extends MessageBase<IMessage> {
    List<NBTTagCompound> settlementTags;

    public MessageSyncSettlementsToClient() {
        super();
        this.settlementTags = new ArrayList<>();
    }

    public MessageSyncSettlementsToClient(ISettlement... settlements) {
        this();
        for(ISettlement settlement : settlements) {
            this.settlementTags.add(settlement.writeSettlementToNBT(new NBTTagCompound()));
        }
    }

    public MessageSyncSettlementsToClient(List<ISettlement> settlements) {
        this();
        this.settlementTags.addAll(settlements.stream().map(settlement -> settlement.writeSettlementToNBT(new NBTTagCompound())).collect(Collectors.toList()));
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        if(ctx.side == Side.CLIENT) {
            for(NBTTagCompound tag : settlementTags) {
                SettlementHandler.getInstanceClient().syncSettlementFromNBT(tag);
            }
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int size = buf.readInt();
        for(int i = 0; i < size; i++) {
            this.settlementTags.add(ByteBufUtils.readTag(buf));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.settlementTags.size());
        for(NBTTagCompound tag : this.settlementTags) {
            ByteBufUtils.writeTag(buf, tag);
        }
    }
}
