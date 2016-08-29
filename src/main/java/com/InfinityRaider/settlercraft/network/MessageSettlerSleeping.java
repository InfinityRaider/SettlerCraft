package com.InfinityRaider.settlercraft.network;

import com.InfinityRaider.settlercraft.SettlerCraft;
import com.InfinityRaider.settlercraft.handler.SleepHandler;
import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageSettlerSleeping extends MessageBaseSettler<IMessage> {
    private BlockPos pos;
    private boolean sleep;
    private EntitySettler settler;

    public MessageSettlerSleeping() {
        super();
    }

    public MessageSettlerSleeping(EntitySettler settler, BlockPos pos, boolean sleep) {
        this();
        this.settler = settler;
        this.pos = pos;
        this.sleep = sleep;
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        if(ctx.side == Side.CLIENT) {
            World world = SettlerCraft.proxy.getClientWorld();
            if(sleep) {
                SleepHandler.getInstance().putSettlerToSleep(world, pos, world.getBlockState(pos), settler);
            } else {
                SleepHandler.getInstance().wakeSettlerUp(settler);
            }
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        this.sleep = buf.readBoolean();
        this.settler = this.readSettlerFromByteBuf(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        buf.writeBoolean(sleep);
        this.writeSettlerToByteBuf(buf, settler);
    }
}