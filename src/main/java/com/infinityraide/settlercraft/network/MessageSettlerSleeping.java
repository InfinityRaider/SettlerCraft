package com.infinityraide.settlercraft.network;

import com.infinityraide.settlercraft.SettlerCraft;
import com.infinityraide.settlercraft.handler.SleepHandler;
import com.infinityraide.settlercraft.settlement.settler.EntitySettler;
import com.infinityraider.infinitylib.network.MessageBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageSettlerSleeping extends MessageBase<IMessage> {
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
        World world = SettlerCraft.proxy.getClientWorld();
        if (sleep) {
            SleepHandler.getInstance().putSettlerToSleep(world, pos, world.getBlockState(pos), settler);
        } else {
            SleepHandler.getInstance().wakeSettlerUp(settler);
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }
}
