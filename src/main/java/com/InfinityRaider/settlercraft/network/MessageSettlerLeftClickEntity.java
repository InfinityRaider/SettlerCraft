package com.InfinityRaider.settlercraft.network;

import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import com.infinityraider.infinitylib.network.MessageBase;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageSettlerLeftClickEntity extends MessageBase<IMessage> {
    private EntitySettler settler;
    private Entity target;

    public MessageSettlerLeftClickEntity() {
        super();
    }

    public MessageSettlerLeftClickEntity(EntitySettler settler, Entity target) {
        this();
        this.settler = settler;
        this.target = target;
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        if(this.settler != null && this.target != null) {
            this.settler.attackTargetEntityWithCurrentItem(this.target);
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }
}
