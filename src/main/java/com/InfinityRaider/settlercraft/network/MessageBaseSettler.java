package com.InfinityRaider.settlercraft.network;

import com.InfinityRaider.settlercraft.settlement.settler.EntitySettler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class MessageBaseSettler<T extends IMessage> extends MessageBase<T> {
    public MessageBaseSettler() {
        super();
    }

    protected EntitySettler readSettlerFromByteBuf(ByteBuf buf) {
        Entity entity = readEntityFromByteBuf(buf);
        return (entity instanceof EntitySettler)?(EntitySettler) entity : null;
    }

    protected void writeSettlerToByteBuf(ByteBuf buf, EntitySettler settler) {
        writeEntityToByteBuf(buf, settler);
    }
}
