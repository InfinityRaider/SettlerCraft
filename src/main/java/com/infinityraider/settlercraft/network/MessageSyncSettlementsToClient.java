package com.infinityraider.settlercraft.network;

import com.infinityraider.settlercraft.api.v1.ISettlement;
import com.infinityraider.settlercraft.settlement.SettlementHandler;
import com.infinityraider.infinitylib.network.MessageBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

public class MessageSyncSettlementsToClient extends MessageBase<IMessage> {
    NBTTagCompound[] settlementTags;

    public MessageSyncSettlementsToClient() {
        super();
    }

    public MessageSyncSettlementsToClient(ISettlement... settlements) {
        this();
        this.settlementTags = new NBTTagCompound[settlements.length];
        for(int i = 0; i < settlements.length; i++) {
            this.settlementTags[i] = settlements[i].writeSettlementToNBT(new NBTTagCompound());
        }
    }

    public MessageSyncSettlementsToClient(List<ISettlement> settlements) {
        this();
        this.settlementTags = new NBTTagCompound[settlements.size()];
        for(int i = 0; i < settlements.size(); i++) {
            this.settlementTags[i] = settlements.get(i).writeSettlementToNBT(new NBTTagCompound());
        }
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        for (NBTTagCompound tag : settlementTags) {
            SettlementHandler.getInstanceClient().syncSettlementFromNBT(tag);
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }
}
