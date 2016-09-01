package com.InfinityRaider.settlercraft.network;

import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Message to fire the forge event when a settler right clicks in air with an item
 */
public class MessageSettlerRightClickAir extends MessageBaseSettler<IMessage> {
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
        if(ctx.side == Side.CLIENT && settler != null) {
            ForgeHooks.onEmptyClick(settler.getFakePlayerImplementation(), hand);
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.settler = this.readSettlerFromByteBuf(buf);
        this.hand = EnumHand.values()[buf.readInt()];
    }

    @Override
    public void toBytes(ByteBuf buf) {
        this.writeSettlerToByteBuf(buf, settler);
        buf.writeInt(hand.ordinal());
    }
}
