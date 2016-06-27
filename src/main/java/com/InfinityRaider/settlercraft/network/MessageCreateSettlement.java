package com.InfinityRaider.settlercraft.network;

import com.InfinityRaider.settlercraft.SettlerCraft;
import com.InfinityRaider.settlercraft.api.v1.ISettlement;
import com.InfinityRaider.settlercraft.settlement.Settlement;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageCreateSettlement extends MessageBase<IMessage> {
    private NBTTagCompound settlementTag;
    private int dimension;

    @SuppressWarnings("unused")
    public MessageCreateSettlement() {}

    public MessageCreateSettlement(ISettlement settlement) {
        this.settlementTag = settlement.writeSettlementToNBT(new NBTTagCompound());
        this.dimension = settlement.world().provider.getDimension();
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        if(ctx.side == Side.CLIENT && this.settlementTag != null) {
            World world = SettlerCraft.proxy.getClientWorld();
            if(world.provider.getDimension() == this.dimension) {
                ISettlement settlement = new Settlement(world);
                settlement.readSettlementFromNBT(this.settlementTag);
            }
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.settlementTag = ByteBufUtils.readTag(buf);
        this.dimension = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, this.settlementTag);
        buf.writeInt(this.dimension);
    }
}
